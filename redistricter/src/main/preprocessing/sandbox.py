# from shapely.geometry import shape, mapping
# from shapely.strtree import STRtree
# import json
#
# with open('../precincts/nh_geo.json', 'r') as f:
#     features = json.load(f)['features']
#     shapes = [shape(feature['geometry']) for feature in features]
#
# with open('out.json', 'w') as f:
#     features = [{'type': 'Feature', 'geometry': mapping(shape)} for shape in shapes]
#     geojson = {'type': 'FeatureCollection', 'features': features}
#     json.dump(geojson, f)


# strtree = STRtree(shapes)
#
# q = strtree.query(shapes[0])
# print([x in shapes for x in q])