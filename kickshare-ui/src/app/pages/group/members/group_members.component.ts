/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Component, Input, OnChanges, SimpleChanges} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import "rxjs/add/operator/switchMap";
import {Backer} from "../../../services/domain";
import {LoggerFactory} from "../../../components/logger/loggerFactory.component";
import {SystemService} from "../../../services/system.service";
import {GroupService} from "../../../services/group.service";
import remove from "../../../utils/util";

@Component({
    selector: 'group_members',
    templateUrl: './group_members.html'
})
export class GroupMembers implements OnChanges {
    private logger = LoggerFactory.getLogger("components:group:members");
    private viewerId: number;
    @Input() groupId: number;
    @Input() leader: Backer;
    @Input() backers: Backer[];

    constructor(private route: ActivatedRoute, private system:SystemService, private groupService:GroupService) {
        this.viewerId = system.getId()
    }
    ngOnChanges(changes: SimpleChanges): void {
        if(changes['leader']) {
            this.leader = changes['leader'].currentValue;
        }
        if(changes['backers']) {
            this.backers = changes['backers'].currentValue;
        }
        var leaderId = this.leader ? this.leader.id : null;
    }

    public deleteBacker(backer: Backer) {
        this.groupService.leaveGroup(this.groupId, backer.id);
        remove(this.backers, backer);
    }
}
