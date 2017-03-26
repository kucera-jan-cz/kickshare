import {Injectable} from "@angular/core";
import {Http} from "@angular/http";

@Injectable()
export class GroupService {
    constructor(private http: Http) {}

    public async get(url:String) {
        try {
            const res = await this.http.get('').toPromise();
            return res.json();
        } catch (e) {
            throw new Error(`http GET ${url}`);
        }
    }
}
