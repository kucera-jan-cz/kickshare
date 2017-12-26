/**
 * Created by KuceraJan on 25.7.2017.
 */

import {Injectable} from "@angular/core";
import {AuthHttp} from "./auth-http.service";
import {Group, Notification} from "./domain";

@Injectable()
export class UserService {
    constructor(private http: AuthHttp) {

    }

    public getLatestNotifications(backer_id: number): Promise<Notification[]> {
        const path = "/users/" + backer_id + "/notifications/latest";
        return this.http.getResponse(path).then(
            res => {
                return res.json() as Notification[];
            }
        )
    }

    public getUserGroups(backerId: number): Promise<Group[]> {
        const path = `/users/${backerId}/groups`;
        const groups: Promise<Group[]> = this.http.get(path);
        return groups;
    }
}

