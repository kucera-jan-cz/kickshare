import {Injectable} from "@angular/core";
import {Headers, Http, Jsonp, RequestOptionsArgs, Response, URLSearchParams} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {PRIMARY_OUTLET, Router} from "@angular/router";
import {AuthHttp} from "./auth-http.service";
import {LoggerFactory} from "../components/logger/loggerFactory.component";

@Injectable()
export class BasicAuthHttp extends AuthHttp {
    private host = "http://localhost:9000";
    private logger = LoggerFactory.getLogger('services:auth:basic');

    // private host = "https://local.kickshare.eu";

    constructor(private http: Http, private jsonp: Jsonp, private router: Router) {
        super();
        if (this.isAuthenticated()) {

        }
    }

    public authenticate(username: string, password: string): Promise<Number> {
        const url: string = `${this.host}/authenticate?username=${username}&password=${password}`;
        const basic = "Basic " + btoa(username + ":" + password);
        let headers: Headers = new Headers(['Authorization', basic]);
        var args: RequestOptionsArgs = {
            headers: headers,
        };
        const promise = this.http.post(url, null, args).share();
        promise.subscribe(
            data => {
                this.saveSession(data, basic);
                username = null;
                password = null;
                const userId = data.json()['id'] as number;
                this.logger.info("Authentication successful ID: {0}", userId);
                this.userIdSubject.next(userId);
                this.authenticatedSubject.next(true);
            },
            err => console.error('Failed to login: ' + err),
            () => console.log('Authentication Complete')
        );
        return promise.map(data => data.json()['id'] as number).toPromise();
    }

    public isAuthenticated(): boolean {
        return localStorage.getItem('authorization') != null;
    }

    public logout(): void {
        localStorage.removeItem('authorization');
        this.userIdSubject.next(null);
        this.authenticatedSubject.next(false);
    }

    public getJsonp(path, handler: (...args: any[]) => void): void {
        const url: string = `${this.host}/${path}`;
        var args = this.createRequestArgs();
        this.logger.debug("Calling JSONP: {0}", url);
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
        // headers.append('Authorization', 'Basic eGF0cml4MTAxQGdtYWlsLmNvbTp1c2Vy');
        // headers.append('Authorization', localStorage.getItem('authorization'));
        this.logger.info("Expected: Basic eGF0cml4MTAxQGdtYWlsLmNvbTp1c2Vy");
        this.logger.info("Received: " + localStorage.getItem('authorization'));
        // headers.append('Cookies', 'JSESSIONID='+localStorage.getItem('authorization'));
        const urlTree = this.router.parseUrl(this.router.url);
        const country = urlTree.root.children[PRIMARY_OUTLET].segments[0].toString().toUpperCase();
        this.logger.info("URL: " + this.router.url);
        this.logger.info("URL PATH: " + country);
        headers.append('country', 'CZ');
        // headers.append('Authorization', localStorage.getItem('authorization'));

        // headers.append('Cookie', sessionStorage.getItem('session'));
        var args: RequestOptionsArgs = {
            headers: headers,
            params: params,
            withCredentials: true
        };
        return args
    }

    private saveSession(response: Response, authorization: string) {
        this.logger.info("Type: " + response.constructor.name);
        this.logger.info(JSON.stringify(response));
        const cookie = response.headers.get('Set-Cookie');
        this.logger.info("Headers: " + JSON.stringify(response.headers));
        this.logger.info("Cookie: " + cookie);
        localStorage.setItem('session', cookie);
        localStorage.setItem('authorization', authorization);
    }
}
