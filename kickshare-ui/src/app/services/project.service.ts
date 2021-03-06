/**
 * Created by KuceraJan on 9.4.2017.
 */

import {Injectable} from "@angular/core";
import {AuthHttp} from "./auth-http.service";
import {ProjectInfo, SearchOptions} from "./domain";
import {stringify} from "query-string";
import {LoggerFactory} from "../components/logger/loggerFactory.component";
import {HttpParams} from "@angular/common/http";

@Injectable()
export class ProjectService {
    private logger = LoggerFactory.getLogger('services:project');

    constructor(private http: AuthHttp) {
    }

    public searchProjects(options: SearchOptions): Promise<ProjectInfo[]> {
        const params = stringify(options);
        this.logger.info("Searching projects with: " + params);
        const promise: Promise<ProjectInfo[]> = this.http.get("/projects/search?" + params);
        promise.then(it => this.logger.info("Search project result: " + JSON.stringify(it)));
        return promise;
    }

    public searchProjectsByName(categoryId: number, name: string): Promise<ProjectInfo[]> {
        let params = new HttpParams().set("name", name).set("category_id", String(categoryId));
        const promise: Promise<ProjectInfo[]> = this.http.get("/projects?" + params);
        promise.then(it => this.logger.info("Received projects: " + it.map(p => p.name).join(",")));
        return promise;
    }

    public getProject(projectId: number): Promise<ProjectInfo> {
        this.logger.debug("Searching for id: {0}", projectId);
        const promise: Promise<ProjectInfo> = this.http.get(`projects/${projectId}`);
        promise.then(it => this.logger.info("Received project: {0}", JSON.stringify(it)));
        return promise;
    }
}

