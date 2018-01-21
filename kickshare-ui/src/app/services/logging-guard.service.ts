/**
 * Created by KuceraJan on 21.5.2017.
 */
import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, CanActivateChild, RouterStateSnapshot} from "@angular/router";
import {LoggerFactory} from "../components/logger/loggerFactory.component";

@Injectable()
export class LoggingGuardService implements CanActivateChild, CanActivate {
    private logger = LoggerFactory.getLogger('services:country:guard');

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        console.error("Can activate " + state.url);
        this.logger.debug("Can activate {0}", state.url);
        return true;
    }

    canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        console.error("Can activate child " + state.url);
        this.logger.debug("Can activate child {0}", state.url);
        return true;
    }
}
