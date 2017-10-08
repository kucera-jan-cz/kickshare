import {Injectable} from "@angular/core";
import {Headers, Http, Jsonp, RequestOptionsArgs, Response, URLSearchParams} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {Observable} from "rxjs/Observable";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {PRIMARY_OUTLET, Router} from "@angular/router";
import {AuthHttp} from "./auth-http.service";
import {OAuthService} from "angular-oauth2-oidc";

@Injectable()
export class OauthHttp implements AuthHttp {
    private host = "http://localhost:9000";
    // private host = "http://localhost:8080";
    // private host = "https://local.kickshare.eu";
    private authenticatedSubject: BehaviorSubject<boolean> = new BehaviorSubject(null);
    private userIdSubject: BehaviorSubject<number> = new BehaviorSubject(null);
    authenticateEmitter: Observable<boolean>;
    userIdEmitter: Observable<number>;

    constructor(private http: Http, private jsonp: Jsonp, private router: Router, private oauthService: OAuthService) {
        this.authenticateEmitter = this.authenticatedSubject.asObservable();
        this.userIdEmitter = this.userIdSubject.asObservable();
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
        var headers = Headers.fromResponseHeaderString("Authorization:Basic dXNlcjp1c2Vy");
        let token = await this.oauthService.fetchTokenUsingPasswordFlow(username, password, headers);
        console.info("Authentication OAuth: " + token['access_token']);
        console.info("Is authenticated: " + this.isAuthenticated());
        console.info("Token: " + this.oauthService.getAccessToken());
        let user = await this.get("/accounts/user");
        console.info("User: " + user.json()['name']);
        console.info(JSON.stringify(token));
        //@TODO - extract also country

        this.userIdSubject.next(user.json()['name'].length);
        this.authenticatedSubject.next(true);
        // const userId = token.json()['id'] as number;
        return Promise.resolve(user.json()['name'].length);
    }

    public isAuthenticated(): boolean {
        return this.oauthService.getAccessToken() != null;
    }

    public getAuthEmitter(): Observable<boolean> {
        return this.authenticateEmitter;
    }

    public logout(): void {
        this.oauthService.logOut();
        //@TODO - consider redirect to dashboard
        location.reload()
    }

    public getJsonp(path, handler: (...args: any[]) => void): void {
        const url: string = `${this.host}/${path}`;
        var args = this.createRequestArgs();
        console.info("Calling JSONP: " + url);
        this.jsonp.get(url, args).subscribe(response => handler(response));
    }

    public get(path, params?: URLSearchParams): Promise<Response> {
        const url: string = `${this.host}/${path}`;
        var args = this.createRequestArgs(params);
        return this.http.get(url, args).toPromise();
    }

    public post(path, data): Promise<Response> {
        const url: string = `${this.host}/${path}`;
        var args = this.createRequestArgs();
        return this.http.post(url, data, args).toPromise();
    }

    public patch(path, data): Promise<Response> {
        const url: string = `${this.host}/${path}`;
        var args = this.createRequestArgs();
        return this.http.patch(url, data, args).toPromise();
    }

    private createRequestArgs(params?: URLSearchParams): RequestOptionsArgs {
        let headers: Headers = new Headers();
        headers.append('Content-Type', 'application/json');
        if(this.oauthService.hasValidAccessToken()) {
            console.info("Including token: "+ this.oauthService.getAccessToken());
            headers.append('Authorization', 'Bearer ' + this.oauthService.getAccessToken());
        }
        const urlTree = this.router.parseUrl(this.router.url);
        const country = urlTree.root.children[PRIMARY_OUTLET].segments[0].toString().toUpperCase();
        console.info("URL: " + this.router.url);
        console.info("URL PATH: " + country);
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