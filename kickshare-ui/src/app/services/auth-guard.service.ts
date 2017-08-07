/**
 * Created by KuceraJan on 21.5.2017.
 */
import {Injectable} from "@angular/core";
import {CanActivate, Router} from "@angular/router";
import {Observable} from "rxjs/Observable";
import {AuthHttp} from "./auth-http.service";
import "rxjs/Rx";
import "rxjs/add/operator/take";

@Injectable()
export class AuthGuardService implements CanActivate {
  constructor(private authHttp: AuthHttp,
              private router: Router) {
  }

  canActivate(): Observable<boolean> {
    console.info("Checking activation: "+this.authHttp.isAuthenticated());
    return this.authHttp.authenticateEmitter.map(authenticated => {
      console.info("Authenticated: "+authenticated);
      if (!authenticated) {
        console.info("Routing to logic");
        this.router.navigate(['login']);
      } else {
        console.info("User is authenticated");
        return true;
      }
    }).take(1);
  }
}
