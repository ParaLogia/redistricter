import requests
import json
from collections import defaultdict

session = requests.Session()


startdata = \
{
    "abbreviation": "nh",
    "weights": {
        "POLSBY_POPPER": 0.5,
        "POPULATION_FAIRNESS": 0.5
    },
    "algorithm": "SIMULATED_ANNEALING",
    "districts": 2,
    "variation": "GREEDY_ACCEPT",
    "seed": 1234,
    "year": 2000,
}


def select(state="NY"):
    data = {"state": state}
    response = session.post('http://127.0.0.1:8080/select', data=data)
    print(response.text)


def start(data=startdata):
    response = session.post('http://127.0.0.1:8080/start', json=data)
    # print(response.text)
    return json.loads(response.text)


def next():
    response = session.post('http://127.0.0.1:8080/next')
    # print(response.text)
    if not response.text.strip():
        return {}
    return json.loads(response.text)


dists = defaultdict(list)

def test():
    global dists
    start()
    n = next()
    objVal = 0.0
    i = 0
    while n:
        if i % 500 == 0 or n['objectiveDelta'] > 0.5:
            print(i)
            print('    ', n)
        i += 1
        objVal += n['objectiveDelta']
        dists[n['destinationDistrict']].append(n['precinct'])
        n = next()
    return objVal


def main():
    start()


if __name__ == "__main__":
    main()