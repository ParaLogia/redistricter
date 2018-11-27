# Phillip Huang

import requests
import json
from shapely.geometry import shape as Shape, mapping

URL_TEMPLATE = 'https://raw.githubusercontent.com/unitedstates/districts/gh-pages/cds/2016/{}-{}/shape.geojson'

STATE = 'ny'

DISTS_FILE = '../districts/{}_dists.json'.format(STATE)
PRECINCTS_FILE = '../precincts/{}_geo.json'.format(STATE)


NUM_DISTRICTS = {
    'co': 7,
    'nh': 2,
    'ny': 27,
}


def requestdists(state, ndists):
    geojson = {"type": "FeatureCollection", "features": []}
    features = geojson['features']
    for dist in range(1, ndists + 1):
        print(f'retreiving {state}-{dist}')
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


def getprecinctdistricts(districts, precincts):
    precinct_ct = 0
    for precinct in precincts:
        precinct_ct += 1
        if precinct_ct % 100 == 0:
            print('checking precinct', precinct_ct)
        for district in districts:
            if precinct.centroid.within(district):
                precinct.properties['district'] = district.properties['district']
                break
        else:
            print(f'Precinct irregularity: {precinct.properties["geo_id"]}')
            for district in districts:
                if precinct.intersects(district):
                    precinct.properties['district'] = district.properties['district']
                    break
            else:
                print('Unable to match district.')
                precinct.properties['district'] = 'N/A'

    return precincts


def main():
    districts = loadshapes(DISTS_FILE)
    precincts = loadshapes(PRECINCTS_FILE)
    precincts = getprecinctdistricts(districts, precincts)

    with open(PRECINCTS_FILE, 'w') as f:
        features = [{
            'type': 'Feature',
            'geometry': mapping(precinct),
            'properties': precinct.properties
        } for precinct in precincts]
        geojson = {'type': 'FeatureCollection', 'features': features}
        json.dump(geojson, f)


if __name__ == '__main__':
    main()
