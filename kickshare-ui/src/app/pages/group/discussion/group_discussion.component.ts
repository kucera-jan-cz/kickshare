/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import "rxjs/add/operator/switchMap";
import {GroupService} from "../../../services/group.service";
import {Post} from "../../../services/domain";

@Component({
    selector: 'group_discussion',
    styleUrls: ['./group_discussion.scss'],
    templateUrl: './group_discussion.html'
})
export class GroupDiscussion {
    posts: Post[] = [
        new Post(1, 1, 1, new Date(), new Date(), 0, "Lorem ipsum dolor sit amet"),
        new Post(2, 1, 1, new Date(), new Date(), 0, "Lorem ipsum dolor sit amet")
    ];

    constructor(private route: ActivatedRoute, private groupService: GroupService) {
    }


}
