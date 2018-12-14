import { Component, OnInit } from "@angular/core";

import { StateService, DisplayState } from "../state.service";
import { AdminService } from "../admin.service";
import { states } from "../models/states";
import { statesData } from "../models/state-geo";

import * as L from "leaflet";
import { environment } from '../../environments/environment';

// @ts-ignore
import _ from "leaflet-search";
import { FormGroup } from "@angular/forms";
import { latLng, tileLayer } from "leaflet";
import { Http } from "@angular/http";

declare var require: any;

@Component({
  selector: "app-map-component",
  templateUrl: "./map-component.component.html",
  styleUrls: ["./map-component.component.css"]
})
export class MapComponentComponent implements OnInit {
  options = {
    layers: [
      tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        attribution: "&copy; OpenStreetMap contributors"
      })
    ],
    zoom: 7,
    center: latLng([46.879966, -121.726909])
  };


  public statesDDL: DisplayState[];

  public theForm: FormGroup;

  public selectedAlgorithm: string;

  public lockObjectives: boolean;

  public algorithmInProgress: boolean;

  public randomSeed: number;
  public searchTerm: string;

  public statesData: any;

  public enablePrecinctToggle = false;
  public showPrecinctTable = false;

  public geojson: any;
  public mymap: any;

  public displayedColumns: string[] = ["position", "name"];

  constructor(
    private stateService: StateService,
    private adminService: AdminService,
    private http: Http
  ) {
    this.statesData = statesData;
  }

  ngOnInit() {
    // Add states to drop down
    this.statesDDL = states;

    // Initialize map
    var mapvar = (this.mymap = L.map("mapid").setView([37.8, -96], 4));
    var stateService = this.stateService;

    L.tileLayer(
      "https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}",
      {
        attribution:
          'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery � <a href="https://www.mapbox.com/">Mapbox</a>',
        maxZoom: 18,
        id: "mapbox.streets",
        accessToken:
          "pk.eyJ1IjoiYXdvamoiLCJhIjoiY2puOTJkMWQ4MWtxMzNwbGRlbzl2eTJ1MiJ9.ZmekMcW6cjkkUMcbigCelQ"
      }
    ).addTo(this.mymap);

    // var info = L.control();

    // info.onAdd = function(map) {
    //   this._div = L.DomUtil.create("div", "info"); // create a div with a class "info"
    //   this.update();
    //   return this._div;
    // };

    // // method that we will use to update the control based on feature properties passed
    // info.update = function(props) {
    //   this._div.innerHTML =
    //     "<h4>US Population Density</h4>" +
    //     (props
    //       ? "<b>" +
    //         props.name +
    //         "</b><br />" +
    //         props.density +
    //         " people / mi<sup>2</sup>"
    //       : "Hover over a state");
    // };

    // info.addTo(this.mymap);


    // let hey = L.Control.Search;
    // console.log(hey);

    // mapvar.addControl( new L.Control.Search({
    //   url: 'http://nominatim.openstreetmap.org/search?format=json&q={s}',
    //   jsonpParam: 'json_callback',
    //   propertyName: 'display_name',
    //   propertyLoc: ['lat', 'lon'],
    //   marker: L.circleMarker([0, 0],{radius:30}),
    //   autoCollapse: true,
    //   autoType: false,
    //   minLength: 2
    // }) );

    this.geojson = L.geoJSON(this.stateService.getAllStatesGeo(), {
      style: {
        weight: 2,
        fillColor: "#E6E6FA",
        color: "#673AB7",
        fillOpacity: 0.7
      },
      onEachFeature: function(feature, layer) {
        // When a state is clicked
        layer.on("click", function() {
          // Process the coordss and add them to map
          let newCoords = processCoord(feature.geometry.coordinates);
          let polyline = L.polyline(newCoords, {
            color: "#E6E6FA"
          }).addTo(mapvar);
          mapvar.fitBounds(polyline.getBounds());

          // Set districts if applicable
          let json = stateService.setState(feature.properties.name);
          console.log(json);
          if (json !== null) {
            L.geoJSON(json, {
              style: {
                weight: 2,
                color: "#512DA8",
                fillColor: "#E6E6FA",
                fillOpacity: 0.9
              }
            }).addTo(mapvar);
          }
        });

        function processCoord(coords) {
          let coords1 = [];
          for (var i = 0; i < coords[0].length; i++) {
            coords1.push([coords[0][i][1], coords[0][i][0]]);
          }

          return coords1;
        }
      }
    }).addTo(this.mymap);
  }

  /** Converts the number to percent format */
  public percentFormat(value: number | null) {
    if (!value) {
      return "0%";
    } else {
      return value + "%";
    }
  }

  public startAlgorithm() {
    this.algorithmInProgress = true;
    this.lockObjectives = true;


    let algorithmInfo = {
      'abbreviation' : states.find(st => st.name === this.stateService.state.name).abbreviation,
      'weights' : {
        'POPULATION_FAIRNESS' : this.adminService.selectedAlgorithm.populationFairness / 100,
        'POLSBY_POPPER' : this.adminService.selectedAlgorithm.polsbyPopper / 100,
        'EFFICIENCY_GAP' : this.adminService.selectedAlgorithm.efficencyGap / 100,
        'PROPORTIONALITY' : this.adminService.selectedAlgorithm.porportionality / 100
      },
      'algorithm' : this.adminService.selectedAlgorithm.name,
      'variation' : this.adminService.selectedAlgorithm.variation,
      'seed' : 12345
    };

    
    console.log(algorithmInfo);

    this.http.post(environment.apiBaseUrl + "/start", algorithmInfo).toPromise()
      .then( (res) => {
        // Algo return set of districts
        this.initializeDistricts(res);
      },
      // If an error occurs, log it
      (err) => {
        console.log("error: " + err);
      });
  }

  public pauseAlgorithm() {
    this.algorithmInProgress = false;
    this.lockObjectives = false;
  }

  public generateRandomSeed() {
    console.log("I HERE");
  }

  public initializeDistricts(districtData: any) {}

  public changeStateGeo(e) {
    this.enablePrecinctToggle = true;
    let stateIndex = 0;
    for (let i = 0; i < this.statesDDL.length; i++) {
      if (this.statesDDL[i].name === this.stateService.state.name) {
        stateIndex = i;
      }
    }

    // Process coordinates and zoom in on selected state
    let coords = this.processCoordinates(stateIndex);
    console.log(coords);
    let polyline = L.polyline(coords, { color: "#E6E6FA" }).addTo(this.mymap);
    this.mymap.fitBounds(polyline.getBounds());

    let json = this.stateService.setState(this.stateService.state.name);
    if (json !== null) {
      L.geoJSON(json, {
        style: {
          weight: 2,
          color: "#512DA8",
          fillColor: "#E6E6FA",
          fillOpacity: 0.9
        }
      }).addTo(this.mymap);
    }

    this.stateService.state.numPrecincts = this.statesData.features[
      stateIndex
    ].geometry.coordinates[0].length;
    
    this.stateService.state.abbreviation = states.find(st => st.name === this.stateService.state.name).abbreviation;
  }

  public processCoordinates(index: number): any[] {
    // Process coordinates
    let coords = [];
    for (
      var i = 0;
      i < this.statesData.features[index].geometry.coordinates[0].length;
      i++
    ) {
      coords.push([
        this.statesData.features[index].geometry.coordinates[0][i][1],
        this.statesData.features[index].geometry.coordinates[0][i][0]
      ]);
    }

    return coords;
  }

  public toggleDisplayPrecincts(): void {
    this.showPrecinctTable = true;
    let json = this.stateService.getPrecinctGeo(this.stateService.state.name);
    if (json !== null) {
      console.log(json);
      let layer = L.geoJSON(json, {
        onEachFeature: function(feature, layer) {
          layer.on("click", function() {});
        },
        style: {
          weight: 1,
          color: "#512DA8",
          fillColor: "#E6E6FA",
          fillOpacity: 0.9
        }
      }).addTo(this.mymap);
      // layer.bringToBack();
    }

    //   var start = new Date().getTime();
    //   var end = start;
    //   while(end < start + 3000) {
    //     end = new Date().getTime();
    //  }
    this.stateService.selectedPrecinct.id = 137;
    this.stateService.selectedPrecinct.districtId = 1;
    this.stateService.selectedPrecinct.party = "Democrat";

    this.enablePrecinctToggle = false;
  }

public search(): void {
  console.log("yes");
  let url = 'http://nominatim.openstreetmap.org/search?format=json&q=' + 'Brooklyn';
  this.http
      .get(url)
      .toPromise()
      .then(
        (res) => {
          // Populate state data from backend
         console.log( JSON.parse(res['_body'])[0]['lat'] );
         console.log( JSON.parse(res['_body'])[0]['lon'] );
        },
        // If an error occurs, log it
        err => {
          console.log('error: ' + err);
        }
      );
}
  
}
