/**
 * Created by KuceraJan on 21.5.2017.
 */
import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {Observable} from "rxjs/Observable";
import {AuthHttp} from "./auth-http.service";
import "rxjs/Rx";
import "rxjs/add/operator/take";

@Injectable()
export class AuthGuardService implements CanActivate {
    constructor(private authHttp: AuthHttp, private router: Router) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        console.info("Checking activation: " + this.authHttp.isAuthenticated());
        return this.authHttp.getAuthEmitter().map(authenticated => {
            console.info("Authenticated: " + authenticated);
            if (!authenticated) {
                console.info("Routing to login, returnUrl: " + state.url);
                // this.router.navigate(['login']);
                this.router.navigate(['/cz/login'], {queryParams: {returnUrl: state.url}});
            } else {
                console.info("User is authenticated");
                return true;
            }
        }).take(1);
    }
}
