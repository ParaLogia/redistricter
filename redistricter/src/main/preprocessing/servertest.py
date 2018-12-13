import requests

session = requests.Session()

startdata = \
{
    "state": "NY",
    "weights": {
        "POLSBY_POPPER": 0.3,
        "POPULATION_FAIRNESS": 0.7
    },
    "algorithm": "SIMULATED_ANNEALING",
    "variation": "ANY_ACCEPT",
    "seed": 12356
}


def select(state="NY"):
    data = {"state": state}
    response = session.post('http://127.0.0.1:8080/select', data=data)
    print(response.text)


def start(data=startdata):
    response = session.post('http://127.0.0.1:8080/start', json=data)
    print(response.text)


def next():
    response = session.post('http://127.0.0.1:8080/next')
    print(response.text)


def main():
    start()
    next()


if __name__ == "__main__":
    main()