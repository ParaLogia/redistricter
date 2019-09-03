# Assigns votes to precincts by interpolating from district data

import csv
import json
import itertools as it
from collections import defaultdict

states = 'CO', 'NH', 'NY'


def filtercsv(two_party=False):
    fieldnames= 'year,state_po,district,party,candidatevotes,totalvotes'.split(',')
    truefieldnames= 'year,state,district,party,candidatevotes,totalvotes'.split(',')
    IN = '../votes/1976-2016-house.csv'
    OUT = '../votes/1998-2016-house.csv'

    with open(IN, 'r', newline='', encoding='utf8') as infile, open(OUT, 'w', newline='') as outfile:
        reader = csv.DictReader(infile, delimiter=',', quotechar='"')
        writer = csv.DictWriter(outfile, fieldnames=truefieldnames)
        writer.writeheader()
        for row in reader:
            if row['state_po'] in states and int(row['year']) in range(1998, 2017)\
                    and (row['party'] in ('republican', 'democrat') or not two_party):
                writer.writerow({field.split('_po')[0]: value
                                 for field, value in row.items() if field in fieldnames})


def sortfiltercsv():
    fieldnames= 'year,state,district,party,candidatevotes,totalvotes'.split(',')
    CSV = '../votes/1998-2016-house.csv'
    with open(CSV, 'r', newline='', encoding='utf8') as f:
        reader = csv.DictReader(f, delimiter=',', quotechar='"')
        rows = [row for row in reader]
    rows = sorted(rows, key=lambda r: (r['state'], int(r['year']), int(r['district']), -int(r['candidatevotes'])))
    for (state, year, district), subrows in it.groupby(rows, key=lambda r: (r['state'], r['year'], r['district'])):
        # print(state, district, year)
        subrows = list(subrows)
        if not any(row['party'].strip() == 'democrat' for row in subrows):
            print(f'Faking democrats in {state} district {district}, {year}')
            for row in subrows:
                if row['party'].strip() != 'republican':
                    row['party'] = 'democrat'
                    break

        if not any(row['party'].strip() == 'republican' for row in subrows):
            print(f'Faking republicans in {state} district {district}, {year}')
            for row in subrows:
                if row['party'].strip() != 'democrat':
                    row['party'] = 'republican'
                    break

    with open(CSV, 'w', newline='', encoding='utf8') as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        for row in rows:
            if row['party'] in ('democrat', 'republican'):
                writer.writerow(row)


def dumpjson():
    CSV = '../votes/1998-2016-house.csv'
    with open(CSV, 'r', newline='', encoding='utf8') as f:
        reader = csv.DictReader(f, delimiter=',', quotechar='"')
        rows = [row for row in reader]

    for state, staterows in it.groupby(rows, key=lambda r: r['state']):
        votes = defaultdict(dict)
        for year, yearrows in it.groupby(staterows, key=lambda r: r['year']):
            year = int(year)
            for district, districtrows in it.groupby(yearrows, key=lambda r: r['district']):
                district = int(district)
                votes[district][year] = {row['party'].upper(): int(row['candidatevotes']) for row in districtrows}

        outfile = '../votes/{}_dist_votes.json'.format(state.lower())
        with open(outfile, 'w') as f:
            json.dump(votes, f)


def estimatevotes():
    for state in (
        # 'nh',
        'co',
        'ny'
    ):
        precincts_in = '../precincts/{}_precincts_geo.json'.format(state)
        precincts_out = '../precincts/{}_precincts_geo.json'.format(state)
        votes_files = '../votes/{}_dist_votes.json'.format(state)
        with open(votes_files, 'r') as f:
            year_dist_votes = json.load(f)
        with open(precincts_in, 'r') as f:
            geojson = json.load(f)

        precincts = [feature['properties'] for feature in geojson['features']]

        # print(year_dist_votes)
        pdisttotals = {
            int(dist): {
                party: sum(p['votes'][party] for p in precincts if str(p['district']) == str(dist))
                    for party in ('REPUBLICAN', 'DEMOCRAT')
            } for dist in year_dist_votes
        }
        print(pdisttotals)
        for feature in geojson['features']:
            precinct = feature['properties']
            votes = defaultdict(dict)
            dist = precinct['district']
            for year, dist_votes in year_dist_votes[str(dist)].items():
                for party, party_votes in dist_votes.items():
                    votes[int(year)][party] = round(precinct['votes'][party]/pdisttotals[dist][party]*party_votes)
            precinct['votes'] = dict(votes)
            # print(precinct['votes'])

        with open(precincts_out, 'w') as f:
            json.dump(geojson, f)

def main():
    filtercsv()
    sortfiltercsv()
    dumpjson()
    estimatevotes()

if __name__ == '__main__':
    main()
