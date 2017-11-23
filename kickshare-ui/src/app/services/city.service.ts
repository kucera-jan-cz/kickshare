/**
 * Created by KuceraJan on 9.4.2017.
 */

import {Injectable} from "@angular/core";
import {AuthHttp} from "./auth-http.service";
import {City} from "./domain";
import {stringify} from "query-string";
import {LogService} from "./log.service";

@Injectable()
export class CityService {
    private log: LogService

    constructor(private http: AuthHttp) {
        this.log = new LogService();
    }

    public getUsersCities(userId: number): Promise<City[]> {
        const promise: Promise<City[]> = this.http.getJson("/users/" + userId + "/cities");
        return promise;
    }

    public getCities(cityName: string): Promise<City[]> {
        const params = stringify({name: cityName});
        return this.http.getJson<City[]>("cities/search", params);
    }

}

