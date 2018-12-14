import json;
import os;

filename = "../app/models/states.json"
with open(filename, 'r') as file:
    data = json.load(file);
    features = data['features']
    i = 0;
    for x in features: 
        for coord in x['geometry']['coordinates'][0]:
            coord = [coord[1],coord[0]];
            print(coord);

newfile = "../app/models/states.json"
with open(newfile, 'w') as file:
    json.dump(data, file, indent=4);