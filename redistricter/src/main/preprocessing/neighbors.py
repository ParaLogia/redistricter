from shapely.geometry import shape as Shape, MultiLineString, mapping
from shapely.ops import cascaded_union
from shapely.strtree import STRtree
from collections import defaultdict
import json
from sys import setrecursionlimit

setrecursionlimit(2000)

STATE = 'co'
PRECINCTS_FILE = '../precincts/{}_geo.json'.format(STATE)
NEIGHBORS_FILE = '../neighbors/{}_neighbors.json'.format(STATE)
EXTERIOR_FILE = '../neighbors/{}_exterior.json'.format(STATE)
INTERIOR_FILE = '../neighbors/{}_interior.json'.format(STATE)


def loadshapes(infile):
    with open(infile, 'r') as f:
        features = json.load(f)['features']

    shapes = []
    for feature in features:
        shape = Shape(feature['geometry'])
        shape.pcode = (feature['properties']['geo_id'])
        shapes.append(shape)

    return shapes


def findneighbors(shapes):
    # Fast query tree
    strtree = STRtree(shapes)

    neighbors = defaultdict(list)
    explored = set()
    edges = 0
    for p1 in shapes:
        explored.add(p1.pcode)
        nearby = (p for p in strtree.query(p1) if p.pcode not in explored)

        for p2 in nearby:
            if p1.relate_pattern(p2, '****1****'):
                n1, n2 = p1.pcode, p2.pcode
                neighbors[n1].append(n2)
                neighbors[n2].append(n1)

                if edges % 1000 == 0:
                    print(edges, 'edges')
                edges += 1
    print('Total edges:', edges)

    for p in neighbors:
        neighbors[p] = sorted(neighbors[p])
    return neighbors


def duplicates(shapes):
    names = defaultdict(int)
    for s in shapes:
        names[s.pcode] += 1

    return [(pcode, n) for pcode, n in names.items() if n > 1]


def islands(shapes, neighbors):
    return [p for p in shapes if p.pcode not in neighbors]


def borders(shapes, neighbors):
    shaperefs = {shape.pcode: shape for shape in shapes}

    interior = []
    discovered = set()
    explored = set()
    parents = defaultdict(str)
    precinct_ct = 0

    def dfs(pcode):
        nonlocal precinct_ct, interior
        precinct_ct += 1
        print(f'{precinct_ct}. {pcode}')
        discovered.add(pcode)
        parent = parents[pcode]
        for ncode in neighbors[pcode]:
            if ncode not in explored and ncode != parent:
                precinct, neighbor = shaperefs[pcode], shaperefs[ncode]
                border = precinct.boundary.intersection(neighbor.boundary)
                interior.append(border)
                if ncode not in discovered:
                    parents[ncode] = pcode
                    dfs(ncode)
        explored.add(pcode)

    print('Beginning DFS')
    for p in shaperefs:
        if p not in explored:
            dfs(p)

    # TODO exterior
    exterior = []
    # print('computing exterior...')
    # totalborders = cascaded_union([shape.boundary for shape in shapes])
    # exterior = totalborders.difference(interior)
    return interior, exterior


def main():
    print('loading precincts...')
    shapes = loadshapes(PRECINCTS_FILE)
    print('loading neighbors...')
    with open(NEIGHBORS_FILE, 'r') as f:
        neighbors = json.load(f)
    print('loading complete.')

    interior, exterior = borders(shapes, neighbors)
    with open(INTERIOR_FILE, 'w') as f:
        bounds = [mapping(boundary) for boundary in interior]
        features = [{'type': 'Feature', 'geometry': bound} for bound in bounds]
        geojson = {'type': 'FeatureCollection', 'features': features}
        json.dump(geojson, f)


if __name__ == '__main__':
    main()
