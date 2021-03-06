# Phillip Huang
# Converts GeoJSON metadata on precinct objects into a more readable format

import json
import re

CO_IN = r'C:/Users/phill/CSE 308/Data/Colorado/CO_final.json'
NH_IN = r'C:/Users/phill/CSE 308/Data/New Hampshire/nh_final.json'
NY_IN = r'C:/Users/phill/CSE 308/Data/New York/ny_final.json'

CO_OUT = '../precincts/co_precincts.json'
NH_OUT = '../precincts/nh_precincts.json'
NY_OUT = '../precincts/ny_precincts.json'

CO_PROPS = {
    'GEOID10': 'geo_id',
    # 'VTDST10': 'precinct_code',
    'STATEFP10': 'state_code',
    'COUNTYFP10': 'county_code',
    # 'NAME10': 'precinct_name',
    'INTPTLAT10': 'latitude',
    'INTPTLON10': 'longitude',
    'VAP': 'voting_age_pop',
    'POP100': 'population',
    # 'ALAND10': 'land_area',
    # 'AWATER10': 'water_area',
    'PRES08__D': 'dem_votes',
    'PRES08__R': 'rep_votes',
    # 'PRES08__D': 'dem_pres_votes_2008',
    # 'PRES08__R': 'rep_pres_votes_2008',
    # 'PRES08_MP': 'other_pres_votes_2008',
    # 'USSEN08_D': 'dem_senate_votes_2008',
    # 'USSEN08_R': 'rep_senate_votes_2008',
    # 'USSEN08_MP': 'other_senate_votes_2008',
    # 'USHSE08_D': 'dem_house_votes_2008',
    # 'USHSE08_R': 'rep_house_votes_2008',
    # 'REGVTR08_D': 'registered_dems_2008',
    # 'REGVTR08_R': 'registered_reps_2008',
    # 'REGVTR08_M': 'registered_other_2008',
    # 'REGVTR08_U': 'registered_unaffiliated_2008',
    # 'USSEN10D': 'dem_senate_votes_2010',
    # 'USSEN10R': 'rep_senate_votes_2010',
    # 'USSEN10MP': 'other_senate_votes_2010',
    # 'USHSE10D': 'dem_house_votes_2010',
    # 'USHSE10R': 'rep_house_votes_2010',
    # 'REGVTR10D': 'registered_dems_2010',
    # 'REGVTR10R': 'registered_reps_2010',
    # 'REGVTR10U': 'registered_unaffiliated_2010',
    # 'REGVTR10M': 'registered_other_2010',
}

NH_PROPS = {
    'GEOID10': 'geo_id',
    # 'VTDST10': 'precinct_code',
    'STATEFP10': 'state_code',
    'COUNTYFP10': 'county_code',
    # 'NAME10': 'precinct_name',
    'INTPTLAT10': 'latitude',
    'INTPTLON10': 'longitude',
    # 'ALAND10': 'land_area',
    # 'AWATER10': 'water_area',
    'VAP': 'voting_age_pop',
    'POP100': 'population',
    'PRES_DEM08': 'dem_votes',
    'PRES_REP08': 'rep_votes',
    # 'PRES_DEM08': 'dem_pres_votes_2008',
    # 'PRES_REP08': 'rep_pres_votes_2008',
    # 'US_DVOTE_0': 'dem_senate_votes_2008',
    # 'US_RVOTE_0': 'rep_senate_votes_2008',
    # 'US_DVOTE_1': 'dem_senate_votes_2004',
    # 'US_RVOTE_1': 'rep_senate_votes_2004',
    # 'UH_DVOTE_0': 'dem_house_votes_2008',
    # 'UH_RVOTE_0': 'rep_house_votes_2008',
    # 'UH_DVOTE_1': 'dem_house_votes_2006',
    # 'UH_RVOTE_1': 'rep_house_votes_2006',
    # 'UH_DVOTE_2': 'dem_house_votes_2004',
    # 'UH_RVOTE_2': 'rep_house_votes_2004',
}

NY_PROPS = {
    'GEOID10': 'geo_id',
    # 'VTDST10': 'precinct_code',
    'STATEFP10': 'state_code',
    'COUNTYFP10': 'county_code',
    # 'NAME10': 'precinct_name',
    'INTPTLAT10': 'latitude',
    'INTPTLON10': 'longitude',
    # 'ALAND10': 'land_area',
    # 'AWATER10': 'water_area',
    'VAP': 'voting_age_pop',
    'POP100': 'population',
    'USS_2_DVOT': 'dem_votes',
    'USS_2_RVOT': 'rep_votes',
    # 'USS_2_DVOT': 'dem_senate_votes_2010',
    # 'USS_2_RVOT': 'rep_senate_votes_2010',
    # 'USS_6_DVOT': 'dem_special_senate_votes_2010',
    # 'USS_6_RVOT': 'rep_special_senate_votes_2010',
}

STATE_CODES = {
    "33": "NH",
    "36": "NY",
    "08": "CO",
}



def editgeojson(infile, props, outfile):
    print('loading', infile)
    with open(infile, 'r') as f:
        geojson = json.load(f)

    features = geojson['features']
    # print(len(features))
    for feature in features:
        properties = feature['properties']
        feature['properties'] = {
            props[prop]: value for prop, value in properties.items() if prop in props
        }
    for prop in props.values():
        if prop not in feature['properties']:
            print('Missing property:', prop)
    geojson['features'] = [f for f in features if int(f['properties']['population'])
                           or not re.match(r'\w+(999\d|ZZZZZZ)', f['properties']['geo_id'])]
    print(f'Before: {len(features)}, After: {len(geojson["features"])}')
    print('dumping file')
    with open(outfile, 'w') as f:
        json.dump(geojson, f)


def main():
    # for infile, props, outfile in (
    #         # (CO_IN, CO_PROPS, CO_OUT),
    #         (NH_IN, NH_PROPS, NH_OUT),
    #         # (NY_IN, NY_PROPS, NY_OUT),
    # ):
    #     editgeojson(infile, props, outfile)

    state = 'co'
    infile = '../precincts/{}_precincts.json'.format(state)
    outfile = '../precincts/{}_precincts_geo.json'.format(state)

    obj_props = {
        'id',
        'latitude',
        'longitude',
        'population',
        'votes',
        'district',
        'area',
        'perimeter',
        'demographics',
    }

    with open(infile, 'r') as f:
        geojson = json.load(f)

    ids = {}
    precincts = []
    for i, feature in enumerate(geojson['features'], start=1):
        props = feature['properties']
        props['id'] = i
        ids[props['geo_id']] = i
        props['votes'] = {
            'DEMOCRAT': props['dem_votes'],
            'REPUBLICAN': props['rep_votes']
        }
        props['latitude'] = float(props['latitude'])
        props['longitude'] = float(props['longitude'])
        props['district'] = int(props['district'])

        new_props = {k: v for k, v in props.items() if k in obj_props}
        feature['properties'] = new_props
        precincts.append(new_props)

    with open(outfile, 'w') as f:
        json.dump(geojson, f)

    # neighbors_in = '../neighbors/{}_neighborders.json'.format(state)
    # neighbors_out = '../neighbors/{}_neighbors.json'.format(state)
    # with open(neighbors_in, 'r') as f:
    #     neighbors_json = json.load(f)
    #
    # new_neighbors = {
    #     ids[p]: {
    #         ids[n]: b for n, b in ns.items()
    #     } for p, ns in neighbors_json.items()
    # }
    # with open(neighbors_out, 'w') as f:
    #     json.dump(new_neighbors, f)


if __name__ == '__main__':
    main()
