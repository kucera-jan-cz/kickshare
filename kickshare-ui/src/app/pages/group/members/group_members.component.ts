/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Component, Input, OnChanges, OnInit, SimpleChanges} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import "rxjs/add/operator/switchMap";
import {Backer} from "../../../services/domain";
import {LoggerFactory} from "../../../components/logger/loggerFactory.component";
import {SystemService} from "../../../services/system.service";
import {GroupService} from "../../../services/group.service";
import {GroupMetadata} from "../components/group.metadata";

@Component({
    selector: 'group_members',
    templateUrl: './group_members.html'
})
export class GroupMembers implements OnChanges, OnInit {
    private logger = LoggerFactory.getLogger("components:group:members");
    private viewerId: number;
    leader: Backer;
    @Input() meta: GroupMetadata;

    constructor(private route: ActivatedRoute, private system: SystemService, private groupService: GroupService) {
        this.viewerId = system.getId();

    }

    ngOnInit(): void {
        this.leader = this.meta.getLeader();
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['meta']) {
            this.meta = changes['meta'].currentValue;
        }
    }

    public async deleteBacker(backer: Backer) {
        this.meta.leave(backer);
        // await this.groupService.leaveGroup(this.meta.getId(), backer.id);
    }

    public async joinGroup() {
        this.logger.debug("Requesting membership for {0} into group {1}", this.viewerId, this.meta.getId());
        if(this.meta.isAnonymous()) {
            // this.router.navigate(['/cz/login'], {queryParams: {returnUrl: state.url}});
        } else {
            this.meta.join();
            // await this.groupService.joinGroup(this.meta.getId(), this.viewerId);
        }
    }
}
