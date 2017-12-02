/**
 * Created by KuceraJan on 21.5.2017.
 */
import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {Observable} from "rxjs/Observable";
import {AuthHttp} from "./auth-http.service";
import "rxjs/Rx";
import "rxjs/add/operator/take";
import {LoggerFactory} from "../components/logger/loggerFactory.component";

@Injectable()
export class AuthGuardService implements CanActivate {
    private logger = LoggerFactory.getLogger('services:auth:guard');
    constructor(private authHttp: AuthHttp, private router: Router) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        this.logger.info("Checking activation: " + this.authHttp.isAuthenticated());
        return this.authHttp.getAuthEmitter().map(authenticated => {
            this.logger.info("Authenticated: " + authenticated);
            if (!authenticated) {
                this.logger.info("Routing to login, returnUrl: " + state.url);
                // this.router.navigate(['login']);
                this.router.navigate(['/cz/login'], {queryParams: {returnUrl: state.url}});
            } else {
                this.logger.info("User is authenticated");
                return true;
            }
        }).take(1);
    }
}
