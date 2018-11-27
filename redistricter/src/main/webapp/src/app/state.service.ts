import { Injectable } from '@angular/core';

declare var require: any;

@Injectable({
  providedIn: 'root'
})
export class StateService {

  public state: State;
  public selectedPrecinct: Precinct;

  constructor() { 
    this.state = new State();
    this.selectedPrecinct = new Precinct();
  }

  /** Get the district geo json info  */
  public getDistrictGeo(stateName: String) {
    
    switch(stateName) {
      case 'Colorado': {
        return require('../assets/districts/co_dists_geo.json');
      }
      case 'New Hampshire': {
        return require('../assets/districts/nh_dists_geo.json');
      }
      case 'New York': {
        return require('../assets/districts/ny_dists_geo.json');
      }
      default : {
        return null;
      }
    }
  }

   /** Get the precinct geo json info  */
   public getPrecinctGeo(stateName: String) {
    
    switch(stateName) {
      case 'Colorado': {
        return require('../assets/precincts/co_geo.json');
      }
      case 'New Hampshire': {
        return require('../assets/precincts/nh_geo.json');
      }
      case 'New York': {
        return require('../assets/precincts/ny_geo.json');
      }
      default : {
        return null;
      }
      
    }
  }

  public populatePrecinct(){
    console.log("I here");
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

