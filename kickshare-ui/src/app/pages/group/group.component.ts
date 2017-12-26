/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import "rxjs/add/operator/switchMap";
import {ProjectService} from "../../services/project.service";
import {GroupService} from "../../services/group.service";
import {Backer, CampaignPhoto, Group as GroupDomain, Project} from "../../services/domain";
import {SystemService} from "../../services/system.service";
import {LoggerFactory} from "../../components/logger/loggerFactory.component";
import {GroupMetadata} from "./components/group.metadata";
import {UrlService} from "../../services/url.service";

@Component({
    selector: 'group',
    styleUrls: ['./group.scss'],
    templateUrl: './group.html'
})
export class GroupComponent implements OnInit {

    public initialized: boolean = false;
    private logger = LoggerFactory.getLogger("components:group");
    id: number;
    group: GroupDomain;
    project: Project;
    photo: CampaignPhoto;
    leader: Backer;
    // @ViewChild(GroupMembers) members: GroupMembers;
    meta: GroupMetadata;
    country: string;

    constructor(private route: ActivatedRoute, private groupService: GroupService, private projectService: ProjectService,
                private systemService: SystemService, public url: UrlService) {
    }

    async ngOnInit() {
        this.country = this.systemService.countryCode.toLowerCase();
        this.id = this.route.snapshot.params['id'];
        this.logger.info("Searching for group: {0}", this.id);
        //@TODO - parallel requests instead of waits
        let info = await this.groupService.getGroupInfo(this.id);
        this.meta = new GroupMetadata(info, this.systemService.getId(), this.groupService);
        this.group = info.group;
        this.project = info.project;
        this.photo = info.photo;
        this.leader = info.leader;
        this.logger.info("System Backer ID: {0}, Leader ID: {1}", this.systemService.getId(), info.leader.id);
        this.initialized = true;
    }
}
