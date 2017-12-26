/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Component, Input, OnDestroy, OnInit} from "@angular/core";
import "rxjs/add/operator/switchMap";
import {Backer} from "../../../services/domain";
import {LoggerFactory} from "../../../components/logger/loggerFactory.component";
import {GroupMetadata} from "../components/group.metadata";
import {Subscription} from "rxjs/Subscription";

@Component({
    selector: 'group_requests',
    templateUrl: './group_requests.html'
})
export class GroupRequests implements OnInit, OnDestroy {
    private logger = LoggerFactory.getLogger("components:group:requests");
    @Input() meta: GroupMetadata;
    requests: Backer[];
    private subscription: Subscription;

    ngOnInit() {
        this.subscription = this.meta.requests().subscribe(requests => this.requests = requests);
    }

    ngOnDestroy(): void {
        this.subscription.unsubscribe();
    }

    public approve(backer: Backer) {
        this.logger.info("Approving group request for backer {0}", backer.id);
        this.meta.accept(backer);
    }

    public async decline(backer: Backer) {
        this.logger.info("Rejecting group request for backer {0}", backer.id);
        this.meta.reject(backer);
    }
}
