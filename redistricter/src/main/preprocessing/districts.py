# Phillip Huang
# Matches precincts with their districts using their GeoJSON shapes

from collections import defaultdict

import requests
import json
from shapely.geometry import shape as Shape, mapping
from shapely.ops import cascaded_union

URL_TEMPLATE = 'https://raw.githubusercontent.com/unitedstates/districts/gh-pages/cds/2016/{}-{}/shape.geojson'

STATE = 'ny'

DISTS_FILE = '../districts/{}_dists.json'.format(STATE)
NEW_DISTS_FILE = '../districts/{}_dists2.json'.format(STATE)
# NEW_DISTS_FILE = '../districts/{}_dists3.json'.format(STATE)
PRECINCTS_FILE = '../precincts/{}_precincts.json'.format(STATE)
# PRECINCTS_FILE = 'C:/Users/phill/CSE 308/Data/Colorado/{}_geo.json'.format(STATE)


NUM_DISTRICTS = {
    'co': 7,
    'nh': 2,
    'ny': 27,
}


def requestdists(state, ndists):
    geojson = {"type": "FeatureCollection", "features": []}
    features = geojson['features']
    for dist in range(1, ndists + 1):
        print(f'retreiving {state.upper()}-{dist}')
        shape = requests.get(URL_TEMPLATE.format(state.upper(), dist)).text
        shape = json.loads(shape)
        features.append(shape)

    print('dumping')
    with open(DISTS_FILE.format(state), 'w') as f:
        json.dump(geojson, f)


def loadshapes(infile):
    with open(infile, 'r') as f:
        features = json.load(f)['features']

    shapes = []
    for feature in features:
        shape = Shape(feature['geometry'])
        shape.properties = feature['properties']
        shapes.append(shape)

    return shapes


ANOMALIES = {
    '36047811': '9',
    '360471168': '7',
    '36047894': '10',
    '36047895': '10',
    '360611053': '7',
    '36081103': '5',
    '36081102': '5',
    '36081101': '5',
    '36081100': '5',
    '36081104': '5',
    '36029184': '26',
}


def compute_dist_precs(districts, precincts):
    precinct_ct = 0
    d = defaultdict(list)
    for precinct in precincts:
        precinct_ct += 1
        if precinct_ct % 500 == 0:
            print('checking precinct', precinct_ct)

        prec_id = precinct.properties['geo_id']
        if STATE == 'ny' and prec_id in ANOMALIES:
            print(f'Using anomaly: {prec_id}: {ANOMALIES[prec_id]}')
            d[ANOMALIES[prec_id]].append(precinct)
            precinct.properties['district'] = ANOMALIES[prec_id]
            continue

        centroid = precinct.centroid
        for district in districts:
            if centroid.within(district)\
                    and precinct.intersection(district).area * 2 > precinct.area:
                d[district.properties['district']].append(precinct)
                precinct.properties['district'] = district.properties['district']
                break
        else:
            print(f'Precinct irregularity: {prec_id}')
            # TODO better assignment
            intersecting = [district for district in districts if precinct.intersects(district)]
            try:
                district = min(intersecting, key=lambda dist: centroid.distance(dist))
                d[district.properties['district']].append(precinct)
                precinct.properties['district'] = district.properties['district']
            except ValueError:
                print('Unable to match district.')
                precinct.properties['district'] = 'N/A'

    return d

def main():
    districts = loadshapes(DISTS_FILE)
    precincts = loadshapes(PRECINCTS_FILE)
    dps = compute_dist_precs(districts, precincts)

    print('Creating adjusted districts')
    new_dists = []
    for d, ps in dps.items():
        print('Coalescing district', d)
        shape = cascaded_union(ps)
        shape.properties = {
            'district': d,
            # 'area': sum(int(p.properties['land_area']) for p in ps),
            'area': shape.area,
            'perimeter': shape.boundary.length,
        }
        new_dists.append(shape)

    print('dumping new districts file')
    with open(NEW_DISTS_FILE, 'w') as infile:
        features = [{
            'type': 'Feature',
            'geometry': mapping(dist),
            'properties': dist.properties
        } for dist in new_dists]
        geojson = {'type': 'FeatureCollection', 'features': features}
        json.dump(geojson, infile)

    # print('adding precinct areas/perimeters')
    # for precinct in precincts:
    #     precinct.properties.update({
    #         'area': precinct.area,
    #         'perimeter': precinct.boundary.length,
    #     })
    # print('dumping precincts file')
    # with open(PRECINCTS_FILE, 'w') as f:
    #     features = [{
    #         'type': 'Feature',
    #         'geometry': mapping(precinct),
    #         'properties': precinct.properties
    #     } for precinct in precincts]
    #     geojson = {'type': 'FeatureCollection', 'features': features}
    #     json.dump(geojson, f)


if __name__ == '__main__':
    main()
