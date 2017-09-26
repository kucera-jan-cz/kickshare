import {Injectable} from "@angular/core";
import {Headers, Http, Jsonp, RequestOptionsArgs, Response, URLSearchParams} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {Observable} from "rxjs/Observable";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {PRIMARY_OUTLET, Router} from "@angular/router";
import {AuthHttp} from "./auth-http.service";

@Injectable()
export class BasicAuthHttp implements AuthHttp {
    private host = "http://localhost:9000";
    // private host = "https://local.kickshare.eu";
    private authenticatedSubject: BehaviorSubject<boolean> = new BehaviorSubject(null);
    private userIdSubject: BehaviorSubject<number> = new BehaviorSubject(null);
    //@TODO make this private field?
    authenticateEmitter: Observable<boolean>;
    userIdEmitter: Observable<number>;

    constructor(private http: Http, private jsonp: Jsonp, private router: Router) {
        this.authenticateEmitter = this.authenticatedSubject.asObservable();
        this.userIdEmitter = this.userIdSubject.asObservable();
        if(this.isAuthenticated()) {

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
                console.info("Authentication successful, ID: " + userId);
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

    public getAuthEmitter(): Observable<boolean> {
        return this.authenticateEmitter;
    }

    public logout(): void {
        localStorage.removeItem('authorization');
        this.userIdSubject.next(null);
        this.authenticatedSubject.next(false);
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
        // headers.append('Authorization', 'Basic eGF0cml4MTAxQGdtYWlsLmNvbTp1c2Vy');
        // headers.append('Authorization', localStorage.getItem('authorization'));
        console.info("Expected: Basic eGF0cml4MTAxQGdtYWlsLmNvbTp1c2Vy");
        console.info("Received: " + localStorage.getItem('authorization'));
        // headers.append('Cookies', 'JSESSIONID='+localStorage.getItem('authorization'));
        const urlTree = this.router.parseUrl(this.router.url);
        const country = urlTree.root.children[PRIMARY_OUTLET].segments[0].toString().toUpperCase();
        console.info("URL: " + this.router.url);
        console.info("URL PATH: " + country);
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
        console.info("Type: " + response.constructor.name);
        console.info(JSON.stringify(response));
        const cookie = response.headers.get('Set-Cookie');
        console.info("Headers: " + JSON.stringify(response.headers));
        console.info("Cookie: "+cookie);
        localStorage.setItem('session', cookie);
        localStorage.setItem('authorization', authorization);
    }
}
