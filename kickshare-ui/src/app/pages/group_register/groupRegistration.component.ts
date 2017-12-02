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
import {LoggerFactory} from "../../components/logger/loggerFactory.component";

declare var $: any;

@Component({
    selector: 'group-registration',
    templateUrl: './groupRegistration.html',
})
export class GroupRegistration implements OnInit {
    private logger = LoggerFactory.getLogger('components:group:registration');
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
        this.logger.info("System service " + systemService);
        this.systemService = systemService;
    }

    async ngOnInit() {
        this.logger.info("Loading country");
        this.country = this.systemService.countryCode;
        this.cities = await this.cityService.getUsersCities(this.systemService.getId());
        this.selectedCity = this.cities[0];
    }

    async nameTyped(event: KeyboardEvent) {
        this.logger.info("Event target: " + event.target.constructor.name);
        const input = <HTMLInputElement>(event.target);
        const text = input.value;
        //Skip arrow keys
        if (event.which >= 37 && event.which <= 40) {
            return;
        }
        this.logger.trace("Searching for {0}, Key: {1}",  text, event.which);
        if (text !== "" && text.length > 2) {
            if (event.which == 13) {
                this.filteredList = await this.kickstarter.searchProjects(text);
            } else {
                this.filteredList = await this.projectService.searchProjectsByName(text);
            }
            this.logger.info("Received projects (" + this.filteredList.length + "): " + this.filteredList.map(p => p.name).join(","));
        } else {
            this.logger.info("Require at least 3 characters");
            this.filteredList = [];
        }
    }

    saveGroup() {
        this.logger.info("Group saved");
    }

    selectProjectByIndex(index: number) {
        this.logger.info("Select index " + index);
        this.selectProject(this.filteredList[index]);
    }

    async selectProject(project: ProjectInfo) {
        this.logger.info("Project selected :" + project.name);
        if (project != null) {
            this.logger.info("Selected id: " + project.id);
            this.selected = project;
            this.renderGroupName();
            this.name = project.name;
        } else {
            this.logger.warn("Item not selected");
        }
    }
    citySelected() {
        this.logger.info("City selected");
        this.renderGroupName();
    }

    scopeChanged() {
        this.logger.info("Scope changed");
        this.renderGroupName();
    }

    private async renderGroupName() {
        if (this.selected != null) {
            const cityId = this.isLocal ? this.selectedCity.id : -1;
            var groupName = await this.groupService.suggestName(this.selected.id, cityId);
            this.logger.info("Suggested group name: " + groupName);
            this.groupName = groupName;
        } else {
            this.logger.warn("Project is not selected skipping");
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
        this.logger.info("Submitting group");
        var groupRequest: Group = {
            name: this.groupName,
            project_id: this.selected.id,
            //@TODO - inject here
            leader_id: -1,
            is_local: this.isLocal,
            limit: this.groupLimit
        };
        let group = await this.groupService.createGroup(groupRequest);
        this.logger.info("Redirecting to group detail");
        this.router.navigate(['../../../group', group.id], {relativeTo: this.route});
    }
}
