/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Injectable} from "@angular/core";
import {AuthHttp} from "./auth-http.service";
import {Backer, Group, GroupInfo, GroupSummary, Pageable, Post, SearchOptions} from "./domain";
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
        const promise: Promise<Group[]> = this.http.get(path);
        promise.then(group => this.logger.info("Groups info: ", JSON.stringify(group)));
        return promise;
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
        promise.then(group => this.logger.info("GroupDiscussion created: " + JSON.stringify(group)));
        return promise;
    }

    public searchGroups(options: SearchOptions): Promise<GroupSummary[]> {
        const params = stringify(options);
        this.logger.info("Searching groups with: " + params);
        const promise: Promise<GroupSummary[]> = this.http.get("/groups/search?" + params);
        promise.then(group => this.logger.info("Search group result: ", JSON.stringify(group)));
        return promise;
    }

    public getGroupBackers(groupId: number): Promise<Backer[]> {
        this.logger.info("Calling getGroupBackers: {0}", groupId);
        const promise: Promise<Backer[]> = this.http.get(`/groups/${groupId}/users`);
        promise.then(it => this.logger.info("Search group users result: ", JSON.stringify(it)));
        return promise;
    }

    public acceptBacker(groupId: number, backerId: number): Promise<any> {
        const path = `/groups/${groupId}/users/${backerId}/approve`;
        const promise: Promise<any> = this.http.post(path, null);
        return promise;
    }

    public rejectBacker(groupId: number, backerId: number): Promise<any> {
        const path = `/groups/${groupId}/users/${backerId}/decline`;
        const promise: Promise<any> = this.http.post(path, null);
        return promise;
    }

    public getBackerRequests(groupId: number): Promise<Backer[]> {
        const path = `groups/${groupId}/users/requests`;
        const promise: Promise<Backer[]> = this.http.get(path, null);
        return promise;
    }

    public joinGroup(groupId: number, backerId: number): Promise<any> {
        const path = `/groups/${groupId}/users/${backerId}`;
        const promise: Promise<any> = this.http.post(path, null);
        return promise;
    }

    public leaveGroup(groupId: number, backerId: number): Promise<any> {
        const path = `/groups/${groupId}/users/${backerId}`;
        const promise: Promise<any> = this.http.delete(path, null);
        return promise;
    }

    public suggestName(projectId: number, cityId: number): Promise<string> {
        const params = stringify({projectId: projectId, cityId: cityId});
        const promise: Promise<string> = this.http.get<string>("/groups/suggest?" + params, null, "text");
        return promise;
    }

    public createPost(groupId: number, text: string): Promise<Post> {
        const post = new Post(-1, groupId, -1, new Date(), null, 0, text);
        const promise: Promise<Post> = this.http.post("/groups/" + groupId + "/posts", post);
        return promise;
    }

    public updatePost(groupId: number, post: Post): void {
        this.http.patch("/groups/" + groupId + "/posts", post);
    }

    public readPosts(groupId: number): Promise<Pageable<Post>> {
        const promise: Promise<Pageable<Post>> = this.http.get(`/groups/${groupId}/posts?size=10&page=0&seek=1`);
        return promise;
    }
}

