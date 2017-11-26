import {Component, ElementRef, OnInit} from "@angular/core";

import "style-loader!./userRegistration.scss";
import {FormGroup} from "@angular/forms";
import {ProjectService} from "../../services/project.service";
import {City, ProjectInfo} from "../../services/domain";
import {SystemService} from "../../services/system.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgbModal, NgbTypeaheadSelectItemEvent} from "@ng-bootstrap/ng-bootstrap";
import {Observable} from "rxjs/Observable";
import {CityService} from "../../services/city.service";
import {GeoFactory} from "../../components/google/geoFactory.component";
import {Location} from "../../components/google/location.component";

declare var $: any;

@Component({
    selector: 'user-registration',
    templateUrl: './userRegistration.html',
})
export class UserRegistration implements OnInit {
    public form: FormGroup = new FormGroup({}); // our model driven form
    public events: any[] = []; // use later to display form changes

    public filteredList = [];
    public nameElement: ElementRef;

    public selected: ProjectInfo;
    public isLocal = true;
    public groupName = '';
    public groupLimit = 10;
    //User
    public email: string;
    public password: string;
    public name: string;
    public surname: string;

    //Location
    public searchCountry: string;
    public country: string;
    public city: City;
    public location: Location;
    public street: string;
    public postalCode: string;

    public states = ['Alabama', 'Alaska', 'American Samoa', 'Arizona', 'Arkansas', 'California', 'Colorado',
        'Connecticut', 'Delaware', 'District Of Columbia', 'Federated States Of Micronesia', 'Florida', 'Georgia',
        'Guam', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana', 'Maine',
        'Marshall Islands', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi', 'Missouri', 'Montana',
        'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey', 'New Mexico', 'New York', 'North Carolina', 'North Dakota',
        'Northern Mariana Islands', 'Ohio', 'Oklahoma', 'Oregon', 'Palau', 'Pennsylvania', 'Puerto Rico', 'Rhode Island',
        'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont', 'Virgin Islands', 'Virginia',
        'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'];

    //Category
    public countries = ['Czech republic', 'Germany'];

    private geoFactory = new GeoFactory();

    constructor(element: ElementRef, private projectService: ProjectService, private systemService: SystemService, private modalService: NgbModal,
                private cityService: CityService,
                private router: Router, private route: ActivatedRoute) {
        this.nameElement = element;
    }

    async ngOnInit() {
        console.info("Loading country");
        this.country = this.systemService.countryCode;
    }

    async nameTyped(event: KeyboardEvent) {
        console.info("Event target: " + event.target.constructor.name);
        const input = <HTMLInputElement>(event.target);
        const text = input.value;
        //Skip arrow keys
        if (event.which >= 37 && event.which <= 40) {
            return;
        }
        console.log("Searching for " + text + " Key: " + event.which);
    }

    cityFormatter = (city: City) => city.name;

    searchCityFunction = (text: Observable<string>) => {
        const citiesObservable: Observable<City[]> = text
            .debounceTime(200)
            .distinctUntilChanged()
            .flatMap(term => Observable.fromPromise(this.cityService.getCities(term)))
        return citiesObservable;
    };

    streetFormatter = (location: Location) => location.address;

    searchStreetFunction = (text: Observable<string>) => {
        //@TODO - resolve country code!!
        const streetsObservable: Observable<Location[]> = text
            .debounceTime(250)
            .distinctUntilChanged()
            .flatMap(term => Observable.fromPromise(this.geoFactory.search('CZ', term)));
        return streetsObservable;
    };

    streetSelected(value : NgbTypeaheadSelectItemEvent) {
        console.info("Setting postal code: " + JSON.stringify(value.item));
        const selectedLocation: Location = value.item;
        this.postalCode = selectedLocation.postal;
    }

    async submit() {
        console.info("Submitting user");
    }
}
