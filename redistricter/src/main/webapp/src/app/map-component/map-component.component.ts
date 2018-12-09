import { Component, OnInit } from "@angular/core";

import { StateService, DisplayState } from "../state.service";
import { AdminService } from "../admin.service";
import { states } from "../models/states";
import { statesData } from "../models/state-geo";

import * as L from "leaflet";
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

  // List of algorithms
  public algorithms: string[] = ["Simulated Annealing", "Region Growing"];

  public statesDDL: DisplayState[];

  public theForm: FormGroup;

  public selectedAlgorithm: string;

  public lockObjectives: boolean;

  public algorithmInProgress: boolean;

  public randomSeed: number;

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
    this.mymap = L.map("mapid").setView([37.8, -96], 4);
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

    this.geojson = L.geoJSON(this.statesData, {
      // onEachFeature: function(feature, layer) {
      //   console.log("Hello");
      //   layer.on({
      //     mouseover: function(){
      //       console.log("hi");
      //     }
      //   })
      //       },
      style: {
        weight: 2,
        fillColor: "#E6E6FA",
        color: "#673AB7",
        fillOpacity: 0.7
      }
    }).addTo(this.mymap);
  }

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
    // this.http.post(environment.apiBaseUrl + "/main/start", this.stateService.state).toPromise()
    //   .then( (res) => {
    //     // Algo return set of districts
    //     this.initializeDistricts(res);
    //   },
    //   // If an error occurs, log it
    //   (err) => {
    //     console.log("error: " + err);
    //   }) 
  }

  public pauseAlgorithm() {
    this.algorithmInProgress = false;
    this.lockObjectives = false;
  }

  public generateRandomSeed() {}
  
  public initializeDistricts(districtData: any) {}

  public changeStateGeo(e) {
    this.enablePrecinctToggle = true;

    console.log(e);
    let stateIndex = 0;
    for (let i = 0; i < this.statesDDL.length; i++) {
      if (this.statesDDL[i].name === this.stateService.state.name) {
        stateIndex = i;
      }
    }

    // Process coordinates and zoom in on selected state
    let coords = this.processCoordinates(stateIndex);
    let polyline = L.polyline(coords, { color: "#E6E6FA" }).addTo(this.mymap);
    this.mymap.fitBounds(polyline.getBounds());

    let json = this.stateService.getDistrictGeo(this.stateService.state.name);
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
    ///

    this.stateService.state.population = 1342795;
    this.stateService.state.votes = 253062;
    this.stateService.state.objFunction = 0.96;
    this.stateService.state.senatorsStr = "Maggie Hassan, Jeanne Shaheen";
    this.stateService.state.numPrecincts = this.statesData.features[stateIndex].geometry.coordinates[0].length;
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
          layer.on({
            mouseover: function() {}
          });
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
    this.stateService.selectedPrecinct.party = 'Democrat';
    this.stateService.selectedPrecinct.population= 101,947;

    this.enablePrecinctToggle = false;
  }
  
  

  public onEachFeature(feature, layer) {
    console.log("Hello");
    // does this feature have a property named popupContent?
    if (feature.properties && feature.properties.popupContent) {
      layer.bindPopup(feature.properties.popupContent);
    }
  }

  //   public highlightFeature(e) {
  //     var layer = e.target;

  //     layer.setStyle({
  //       weight: 5,
  //       color: '#666',
  //       dashArray: '',
  //       fillOpacity: 0.7
  //   });

  //       layer.bringToFront();

  //   }

  //   public resetHighlight(e) {
  //     this.geojson.resetStyle(e.target);
  // }

  // public zoomToFeature(e) {
  //   this.mymap.fitBounds(e.target.getBounds());
  // }

  // public onEachFeature(feature, layer) {
  //   layer.on({
  //       mouseover: this.highlightFeature,
  //       mouseout: this.resetHighlight,
  //       click: this.zoomToFeature
  //   });
  // }
}
