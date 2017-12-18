/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Component, OnInit, ViewChild} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import "rxjs/add/operator/switchMap";
import {ProjectService} from "../../services/project.service";
import {GroupService} from "../../services/group.service";
import {Backer, CampaignPhoto, Group as GroupDomain, Project} from "../../services/domain";
import {SystemService} from "../../services/system.service";
import {LoggerFactory} from "../../components/logger/loggerFactory.component";
import {GroupMembers} from "./members/group_members.component";

@Component({
    selector: 'group',
    styleUrls: ['./group.scss'],
    templateUrl: './group.html'
})
export class Group implements OnInit{
    private logger = LoggerFactory.getLogger("components:group");
    id: number;
    my_group: boolean = true;
    group: GroupDomain;
    project: Project;
    photo: CampaignPhoto;
    leader: Backer;
    backers: Backer[];
    @ViewChild(GroupMembers) members: GroupMembers;


    constructor(private route: ActivatedRoute, private groupService: GroupService, private projectService: ProjectService,
                private system: SystemService) {
    }

    async ngOnInit() {
        this.id = this.route.snapshot.params['id'];
        this.logger.info("Searching for group: {0}", this.id);
        let info = await this.groupService.getGroupInfo(this.id);
        this.group = info.group;
        this.project = info.project;
        this.photo = info.photo;
        this.leader = info.leader;
        this.backers = info.backers;
        this.backers.unshift(info.leader);
        this.logger.info("System Backer ID: {0}, Leader ID: {1}", this.system.getId(), info.leader.id);
        this.my_group = this.system.getId() == this.leader.id;
    }
}
