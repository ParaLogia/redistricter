import requests
import json

session = requests.Session()

width = 10
ndists = 2

startdata = \
{
    "state": f"MOCK {width} {ndists}",
    "weights": {
        "POLSBY_POPPER": 1.0,
        "POPULATION_FAIRNESS": 0.0
    },
    "algorithm": "REGION_GROWING",
    "variation": "PROBABILISTIC_ACCEPT",
    "seed": 123456
}


def select(state="NY"):
    data = {"state": state}
    response = session.post('http://127.0.0.1:8080/select', data=data)
    print(response.text)


def start(data=startdata):
    response = session.post('http://127.0.0.1:8080/start', json=data)
    print(response.text)
    return json.loads(response.text)


def next():
    response = session.post('http://127.0.0.1:8080/next')
    print(response.text)
    if not response.text.strip():
        return {}
    return json.loads(response.text)


def full_test():
    dists = start()
    i = 0
    while True:
        move = next()
        if not move:
            break
        i += 1
        if i % 100 == 0:
            print(f'move {i}')
        dstdist = move['destinationDistrict']
        srcdist = move['sourceDistrict']
        dists[int(dstdist)-1]['precincts'].append(move['precinct'])
        if srcdist > 0:
            dists[int(srcdist)-1]['precincts'].remove(move['precinct'])
#    print(dists)
    print(f'{i} moves')
    pds = {}
    for dist in dists:
        for p in dist['precincts']:
            pds[p] = dist['id']
    chars = ['$', '.', '/', ',', '#', "'", 'X','"'] + list('ABCDEFGHIJKLMNOPQRSTUVWXYZ'.lower())
    for y in range(width):
        for x in range(width):
            p = y*width + x
            print(chars[pds[p]-1], end='')
        print('')


def main():
    full_test()


if __name__ == "__main__":
    main()