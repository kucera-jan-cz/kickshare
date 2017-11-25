import {Component, ElementRef, OnInit} from "@angular/core";

import "style-loader!./groupRegistration.scss";
import {FormGroup} from "@angular/forms";
import {ProjectService} from "../../services/project.service";
import {City, Group, ProjectInfo} from "../../services/domain";
import {SystemService} from "../../services/system.service";
import {KickstarterService} from "../../services/kickstarter.service";
import {GroupService} from "../../services/group.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {CityService} from "../../services/city.service";

declare var $: any;

@Component({
    selector: 'group-registration',
    templateUrl: './groupRegistration.html',
})
export class GroupRegistration implements OnInit {
    private projectService: ProjectService;
    private systemService: SystemService;
    public form: FormGroup = new FormGroup({}); // our model driven form

    public name = "";
    public filteredList = [];
    public nameElement: ElementRef;

    public selected: ProjectInfo;
    public isLocal = false;
    public groupName = '';
    public groupLimit = 10;
    public country: string;
    public cities: City[];
    public selectedCity: City;

    constructor(element: ElementRef, projectService: ProjectService, systemService: SystemService,
                private kickstarter: KickstarterService, private groupService: GroupService, private cityService: CityService,
                private modalService: NgbModal, private router: Router, private route: ActivatedRoute) {
        this.nameElement = element;
        this.projectService = projectService;
        console.info("System service " + systemService);
        this.systemService = systemService;
    }

    async ngOnInit() {
        console.info("Loading country");
        this.country = this.systemService.countryCode;
        this.cities = await this.cityService.getUsersCities(this.systemService.getId());
        this.selectedCity = this.cities[0];
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
        if (text !== "" && text.length > 2) {
            if (event.which == 13) {
                this.filteredList = await this.kickstarter.searchProjects(text);
            } else {
                this.filteredList = await this.projectService.searchProjectsByName(text);
            }
            console.info("Received projects (" + this.filteredList.length + "): " + this.filteredList.map(p => p.name).join(","));
        } else {
            console.info("Require at least 3 characters");
            this.filteredList = [];
        }
    }

    saveGroup() {
        console.info("Group saved");
    }

    selectProjectByIndex(index: number) {
        console.info("Select index " + index);
        this.selectProject(this.filteredList[index]);
    }

    async selectProject(project: ProjectInfo) {
        console.info("Project selected :" + project.name);
        if (project != null) {
            console.info("Selected id: " + project.id);
            this.selected = project;
            this.renderGroupName();
            this.name = project.name;
        } else {
            console.warn("Item not selected");
        }
    }
    citySelected() {
        console.info("City selected");
        this.renderGroupName();
    }

    scopeChanged() {
        console.info("Scope changed");
        this.renderGroupName();
    }

    private async renderGroupName() {
        if (this.selected != null) {
            const cityId = this.isLocal ? this.selectedCity.id : -1;
            var groupName = await this.groupService.suggestName(this.selected.id, cityId);
            console.info("Suggested group name: " + groupName);
            this.groupName = groupName;
        } else {
            console.warn("Project is not selected skipping");
        }
    }

    /**
     * Open modal windows.
     * @param content
     */
    open(content) {
        this.modalService.open(content, {backdrop: 'static', size: 'lg'});
    }

    /**
     * Submit configuration of group
     * @returns {Promise<void>}
     */
    async submit() {
        console.info("Submitting group");
        var groupRequest: Group = {
            name: this.groupName,
            project_id: this.selected.id,
            //@TODO - inject here
            leader_id: -1,
            is_local: this.isLocal,
            limit: this.groupLimit
        };
        let group = await this.groupService.createGroup(groupRequest);
        console.info("Redirecting to group detail");
        this.router.navigate(['../../../group', group.id], {relativeTo: this.route});
    }
}
