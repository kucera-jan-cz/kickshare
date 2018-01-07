/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Injectable} from "@angular/core";
import "rxjs/Rx";
import {ProjectInfo} from "./domain";
import {AuthHttp} from "./auth-http.service";
import {LoggerFactory} from "../components/logger/loggerFactory.component";
import {HttpParams} from "@angular/common/http";

@Injectable()
export class KickstarterService {
    private logger = LoggerFactory.getLogger('services:auth:kickstarter');

    constructor(private http: AuthHttp) {
    }

    public searchProjects(categoryId: number, name: string): Promise<ProjectInfo[]> {
        this.logger.debug("Searching for Kickstarter project: " + name);
        var params = new HttpParams()
            .set("name", name)
            .set("category_id", String(categoryId))
            .set("store", "true");

        const promise: Promise<ProjectInfo[]> = this.http.get('/kickstarter/search', params);
        // promise.then(it => this.logger.info("Received projects: " + it.map(p => p.name).join(",")));
        return promise;
    }

}
