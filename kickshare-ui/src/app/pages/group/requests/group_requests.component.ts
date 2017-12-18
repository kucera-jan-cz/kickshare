/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Component, Input} from "@angular/core";
import "rxjs/add/operator/switchMap";
import {Backer} from "../../../services/domain";
import {GroupService} from "../../../services/group.service";
import {LoggerFactory} from "../../../components/logger/loggerFactory.component";
import remove from "../../../utils/util";

@Component({
    selector: 'group_requests',
    templateUrl: './group_requests.html'
})
export class GroupRequests {
    private logger = LoggerFactory.getLogger("components:group:requests");
    @Input() groupId: number;
    @Input() backers: Backer[];

    constructor(private groupSerive: GroupService) {

    }

    public async approve(backer: Backer) {
        this.logger.info("Approving group request for backer {0}", backer.id);
        const promise = await this.groupSerive.acceptBacker(this.groupId, backer.id);
        remove(this.backers, backer);
    }

    public async decline(backer: Backer) {
        this.logger.info("Rejecting group request for backer {0}", backer.id);
        const promise = await this.groupSerive.rejectBacker(this.groupId, backer.id);
        remove(this.backers, backer);
    }
}
