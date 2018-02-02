/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Component, Input, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import "rxjs/add/operator/switchMap";
import {GroupService} from "../../../services/group.service";
import {MessagePost} from "../../../services/domain";
import {GroupMetadata} from "../components/group.metadata";
import {toMap} from "../../../utils/util";
import {LoggerFactory} from "../../../components/logger/loggerFactory.component";

@Component({
    selector: 'group_discussion',
    styleUrls: ['./group_discussion.scss'],
    templateUrl: './group_discussion.html'
})
export class GroupDiscussion implements OnInit {
    private logger = LoggerFactory.getLogger("components:group:discussion");

    @Input() meta: GroupMetadata;
    message: string;
    posts: MessagePost[] = [
        // new Post(1, 1, 1, new Date(), new Date(), 0, "Lorem ipsum dolor sit amet"),
        // new Post(2, 1, 1, new Date(), new Date(), 0, "Lorem ipsum dolor sit amet")
    ];

    constructor(private route: ActivatedRoute, private groupService: GroupService) {

    }

    async ngOnInit() {
        //@TODO promise all?
        const page = await this.groupService.readPosts(this.meta.getId());
        // const ids = new Set(page.content.map(it => it.backerId));
        const members = await this.groupService.getGroupBackers(this.meta.getId());
        const namesById = toMap(members, it => it.id, it => it.name + " " + it.surname);
        // new Map(members.map((it) => [ it.id, it.name + " "+  it.surname ]));
        this.posts = page.content.map(it => JSON.parse(JSON.stringify(it)));
        this.posts.forEach(it => it.author = namesById.get(it.backerId));
        this.logger.info("Names by id: {0}", JSON.stringify(namesById));
        this.logger.info("Messages: {0}", JSON.stringify(this.posts));
    }

    setPage(page: number) {
        if (page < 1 || page > this.pager.totalPages) {
            return;
        }

        // get pager object from service
        this.pager = this.pagerService.getPager(this.allItems.length, page);

        // get current page of items
        this.pagedItems = this.allItems.slice(this.pager.startIndex, this.pager.endIndex + 1);
    }

    async postMessage(text: string) {
        const newPost = await this.groupService.createPost(this.meta.getId(), text);
        this.message = "";
        // this.posts.push(newPost);
    }
}
