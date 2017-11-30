/**
 * Created by KuceraJan on 9.4.2017.
 */

import {Injectable} from "@angular/core";
import {AuthHttp} from "./auth-http.service";
import {City} from "./domain";
import {stringify} from "query-string";

@Injectable()
export class CityService {

    constructor(private http: AuthHttp) {
    }

    public getUsersCities(userId: number): Promise<City[]> {
        const promise: Promise<City[]> = this.http.get("/users/" + userId + "/cities");
        return promise;
    }

    public getCities(cityName: string): Promise<City[]> {
        const params = stringify({name: cityName});
        return this.http.get<City[]>("cities/search", params);
    }

}

