/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Component, OnInit} from "@angular/core";
import {LoggerFactory} from "../../components/logger/loggerFactory.component";
import {GroupService} from "../../services/group.service";
import {UserService} from "../../services/user.service";
import {SystemService} from "../../services/system.service";
import {Group} from "../../services/domain";
import {UrlService} from "../../services/url.service";

@Component({
    selector: 'profile',
    templateUrl: './profile.html',
    styleUrls: ['./profile.scss']
})
export class ProfileComponent implements OnInit {
    private logger = LoggerFactory.getLogger("components:profile");
    id: number;
    groups: Group[];

    constructor(private groupService: GroupService, private userService: UserService, private systemService: SystemService, public url: UrlService) {
    }

    async ngOnInit() {
        this.id = this.systemService.getId();
        this.logger.debug("Searching for backer groups");
        this.groups = await this.userService.getUserGroups(this.id);
        this.logger.debug("Groups: " + JSON.stringify(this.groups));
    }

}
