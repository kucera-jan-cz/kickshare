/**
 * Created by KuceraJan on 9.4.2017.
 */
// import {Http, RequestOptionsArgs} from "@angular/http";
// import {Http, Headers, URLSearchParams} from 'angular2/http';
import {URLSearchParams} from "@angular/http";
import {Injectable} from "@angular/core";
import "rxjs/Rx";
import {ProjectInfo} from "./domain";
import {AuthHttp} from "./auth-http.service";
import {LoggerFactory} from "../components/logger/loggerFactory.component";

@Injectable()
export class KickstarterService {
    private logger = LoggerFactory.getLogger('services:auth:kickstarter');
    
  constructor(private http: AuthHttp) {}

  public searchProjects(name: string): Promise<ProjectInfo[]> {
    this.logger.debug("Searching for Kickstarter project: " + name);
    let params = new URLSearchParams();
    params.set("name", name);
    //@TODO externalize this for more values
    params.set("categoryId", "34");
    params.set("store", "true");

    return this.http.getResponse('/kickstarter/search', params).then(
      res => {
        const projects = res.json() as ProjectInfo[];
        this.logger.info("Received projects: " + projects.map(p => p.name).join(","));
        return projects;
      }
    );
  }

}
