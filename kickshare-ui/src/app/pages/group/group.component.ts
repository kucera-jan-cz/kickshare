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

@Component({
    selector: 'group',
    styleUrls: ['./group.scss'],
    templateUrl: './group.html'
})
export class Group implements OnInit{
    id: number;
    my_id: number;
    my_group: boolean = true;
    group: GroupDomain;
    project: Project;
    photo: CampaignPhoto;
    leader: Backer;
    backers: Backer[];

    constructor(private route: ActivatedRoute, private groupService: GroupService, private projectService: ProjectService,
                private system: SystemService) {
    }

    async ngOnInit() {
        this.my_id = 2;
        this.id = this.route.snapshot.params['id'];
        console.info("Searching for group: " + this.id);
        let info = await this.groupService.getGroupInfo(this.id);
        this.group = info.group;
        this.project = info.project;
        this.photo = info.photo;
        this.leader = info.leader;
        this.backers = info.backers;
        this.backers.unshift(info.leader);
        console.info("System Backer ID: " + this.system.getId());
        this.my_group = this.system.getId() == this.leader.id;
        // this.my_group = this.my_id == info.leader.id;
    }
}
