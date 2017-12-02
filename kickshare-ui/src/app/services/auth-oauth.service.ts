import {Injectable} from "@angular/core";
import {Headers, Http, Jsonp, RequestOptionsArgs, Response, URLSearchParams} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {PRIMARY_OUTLET, Router} from "@angular/router";
import {AuthHttp} from "./auth-http.service";
import {OAuthService} from "angular-oauth2-oidc";
import {HttpHeaders} from "@angular/common/http";
import {LoggerFactory} from "../components/logger/loggerFactory.component";

@Injectable()
export class OauthHttp extends AuthHttp {
    private logger = LoggerFactory.getLogger('services:auth:oauth');
    private host = "http://localhost:9000";
    // private host = "http://localhost:8080";
    // private host = "https://local.kickshare.eu";

    constructor(private http: Http, private jsonp: Jsonp, private router: Router, private oauthService: OAuthService) {
        super();
        // this.oauthService.loginUrl = 'http://localhost:8080/oauth/token';
        this.oauthService.scope = 'read';
        this.oauthService.clientId = 'user';
        this.oauthService.dummyClientSecret = 'user';
        this.oauthService.tokenEndpoint = this.host +'/oauth/token';
        this.oauthService.setStorage(sessionStorage);
        //@TODO - figure out whether this is valid approach
        this.oauthService.logoutUrl = this.host + '';
        // this.oauthService.setupAutomaticSilentRefresh();
        // this.oauthService.restartSessionChecksIfStillLoggedIn();
    }

    public async authenticate(username: string, password: string): Promise<Number> {
        var headers = new HttpHeaders("Authorization:Basic dXNlcjp1c2Vy");
        let token = await this.oauthService.fetchTokenUsingPasswordFlow(username, password, headers);
        this.logger.info("Authentication OAuth: " + token['access_token']);
        this.logger.info("Is authenticated: " + this.isAuthenticated());
        this.logger.info("Token: " + this.oauthService.getAccessToken());
        let user = await this.getResponse("/accounts/user");
        this.logger.info("User: " + user.json()['name']);
        this.logger.info("Response: " + JSON.stringify(user.json()));
        this.logger.info(JSON.stringify(token));
        //@TODO - extract also country

        let id = user.json()['principal']['id'] as number;
        this.logger.debug("Emitting ID: " + id);
        this.userIdSubject.next(id);
        this.authenticatedSubject.next(true);
        // const userId = token.json()['id'] as number;
        return Promise.resolve(user.json()['name'].length);
    }

    public isAuthenticated(): boolean {
        return this.oauthService.getAccessToken() != null;
    }

    public logout(): void {
        this.oauthService.logOut();
        //@TODO - consider redirect to dashboard
        location.reload()
    }

    public getJsonp(path, handler: (...args: any[]) => void): void {
        const url: string = `${this.host}/${path}`;
        var args = this.createRequestArgs();
        this.logger.info("Calling JSONP: " + url);
        this.jsonp.get(url, args).subscribe(response => handler(response));
    }

    public getResponse(path, params?: URLSearchParams): Promise<Response> {
        const url: string = `${this.host}/${path}`;
        var args = this.createRequestArgs(params);
        return this.http.get(url, args).toPromise();
    }

    public postResponse(path, data): Promise<Response> {
        const url: string = `${this.host}/${path}`;
        var args = this.createRequestArgs();
        return this.http.post(url, data, args).toPromise();
    }

    public patchResponse(path, data): Promise<Response> {
        const url: string = `${this.host}/${path}`;
        var args = this.createRequestArgs();
        return this.http.patch(url, data, args).toPromise();
    }

    private createRequestArgs(params?: URLSearchParams): RequestOptionsArgs {
        let headers: Headers = new Headers();
        headers.append('Content-Type', 'application/json');
        if(this.oauthService.hasValidAccessToken()) {
            this.logger.info("Including token: "+ this.oauthService.getAccessToken());
            headers.append('Authorization', 'Bearer ' + this.oauthService.getAccessToken());
        }
        const urlTree = this.router.parseUrl(this.router.url);
        const country = urlTree.root.children[PRIMARY_OUTLET].segments[0].toString().toUpperCase();
        this.logger.debug("URL: " + this.router.url);
        this.logger.debug("URL PATH: " + country);
        //@TODO - start using valid country schema, once this logic is stable (maybe after checking supported codes?)
        headers.append('country', 'CZ');

        var args: RequestOptionsArgs = {
            headers: headers,
            params: params,
            withCredentials: true
        };
        return args
    }

}