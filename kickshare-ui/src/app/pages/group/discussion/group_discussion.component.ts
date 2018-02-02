/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import "rxjs/add/operator/switchMap";
import {GroupService} from "../../../services/group.service";
import {Backer, MessagePost, Page, Pageable, PageRequest, Post} from "../../../services/domain";
import {GroupMetadata} from "../components/group.metadata";
import {toMap} from "../../../utils/util";
import {LoggerFactory} from "../../../components/logger/loggerFactory.component";
import {Subscription} from "rxjs/Subscription";

@Component({
    selector: 'group_discussion',
    styleUrls: ['./group_discussion.scss'],
    templateUrl: './group_discussion.html'
})
export class GroupDiscussion implements OnInit, OnChanges, OnDestroy {

    private logger = LoggerFactory.getLogger("components:group:discussion");

    @Input() meta: GroupMetadata;
    private backerSubscription: Subscription;
    private namesById: Map<number, string>;
    message: string;
    page: Page;
    posts: MessagePost[] = [];


    constructor(private route: ActivatedRoute, private groupService: GroupService) {}

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['meta']) {
            this.meta = changes['meta'].currentValue;
        }
    }

    async ngOnInit() {
        this.getPosts(new PageRequest(0));
        this.backerSubscription = this.meta.backers().subscribe(
            (backers: Backer[]) => this.namesById = toMap(backers, it => it.id, it => it.name + " " + it.surname)
        );
    }

    ngOnDestroy(): void {
        this.backerSubscription.unsubscribe();
        //@TODO - destroy this subscription or freshing
    }

    setPage(selectedPage: number) {
        if (selectedPage < -1 || selectedPage > this.page.totalPages) {
            return;
        }
        this.getPosts(new PageRequest(selectedPage))
    }

    async postMessage(text: string) {
        await this.groupService.createPost(this.meta.getId(), text);
        this.message = "";
        //@TODO - verify this behavior
        this.getPosts(new PageRequest(0));
        // this.posts.push(newPost);
    }

    private async getPosts(requestedPage: PageRequest) {
        const page: Pageable<Post> = await this.groupService.readPosts(this.meta.getId(), requestedPage);
        const header: Page = page;
        this.logger.info("Page: {0}", JSON.stringify(header));
        const posts: MessagePost[] = page.content.map(it => JSON.parse(JSON.stringify(it)));
        posts.forEach(it => it.author = this.namesById.get(it.backerId));
        this.posts = posts;
        this.logger.debug("Messages: {0}", JSON.stringify(this.posts));
        this.page = page;
    }
}
