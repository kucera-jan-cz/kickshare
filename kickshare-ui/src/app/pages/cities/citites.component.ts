/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Component, ElementRef, OnDestroy, OnInit} from "@angular/core";
import "rxjs/add/operator/switchMap";
import "rxjs/add/operator/toPromise";
import {MapFactory} from "../../components/google/mapFactory.component";
import {ProjectService} from "../../services/project.service";
import {AuthHttp} from "../../services/auth-http.service";

// declare var initCityMap: any;

@Component({
    selector: 'cities',
    styleUrls: ['./cities.scss'],
    templateUrl: './cities.html'
})
export class Cities implements OnInit, OnDestroy {
    // url = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyA-NAJu2diDDRzMKz9jKTIj6HVXODMjXpk&callback=initCityMap';

    private factory = new MapFactory();
    private cityMap: google.maps.Map;

    constructor(private _elementRef: ElementRef, private projectService: ProjectService, private http: AuthHttp) {
        // GoogleMapsLoader.KEY = 'AIzaSyA-NAJu2diDDRzMKz9jKTIj6HVXODMjXpk';
    }

    ngOnInit(): void {
        var readDataCallback = this.readCities.bind(this);
        //49.3333	14.8833	4
        const center = new google.maps.LatLng(49.333,14.8833);
        this.cityMap = this.factory.createCityMap('city-map', center, readDataCallback);
        console.info("Map: " + this.cityMap == null);
        google.maps.event.addListenerOnce(this.cityMap, 'tilesloaded', () => this.readCities());
    }

    ngOnDestroy(): void {
        this.cityMap = null;
    }

    private readCities() {
        console.info("Map: " + this.cityMap == null);
        const bounds = this.cityMap.getBounds();
        console.info("City read data from bounds: " + JSON.stringify(bounds));
        //@TODO - rewrite to NW and SE option notation
        var sw_lat = `sw_lat=${bounds.getSouthWest().lat()}`;
        var sw_lon = `sw_lon=${bounds.getSouthWest().lng()}`;
        var ne_lon = `ne_lon=${bounds.getNorthEast().lng()}`;
        var ne_lat = `ne_lat=${bounds.getNorthEast().lat()}`;
        var script = document.createElement('script');
        var params = `callback=JSONP_CALLBACK&only_local=false&${sw_lat}&${sw_lon}&${ne_lat}&${ne_lon}`;
        const path = `cities/search/jsonp?${params}`;
        this.http.getJsonp(path, resp => this.readCoordinates(resp));
    }

    private readCoordinates(resp: Response) {
        console.info("Got response: " + JSON.stringify(resp));
        const response = resp.json();
        this.clearData();
        for (var i = 0; i < response['features'].length; i++) {
            const coords = response['features'][i].geometry.coordinates;
            const name = response['features'][i].properties.name;
            const latLng = new google.maps.LatLng(coords[1], coords[0]);
            new google.maps.Marker({
                position: latLng,
                map: this.cityMap,
                title: name
            });
        }
    }

    //@TODO - maybe put into MapFactory as helper function
    private clearData() {
        this.cityMap.data.forEach(function (feature) {
            this.cityMap.data.remove(feature);
        });
    }
}
