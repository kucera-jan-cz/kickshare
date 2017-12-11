import {Component} from "@angular/core";
import {GroupSummary, ProjectInfo, SearchOptions} from "../../services/domain";
import {SearchMetadata} from "./searchMetadata.component";
import {GroupService} from "../../services/group.service";
import {ProjectService} from "../../services/project.service";
import {LoggerFactory} from "../../components/logger/loggerFactory.component";

@Component({
    selector: 'dashboard',
    styleUrls: ['./dashboard.scss'],
    templateUrl: './dashboard.html'
})
export class Dashboard {
    private logger = LoggerFactory.getLogger("components:dashboard");
    projects: ProjectInfo[] = [];
    groups: GroupSummary[] = [];
    displayProjects: boolean = true;
    searchMeta: SearchMetadata = new SearchMetadata();


    constructor(private groupService: GroupService, private projectService: ProjectService) {
    }

    projectsUpdated(projects: ProjectInfo[]): void {
        this.logger.info("Dashboard received event :: Projects with " + projects.length);
        this.projects = projects;
    }

    async mapUpdated(searchMeta: SearchMetadata) {
        this.logger.info("Search meta updated: {0}", JSON.stringify(searchMeta));

        this.searchMeta = searchMeta;

        const searchOptions = new SearchOptions();

        searchOptions.only_local = true;
        searchOptions.nw_lat = this.searchMeta.bounds.getNorthEast().lat();
        searchOptions.nw_lon = this.searchMeta.bounds.getSouthWest().lng();
        searchOptions.se_lat = this.searchMeta.bounds.getSouthWest().lat();
        searchOptions.se_lon = this.searchMeta.bounds.getNorthEast().lng();
        if (this.searchMeta.project) {
            this.logger.info("Searching for groups to list");
            //@TODO - make search groups with boundaries
            searchOptions.project_id = this.searchMeta.project.id;
            this.groups = await this.groupService.searchGroups(searchOptions);
            this.projects = [];
            this.displayProjects = false;
        } else {
            this.logger.info("Searching for projects to list");
            this.projects = await this.projectService.searchProjects(searchOptions);
            this.groups = [];
            this.displayProjects = true;
        }
    }
}
