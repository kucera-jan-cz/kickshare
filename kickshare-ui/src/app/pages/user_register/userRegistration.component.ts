import {Component, ElementRef, OnInit} from "@angular/core";

import "style-loader!./userRegistration.scss";
import {FormGroup} from "@angular/forms";
import {ProjectService} from "../../services/project.service";
import {City, Country, ProjectInfo} from "../../services/domain";
import {SystemService} from "../../services/system.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgbModal, NgbTypeaheadSelectItemEvent} from "@ng-bootstrap/ng-bootstrap";
import {Observable} from "rxjs/Observable";
import {CityService} from "../../services/city.service";
import {GeoFactory} from "../../components/google/geoFactory.component";
import {Location} from "../../components/google/location.component";
import {CountryConstants} from "../../constants/country.constants";

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
    public country: Country;
    public city: City;
    public location: Location;
    public street: string;
    public postalCode: string;

    //Category
    public countries:Country[] = CountryConstants.countries();

    private geoFactory = new GeoFactory();

    constructor(element: ElementRef, private projectService: ProjectService, private systemService: SystemService, private modalService: NgbModal,
                private cityService: CityService,
                private router: Router, private route: ActivatedRoute) {
        this.nameElement = element;
    }

    async ngOnInit() {
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
            .flatMap(term => Observable.fromPromise(this.cityService.getCities(term)));
        return citiesObservable;
    };

    streetFormatter = (location: Location) => location.display;

    searchStreetFunction = (text: Observable<string>) => {
        const streetsObservable: Observable<Location[]> = text
            .debounceTime(250)
            .distinctUntilChanged()
            .flatMap(term => Observable.fromPromise(this.geoFactory.search(this.country.code, term)));
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
