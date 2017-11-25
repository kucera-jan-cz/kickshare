/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Injectable} from "@angular/core";
import {AuthHttp} from "./auth-http.service";
import {Group, GroupInfo, GroupSummary, Post, SearchOptions} from "./domain";
import {stringify} from "query-string";

@Injectable()
export class GroupService {
    constructor(private http: AuthHttp) {

    }

    public getGroupInfosByProject(projectId: number): Promise<Group[]> {
        console.info("Calling getGroupInfosByProject");
        const path = "/projects/" + projectId + "/groups";
        return this.http.getResponse(path).then(
            res => {
                console.info("Groups info: " + JSON.stringify(res));
                return res.json() as Group[];
            }
        );
    }

    public getGroupInfo(groupId: number): Promise<GroupInfo> {
        console.info("Calling getGroupInfo");
        const path = "/groups/" + groupId;
        return this.http.getResponse(path).then(
            res => {
                console.info("GroupDiscussion info: " + JSON.stringify(res));
                return res.json() as GroupInfo;
            }
        );
    }

    public createGroup(group: Group): Promise<Group> {
        return this.http.postResponse("/groups", group)
            .then(
                res => {
                    console.info("GroupDiscussion created: " + JSON.stringify(res));
                    return res.json() as Group
                }
            );
    }

    public searchGroups(options: SearchOptions): Promise<GroupSummary[]> {
        const params = stringify(options);
        console.info("Searching groups with: " + params);
        return this.http.getResponse("/groups/search?" + params)
            .then(
                res => {
                    console.info("Search group result: " + JSON.stringify(res));
                    return res.json() as GroupSummary[];
                }
            )
    }

    public suggestName(projectId: number, cityId: number) : Promise<string> {
        const params = stringify({projectId: projectId, cityId : cityId});
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

