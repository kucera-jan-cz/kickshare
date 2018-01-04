import {Injectable} from "@angular/core";
import "rxjs/add/operator/toPromise";
import {PRIMARY_OUTLET, Router} from "@angular/router";
import {AuthHttp} from "./auth-http.service";
import {LoggerFactory} from "../components/logger/loggerFactory.component";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";

@Injectable()
export class BasicAuthHttp extends AuthHttp {
    private logger = LoggerFactory.getLogger('services:auth:basic');
    private static host = "http://localhost:9000";
    // private host = "https://local.kickshare.eu";

    constructor(protected http: HttpClient, private router: Router) {
        super(BasicAuthHttp.host, http);
        if (this.isAuthenticated()) {

        }
    }

    public authenticate(username: string, password: string): Promise<Number> {
        const url: string = `${this.host}/authenticate?username=${username}&password=${password}`;
        const basic = "Basic " + btoa(username + ":" + password);
        let headers: HttpHeaders = new HttpHeaders({'Authorization': basic});
        const observable = this.http.post(url, null, {'headers': headers, observe: 'response'});
        observable.subscribe(
            data => {
                this.saveSession(data, basic);
                username = null;
                password = null;
                const userId = data['id'] as number;
                this.logger.info("Authentication successful ID: {0}", userId);
                this.userIdSubject.next(userId);
                this.authenticatedSubject.next(true);
            },
            err => console.error('Failed to login: ' + err),
            () => console.log('Authentication Complete')
        );
        return observable.map(data => data['id'] as number).toPromise();
    }

    public isAuthenticated(): boolean {
        return localStorage.getItem('authorization') != null;
    }

    public logout(): void {
        localStorage.removeItem('authorization');
        this.userIdSubject.next(null);
        this.authenticatedSubject.next(false);
    }

    protected authHeaders(): HttpHeaders {
        let headers: HttpHeaders = new HttpHeaders();

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
        return headers;
    }

    private saveSession(response: HttpResponse<any>, authorization: string) {
        this.logger.info("Type: " + response.constructor.name);
        this.logger.info(JSON.stringify(response));
        const cookie = response.headers.get('Set-Cookie');
        this.logger.info("Headers: " + JSON.stringify(response.headers));
        this.logger.info("Cookie: " + cookie);
        localStorage.setItem('session', cookie);
        localStorage.setItem('authorization', authorization);
    }
}
