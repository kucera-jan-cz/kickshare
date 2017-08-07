/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Injectable} from "@angular/core";
import {AuthHttp} from "./auth-http.service";
import {Group, GroupInfo, SearchOptions} from "./domain";
import {stringify} from "query-string";

@Injectable()
export class GroupService {
    constructor(private http: AuthHttp) {

    }

    public getGroupInfosByProject(projectId: number): Promise<Group[]> {
        console.info("Calling getGroupInfosByProject");
        const path = "/projects/" + projectId + "/groups";
        return this.http.get(path).then(
            res => {
                console.info("Groups info: " + JSON.stringify(res));
                return res.json() as Group[];
            }
        );
    }

    public getGroupInfo(groupId: number): Promise<GroupInfo> {
        console.info("Calling getGroupInfo");
        const path = "/groups/" + groupId;
        return this.http.get(path).then(
            res => {
                console.info("GroupDiscussion info: " + JSON.stringify(res));
                return res.json() as GroupInfo;
            }
        );
    }

    public createGroup(group: Group): Promise<Group> {
        return this.http.post("/groups", group)
            .then(
                res => {
                    console.info("GroupDiscussion created: " + JSON.stringify(res));
                    return res.json() as Group
                }
            );
    }

    public searchGroups(options: SearchOptions): Promise<GroupInfo[]> {
        const params = stringify(options);
        console.info("Searching groups with: " + params);
        return this.http.get("/groups/search?" + params)
            .then(
                res => {
                    console.info("Search group result: " + JSON.stringify(res));
                    return res.json() as GroupInfo[];
                }
            )
    }
}

