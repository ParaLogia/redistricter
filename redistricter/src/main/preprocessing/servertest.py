# Quick and dirty script to hit the server endpoint
# We used Postman after we found out about that

import requests

startdata = \
{
    "state": "NY",
    "weights": {
        "POLSBY_POPPER": 0.3,
        "POPULATION_FAIRNESS": 0.7
    },
    "algorithm": "REGION_GROWING",
    "variation": "GREEDY_ACCEPT",
    "seed": 12356
}


def select(state="NY"):
    data = {"state": state}
    response = requests.post('http://127.0.0.1:8080/select', data=data)
    print(response.text)


def start(data=startdata):
    response = requests.post('http://127.0.0.1:8080/start', json=data)
    print(response.text)


def next():
    response = requests.post('http://127.0.0.1:8080/next')
    print(response.text)


def main():
    select()


if __name__ == "__main__":
    main()