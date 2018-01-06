/**
 * Created by KuceraJan on 26.3.2017.
 */
import {Component, EventEmitter, Input, OnDestroy, OnInit, Output, Sanitizer, SecurityContext} from "@angular/core";
import {Router} from "@angular/router";
import "style-loader!./gmap.scss";
import {CategoryService} from "../../../services/categories.service";
import {CustomData} from "./customdata.component";
import {ProjectService} from "../../../services/project.service";
import {CompleterItem} from "ng2-completer";
import {Category, JSONPSearchOptions, Project, ProjectInfo, SearchOptions} from "../../../services/domain";
import {SearchMetadata} from "../searchMetadata.component";
import {AuthHttp} from "../../../services/auth-http.service";
import {SystemService} from "../../../services/system.service";
import {LoggerFactory} from "../../../components/logger/loggerFactory.component";
import LatLngBounds = google.maps.LatLngBounds;
import MapTypeId = google.maps.MapTypeId;
import StyleOptions = google.maps.Data.StyleOptions;
// declare var initGroupGridMap: any;
declare var MarkerClusterer: any;

declare var getBounds: Function;

@Component({
    selector: 'gmap',
    templateUrl: './gmap.component.html',
    styleUrls: ['./gmap.scss'],
})
export class GMap implements OnInit, OnDestroy {
    private logger = LoggerFactory.getLogger("components:dashboard:gmap");
    loadAPI: Promise<any>;
    //@TODO externalize the key
    url = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyA-NAJu2diDDRzMKz9jKTIj6HVXODMjXpk&callback=initMap';
    index = 1;
    searchTerm: string;
    settings = {
        onlyNearBy: false,
        withTags: false
    };
    categories: Category[];
    selectedProject: Project;
    @Input() projects: Project[];
    @Output() dataChange: EventEmitter<ProjectInfo[]> = new EventEmitter();
    @Output() mapChange: EventEmitter<SearchMetadata> = new EventEmitter();
    @Output() optionsChange: EventEmitter<SearchOptions> = new EventEmitter();
    tags = [{value: 0, display: 'Action'}, {value: 1, display: 'Co-op'}];
    dataService: CustomData;
    groupGridMap: google.maps.Map;
    markerCluster: any;
    protected searchOptions: JSONPSearchOptions = new JSONPSearchOptions();

    constructor(private router: Router,
                private sanitizer: Sanitizer,
                private projectService: ProjectService,
                private categoryService: CategoryService,
                private system: SystemService,
                private http: AuthHttp) {
    }


    async ngOnInit() {
        this.categories = await this.categoryService.getCategories();
        this.initGroupGridMapTS();
        this.dataService = new CustomData(this.projectService);
    }

    ngOnDestroy() {
        this.logger.debug("Destroying map");
    }

    initGroupGridMapTS() {
        //@TODO - move this logic from mapFactory?
        const current_lat = this.system.current_lat;
        const current_lon = this.system.current_lon;

        const mapOptions: google.maps.MapOptions = {
            zoom: 8,
            center: new google.maps.LatLng(current_lat, current_lon),
            mapTypeId: MapTypeId.ROADMAP
        };
        this.groupGridMap = new google.maps.Map(document.getElementById('map'), mapOptions);

        var clusterStyles = [
            {
                textColor: 'black',
                url: '/assets/icon/gm_kickshare_cluster.png',
                height: 45,
                width: 35,
                textSize: 16
            },
            {
                textColor: 'black',
                url: '/assets/icon/gm_kickshare_cluster.png',
                height: 45,
                width: 35,
                textSize: 16
            },
            {
                textColor: 'black',
                url: '/assets/icon/gm_kickshare_cluster.png',
                height: 45,
                width: 35,
                textSize: 16
            }
        ];
        var mcOptions = {
            gridSize: 45,
            styles: clusterStyles,
            maxZoom: 15,
            imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m'
        };
        this.markerCluster = new MarkerClusterer(this.groupGridMap, null, mcOptions);

        this.groupGridMap.data.setStyle({
            icon: '/assets/icon/gm_kickshare.png',
            fillColor: 'green'
        });

        this.groupGridMap.data.setStyle(function (feature) {
            var type = feature.getProperty('type');
            return this.getIconStyle(type);
        });
        var readDataCallback = this.readDataFromSearch.bind(this);
        this.groupGridMap.addListener('dragend', readDataCallback);
        this.groupGridMap.addListener('zoom_changed', readDataCallback);


        var legend = document.getElementById('legend');
        var div = document.createElement('div');
        div.innerHTML = '<img src="' + '/assets/icon/gm_kickshare_local.png' + '"> ' + 'Local' + '</img>';
        legend.appendChild(div);

        var div = document.createElement('div');
        div.innerHTML = '<img src="' + '/assets/icon/gm_kickshare.png' + '"> ' + 'Global' + '</img>';
        legend.appendChild(div);

        this.groupGridMap.controls[google.maps.ControlPosition.RIGHT_TOP].push(legend);

        this.readData(new google.maps.LatLng(current_lat, current_lon));
    }

    //@TODO - soon remove this and rather define country default boundaries or define view based on current location
    private readData(point: google.maps.LatLng) {
        this.logger.info("Reading Google maps data focusing on point: " + JSON.stringify(point));
        var params = `callback=JSONP_CALLBACK&lat=${point.lat()}&lon=${point.lng()}`;
        const url = `groups/search/jsonp?${params}`;
        this.http.getJsonp(url, response => this.readCoordinates(response));
    }

    private readDataFromSearch(name?: string, onlyLocal?: boolean) {
        const bounds: LatLngBounds = this.groupGridMap.getBounds();
        this.logger.info("Reading data bounds!!!" + JSON.stringify(bounds));
        this.searchOptions.nw_lat = bounds.getNorthEast().lat();
        this.searchOptions.nw_lon = bounds.getSouthWest().lng();
        this.searchOptions.se_lat = bounds.getSouthWest().lat();
        this.searchOptions.se_lon = bounds.getNorthEast().lng();
        if (onlyLocal != null) {
            this.searchOptions.only_local = onlyLocal;
        }
        if (this.selectedProject != null) {
            this.searchOptions.project_id = this.selectedProject.id;
        }
        const params = this.searchOptions.toParams() + "&callback=JSONP_CALLBACK";
        this.logger.info("Searching with params: " + params);
        //@TODO - prefix is missing
        const url = `groups/search/data.jsonp?${params}`;
        this.http.getJsonp(url, response => this.readCoordinates(response));
    }

    getIconStyle(type): StyleOptions {
        this.logger.debug("Getting type: " + type);
        switch (type) {
            case "LOCAL" :
                return {
                    icon: '/assets/icon/gm_kickshare_local.png',
                    fillColor: 'green'
                };
            case "GLOBAL" :
                return {
                    icon: '/assets/icon/gm_kickshare.png',
                    fillColor: 'blue'
                };
            case "MIXED" :
                return {
                    icon: '/assets/icon/gm_kickshare_both.png',
                    fillColor: 'yellow'
                };
            default :
                return {
                    icon: '/assets/icon/gm_kickshare_local.png',
                    fillColor: 'green'
                };
        }
    }

    searchGroups() {
        const searchTerm = this.sanitizer.sanitize(SecurityContext.NONE, this.searchTerm);
        this.readDataFromSearch(searchTerm, false);
        this.logger.info("Searching for: {0}", searchTerm);
        const bounds: LatLngBounds = this.groupGridMap.getBounds();
        const searchMeta = new SearchMetadata();
        searchMeta.project = this.selectedProject;
        searchMeta.bounds = bounds;
        this.mapChange.next(searchMeta);
        this.optionsChange.next(this.searchOptions);
    }

    nameTyped(event: KeyboardEvent) {
        this.logger.trace("Event target: {0}", event.target.constructor.name);
        const input = <HTMLInputElement>(event.target);
        const text = input.value;
        //Skip arrow keys
        if (event.which >= 37 && event.which <= 40) {
            return;
        }
        this.logger.trace("Autocomplete: " + text + " Key: " + event.which);
        if (text !== "" && text.length > 2) {
            const searchTerm = this.sanitizer.sanitize(SecurityContext.NONE, text);
            this.dataService.search(searchTerm);
        } else {
            this.selectedProject = null;
            this.logger.info("Require at least 3 characters");
        }
    }

    itemSelected(event: CompleterItem) {
        if (event) {
            this.logger.debug("Item selected title: " + event.title);
            this.logger.debug("Item selected image: " + event.image);
            this.logger.debug("Item selected type: " + event.originalObject.constructor.name);
            this.selectedProject = event.originalObject;
        } else {
            this.logger.debug("Empty selected item??");
        }
    }

    private clearData() {
        this.groupGridMap.data.forEach(function (feature) {
            this.groupGridMap.data.remove(feature);
        });
        this.markerCluster.clearMarkers();
    }

    private readCoordinates(response: any) {
        this.clearData();
        for (var i = 0; i < response['features'].length; i++) {
            const coords = response['features'][i].geometry.coordinates;
            const type = response['features'][i].properties.type;
            const icon = this.getIconStyle(type).icon;
            const latLng = new google.maps.LatLng(coords[1], coords[0]);
            const marker = new google.maps.Marker({
                position: latLng,
                map: this.groupGridMap,
                icon: icon
            });
            this.markerCluster.addMarker(marker);
        }
    }

}
