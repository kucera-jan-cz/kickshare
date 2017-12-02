/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Injectable} from "@angular/core";
import {AuthHttp} from "./auth-http.service";
import {Group, GroupInfo, GroupSummary, Post, SearchOptions} from "./domain";
import {stringify} from "query-string";
import {LoggerFactory} from "../components/logger/loggerFactory.component";

@Injectable()
export class GroupService {
    private logger = LoggerFactory.getLogger('services:group');

    constructor(private http: AuthHttp) {

    }

    public getGroupInfosByProject(projectId: number): Promise<Group[]> {
        this.logger.info("Calling getGroupInfosByProject");
        const path = "/projects/" + projectId + "/groups";
        return this.http.getResponse(path).then(
            res => {
                this.logger.info("Groups info: " + JSON.stringify(res));
                return res.json() as Group[];
            }
        );
    }

    public getGroupInfo(groupId: number): Promise<GroupInfo> {
        this.logger.info("Calling getGroupInfo({0})", groupId);
        const path = "/groups/" + groupId;
        const promise: Promise<GroupInfo> = this.http.get(path);
        promise.then(group => this.logger.debug("GroupInfo: {0}", JSON.stringify(group)));
        return promise;
    }

    public createGroup(group: Group): Promise<Group> {
        const promise: Promise<Group> = this.http.post("/groups", group);
        promise.then(group => this.logger.info("GroupDiscussion created: " + JSON.stringify(group)))
        return promise;
    }

    public searchGroups(options: SearchOptions): Promise<GroupSummary[]> {
        const params = stringify(options);
        this.logger.info("Searching groups with: " + params);
        return this.http.getResponse("/groups/search?" + params)
            .then(
                res => {
                    this.logger.info("Search group result: " + JSON.stringify(res));
                    return res.json() as GroupSummary[];
                }
            )
    }

    public suggestName(projectId: number, cityId: number): Promise<string> {
        const params = stringify({projectId: projectId, cityId: cityId});
        return this.http.getResponse("/groups/suggest?" + params)
            .then(
                res => {
                    return res.text();
                }
            )
    }

    public createPost(groupId: number, text: string): Promise<Post> {
        const post = new Post(-1, groupId, -1, new Date(), null, 0, text);
        return this.http.postResponse("/groups/" + groupId + "/posts", post)
            .then(
                res => {
                    return res.json() as Post;
                }
            )
    }

    public updatePost(groupId: number, post: Post): void {
        this.http.patchResponse("/groups/" + groupId + "/posts", post);
    }

    public readPosts(groupId: number): Promise<Post[]> {
        return this.http.getResponse("/groups/" + groupId + "/posts")
            .then(
                res => {
                    return res.json() as Post[];
                }
            )
    }
}

