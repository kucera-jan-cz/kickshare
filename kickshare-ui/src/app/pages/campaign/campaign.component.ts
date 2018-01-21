/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import "rxjs/add/operator/switchMap";
import "rxjs/add/operator/toPromise";
import {Group, ProjectInfo} from "../../services/domain";
import {GroupService} from "../../services/group.service";
import {ProjectService} from "../../services/project.service";
import {LoggerFactory} from "../../components/logger/loggerFactory.component";
import {UrlService} from "../../services/url.service";

@Component({
    selector: 'campaign',
    templateUrl: './campaign.html',
    styleUrls: ['./campaign.scss']
})
export class Campaign implements OnInit {
    private logger = LoggerFactory.getLogger("components:campaign");
    id: number;
    name: string;
    project: ProjectInfo;
    groups: Group[];

    constructor(private route: ActivatedRoute, private groupService: GroupService, private projectService: ProjectService,
                public url: UrlService) {
    }

    ngOnInit() {
        this.id = this.route.snapshot.params['id'];
        this.init();
    }

    private async init() {
        this.logger.debug("Searching for groups");
        let projectPromise = this.projectService.getProject(this.id);
        //@TODO - group vs groupInfo
        this.groups = await this.groupService.getGroupInfosByProject(this.id);
        this.logger.debug("Groups: " + JSON.stringify(this.groups));
        let project = await projectPromise;
        this.logger.debug("CAMPAIGN RECEIVED: " + JSON.stringify(project));
        this.project = project;
        this.name = project.name;
    }
}
