import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { states } from "./models/states";
import { HttpClient, HttpParams } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';

import { environment } from '../environments/environment';

declare var require: any;

@Injectable({
  providedIn: 'root'
})
export class StateService {
  public state: State;
  public selectedPrecinct: Precinct;

  constructor(private http: HttpClient) {
    this.state = new State();
    this.selectedPrecinct = new Precinct();
  }

  public getAllStatesGeo() {
    return require('../assets/states.json');
  }

  /** Get the district geo json info  */
  public getDistrictGeo(stateName: String) {
    switch (stateName) {
      case 'Colorado': {
        return require('../assets/districts/co_dists_geo.json');
      }
      case 'New Hampshire': {
        return require('../assets/districts/nh_dists_geo.json');
      }
      case 'New York': {
        return require('../assets/districts/ny_dists_geo.json');
      }
      default: {
        return null;
      }
    }
  }

  /** Get the precinct geo json info  */
  public getPrecinctGeo(stateName: String) {
    switch (stateName) {
      case 'Colorado': {
        return require('../assets/precincts/co_geo.json');
      }
      case 'New Hampshire': {
        return require('../assets/precincts/nh_geo.json');
      }
      case 'New York': {
        return require('../assets/precincts/ny_geo.json');
      }
      default: {
        return null;
      }
    }
  }

  public populatePrecinct() {
    console.log('I here');
  }

  public setState(name: string): any {
    this.state.name = name;
    this.state.abbreviation = states.find(st => st.name === this.state.name).abbreviation;
    this.getStateInfo(name);
    return this.getDistrictGeo(this.state.name);
  }

  public getStateInfo(name: string) {
    console.log('called this');

    let httpHeaders = new HttpHeaders();
    httpHeaders.append('Access-Control-Allow-Origin', '*');

    let httpParams = new HttpParams();
    httpParams.append('state', 'NY');
    let stateParam = states.find(st => st.name === name).abbreviation;

    this.http
      .get('http://localhost:8080/select', {
        headers: httpHeaders,
        params: {'state' : stateParam}
      })
      .toPromise()
      .then(
        (res: Response) => {
          // Populate state data from backend
          this.state.population = res['population'] === undefined ? 0 : res['population'] ;
        },
        // If an error occurs, log it
        err => {
          console.log('error: ' + err);
        }
      );
  }
}

/**
 * Class associated with state object
 */
export class State {
  public name: string;

  public abbreviation: string;

  public population: number;

  public votes: number;

  public senators: Senator[];

  public senatorsStr: string;

  public objFunction: number;

  public numPrecincts: number;

  constructor() {
    this.senators = [];
  }
}

export class Senator {
  public name: string;

  public party: string;

  constructor(name: string) {
    this.name = name;
  }
}

export class DisplayState {
  public name: string;
  public abbreviation: string;
}

export class Precinct {
  public id: number;
  public party: string;
  public population: number;
  public districtId: number;
}
