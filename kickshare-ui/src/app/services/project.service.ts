/**
 * Created by KuceraJan on 9.4.2017.
 */
import {URLSearchParams} from "@angular/http";

import {Injectable} from "@angular/core";
import {AuthHttp} from "./auth-http.service";
import {ProjectInfo, SearchOptions} from "./domain";
import {stringify} from "query-string";
import {LogService} from "./log.service";

@Injectable()
export class ProjectService {
    private log: LogService

    constructor(private http: AuthHttp) {
        // this.log = new LogService(this);
        this.log = new LogService();
    }

    public searchProjects(options: SearchOptions): Promise<ProjectInfo[]> {
        const params = stringify(options);
        console.info("Searching projects with: " + params);
        return this.http.get("/projects/search?" + params)
            .then(
                res => {
                    console.info("Search project result: " + JSON.stringify(res));
                    return res.json() as ProjectInfo[];
                }
            )
    }

    public searchProjectsByName(name: string): Promise<ProjectInfo[]> {
        // console.log("Searching for project: " + name);
        let params = new URLSearchParams();
        params.set("name", name);
        params.set("categoryId", "34");

        return this.http.get('projects', params).then(
            res => {
                // console.info("Received project: " + JSON.stringify(res));
                const projects = res.json() as ProjectInfo[];
                console.info("Received projects: " + projects.map(p => p.name).join(","));
                return projects;
            }
        )
    }

    public getProject(projectId: number): Promise<ProjectInfo> {
        console.log("Searching for id: " + projectId);
        return this.http.get(`projects/${projectId}`).then(
            res => {
                console.info("Received project: " + JSON.stringify(res));
                return res.json() as ProjectInfo
            }
        );
    }
}

