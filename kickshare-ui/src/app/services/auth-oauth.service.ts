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
    // private host = "http://localhost:9000";
    private host = "https://local.kickshare.eu";
    private authenticatedSubject: BehaviorSubject<boolean> = new BehaviorSubject(null);
    private userIdSubject: BehaviorSubject<number> = new BehaviorSubject(null);
    authenticateEmitter: Observable<boolean>;
    userIdEmitter: Observable<number>;

    constructor(private http: Http, private jsonp: Jsonp, private router: Router, private oauthService: OAuthService) {
        this.authenticateEmitter = this.authenticatedSubject.asObservable();
        this.userIdEmitter = this.userIdSubject.asObservable();
        this.oauthService.loginUrl = '';
        this.oauthService.scope = '';
        this.oauthService.clientId = '';
        this.oauthService.setStorage(sessionStorage);
    }

    public async authenticate(username: string, password: string): Promise<Number> {
        let token = await this.oauthService.fetchTokenUsingPasswordFlow(username, password);
        console.info("Authentication OAuth");
        console.info(JSON.stringify(token));
        // const userId = token.json()['id'] as number;
        return Promise.apply(1);
    }

    public isAuthenticated(): boolean {
        return this.oauthService.getAccessToken() != null;
    }

    public getAuthEmitter(): Observable<boolean> {
        return this.authenticateEmitter;
    }

    public logout(): void {
        this.oauthService.logOut();
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
        headers.append('Authorization', 'Bearer ' + this.oauthService.getAccessToken());
        const urlTree = this.router.parseUrl(this.router.url);
        const country = urlTree.root.children[PRIMARY_OUTLET].segments[0].toString().toUpperCase();
        console.info("URL: " + this.router.url);
        console.info("URL PATH: " + country);
        headers.append('country', 'CZ');

        var args: RequestOptionsArgs = {
            headers: headers,
            params: params,
            withCredentials: true
        };
        return args
    }

}