import {Injectable} from "@angular/core";
import "rxjs/add/operator/toPromise";
import {PRIMARY_OUTLET, Router} from "@angular/router";
import {AuthHttp} from "./auth-http.service";
import {OAuthService} from "angular-oauth2-oidc";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {LoggerFactory} from "../components/logger/loggerFactory.component";

@Injectable()
export class OauthHttp extends AuthHttp {
    private logger = LoggerFactory.getLogger('services:auth:oauth');
    private static host = "http://localhost:9000";
    // private host = "http://localhost:8080";
    // private host = "https://local.kickshare.eu";

    constructor(protected http: HttpClient, private router: Router, private oauthService: OAuthService) {
        super(OauthHttp.host, http);
        // this.oauthService.loginUrl = 'http://localhost:8080/oauth/token';
        this.oauthService.scope = 'read';
        this.oauthService.clientId = 'user';
        this.oauthService.dummyClientSecret = 'user';
        this.oauthService.tokenEndpoint = this.host + '/oauth/token';
        this.oauthService.setStorage(sessionStorage);
        //@TODO - figure out whether this is valid approach
        this.oauthService.logoutUrl = this.host + '';
        this.oauthService.restartSessionChecksIfStillLoggedIn();
        // this.oauthService.setupAutomaticSilentRefresh();
        // this.oauthService.restartSessionChecksIfStillLoggedIn();
    }

    public async authenticate(username: string, password: string): Promise<Number> {
        var headers = new HttpHeaders("Authorization:Basic dXNlcjp1c2Vy");
        let token = await this.oauthService.fetchTokenUsingPasswordFlow(username, password, headers);
        this.logger.info("Authentication OAuth: " + token['access_token']);
        this.logger.info("Is authenticated: " + this.isAuthenticated());
        this.logger.info("Token: " + this.oauthService.getAccessToken());
        let user = await this.get("/accounts/user");
        this.logger.info("User: " + user['name']);
        this.logger.info("Response: " + JSON.stringify(user));
        this.logger.info(JSON.stringify(token));
        //@TODO - extract also country

        let id = user['principal']['id'] as number;
        this.logger.debug("Emitting ID: " + id);
        this.userIdSubject.next(id);
        this.authenticatedSubject.next(true);
        // const userId = token.json()['id'] as number;
        return Promise.resolve(user['name'].length);
    }

    public isAuthenticated(): boolean {
        return this.oauthService.getAccessToken() != null;
    }

    public logout(): void {
        this.oauthService.logOut();
        //@TODO - consider redirect to dashboard
        location.reload()
    }

    protected authHeaders(): HttpHeaders {
        var headers: HttpHeaders = new HttpHeaders();
        if (this.oauthService.hasValidAccessToken()) {
            this.logger.info("Including token: " + this.oauthService.getAccessToken());
            headers = headers.append('Authorization', 'Bearer ' + this.oauthService.getAccessToken());
        }
        const urlTree = this.router.parseUrl(this.router.url);
        const country = urlTree.root.children[PRIMARY_OUTLET].segments[0].toString().toUpperCase();
        this.logger.debug("URL: " + this.router.url);
        this.logger.debug("URL PATH: " + country);
        //@TODO - start using valid country schema, once this logic is stable (maybe after checking supported codes?)
        headers = headers.append('country', 'CZ');
        return headers;
    }
}