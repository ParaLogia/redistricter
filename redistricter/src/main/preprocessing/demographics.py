# Applies demographic data to precincts by interpolating district data

import csv
import json
from collections import defaultdict
from itertools import cycle

STATE = 'co'
PRECINCTS_FILE = '../precincts/{}_precincts.json'.format(STATE)
PRECINCTS_FILE_RACE = '../precincts/{}_precincts.json'.format(STATE)


def load_geojson(infile):
    with open(infile, 'r') as f:
        geojson = json.load(f)

    return geojson


COUNTIES = {
    'co': ['Adams', 'Alamosa', 'Arapahoe', 'Archuleta', 'Baca', 'Bent', 'Boulder', 'Broomfield', 'Chaffee', 'Cheyenne', 'Clear Creek', 'Conejos', 'Costilla', 'Crowley', 'Custer', 'Delta', 'Denver', 'Dolores', 'Douglas', 'Eagle', 'El Paso', 'Elbert', 'Fremont', 'Garfield', 'Gilpin', 'Grand', 'Gunnison', 'Hinsdale', 'Huerfano', 'Jackson', 'Jefferson', 'Kiowa', 'Kit Carson', 'La Plata', 'Lake', 'Larimer', 'Las Animas', 'Lincoln', 'Logan', 'Mesa', 'Mineral', 'Moffat', 'Montezuma', 'Montrose', 'Morgan', 'Otero', 'Ouray', 'Park', 'Phillips', 'Pitkin', 'Prowers', 'Pueblo', 'Rio Blanco', 'Rio Grande', 'Routt', 'Saguache', 'San Juan', 'San Miguel', 'Sedgwick', 'Summit', 'Teller', 'Washington', 'Weld', 'Yuma'],
    'nh': ['Belknap County','Carroll County','Cheshire County','Coos County','Grafton County','Hillsborough County','Merrimack County','Rockingham County','Strafford County','Sullivan County'],
    'ny': ['Albany County','Allegany County','Bronx County','Broome County','Cattaraugus County','Cayuga County','Chautauqua County','Chemung County','Chenango County','Clinton County','Columbia County','Cortland County','Delaware County','Dutchess County','Erie County','Essex County','Franklin County','Fulton County','Genesee County','Greene County','Hamilton County','Herkimer County','Jefferson County','Kings County','Lewis County','Livingston County','Madison County','Monroe County','Montgomery County','Nassau County','New York County','Niagara County','Oneida County','Onondaga County','Ontario County','Orange County','Orleans County','Oswego County','Otsego County','Putnam County','Queens County','Rensselaer County','Richmond County','Rockland County','Saint Lawrence County','Saratoga County','Schenectady County','Schoharie County','Schuyler County','Seneca County','Steuben County','Suffolk County','Sullivan County','Tioga County','Tompkins County','Ulster County','Warren County','Washington County','Wayne County','Westchester County','Wyoming County','Yates County'],
}

def load_counties(state):
    infile = f'../demographics/{state}_race.txt'
    if state in ('nh', 'ny'):
        counties = {county: {} for county in COUNTIES[state]}
        RACES = ['TOTAL', 'CAUCASIAN', 'AFRICAN_AMERICAN', 'NATIVE_AMERICAN', 'ASIAN', 'PACIFIC_ISLANDER', 'OTHER']
        with open(infile, 'r', newline='') as f:
            reader = csv.DictReader(f, delimiter='\t')
            for row, race in zip(reader, RACES):
                if race == 'PACIFIC_ISLANDER':
                    continue
                for county in counties:
                    counties[county][race] = int(row[county].replace(',',''))
        return counties
    else:
        counties = defaultdict(dict)
        RACES = ['NATIVE_AMERICAN', 'ASIAN', 'AFRICAN_AMERICAN', 'OTHER', 'CAUCASIAN']
        with open(infile, 'r', newline='') as f:
            reader = csv.DictReader(f, delimiter='\t')
            for row, race in zip(reader, cycle(RACES)):
                county = row['COUNTY']
                counties[county][race] = float(row['COUNT'].replace(',',''))
        for county in counties:
            counties[county]['TOTAL'] = sum(counties[county].values())

        return counties


def county_from_code(code, state=STATE):
    return COUNTIES[state][int(code)//2]

def main():
    precs_json = load_geojson(PRECINCTS_FILE)
    precincts = precs_json['features']
    counties = load_counties(STATE)
    print(counties)
    for precinct in precincts:
        properties = precinct['properties']
        county_code = properties['county_code']
        prec_pop = properties['population']
        county_races = counties[county_from_code(county_code)]
        county_pop = county_races['TOTAL']
        prec_races = {race: round(n*prec_pop/county_pop) for race, n in county_races.items() if race != 'TOTAL'}
        properties['demographics'] = {race: prec_races[race] for race in sorted(prec_races)}
        # print(properties['geo_id'], prec_races)

    with open(PRECINCTS_FILE_RACE, 'w') as f:
        json.dump(precs_json, f)



if __name__ == '__main__':
    main()
