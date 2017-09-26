import {Component, ElementRef, OnInit} from "@angular/core";

import "style-loader!./userRegistration.scss";
import {FormGroup} from "@angular/forms";
import {ProjectService} from "../../services/project.service";
import {ProjectInfo} from "../../services/domain";
import {SystemService} from "../../services/system.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

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
    public city: string;
    public street: string;
    public postalCode: string;

    constructor(element: ElementRef, private projectService: ProjectService,private systemService: SystemService, private modalService: NgbModal,
                private router: Router, private route: ActivatedRoute) {
        this.nameElement = element;
    }

    ngOnInit() {
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

    async submit() {
        console.info("Submitting user");
    }
}
