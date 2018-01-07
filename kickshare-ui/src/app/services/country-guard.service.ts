/**
 * Created by KuceraJan on 21.5.2017.
 */
import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, CanActivateChild, PRIMARY_OUTLET, Router, RouterStateSnapshot} from "@angular/router";
import {LoggerFactory} from "../components/logger/loggerFactory.component";
import {loadScript} from "../utils/util";
import {Subject} from "rxjs/Rx";
import {Observable} from 'rxjs/Observable';

@Injectable()
export class CountryGuardService implements CanActivateChild, CanActivate {
    private logger = LoggerFactory.getLogger('services:country:guard');
    private countrySubject: Subject<string> = new Subject();
    private countryObservable: Observable<string>;

    constructor(private router: Router) {
        this.logger.debug("Initializing Country Guard Service");
        this.countryObservable = this.countrySubject.distinctUntilChanged();
        this.countryObservable.subscribe(countryCode => this.loadGoogleMaps(countryCode));
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        return this.validateAndStoreCountry(route, state);
    }

    canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        return this.validateAndStoreCountry(childRoute, state);
    }

    public getCountryEmitter(): Observable<string> {
        return this.countryObservable;
    }

    private validateAndStoreCountry(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        this.logger.debug("Routing to URL: " + state.url);
        const countryCode = this.extractCountryCode(state.url);
        //@TODO - implement supported country validation
        this.countrySubject.next(countryCode);
        return Observable.of(true);
    }

    private extractCountryCode(url: string): string {
        const urlTree = this.router.parseUrl(url);
        this.logger.trace("Tree: " + urlTree.root.children[PRIMARY_OUTLET]);
        const countryCode = urlTree.root.children[PRIMARY_OUTLET].segments[0].toString().toUpperCase();
        this.logger.info("Setting country code {0}", countryCode);
        return countryCode;
    }

    private loadGoogleMaps(countryCode:string): void {
        this.logger.info("Loading google maps for country {0}", countryCode);
        const key = "AIzaSyA-NAJu2diDDRzMKz9jKTIj6HVXODMjXpk";
        loadScript("https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/markerclusterer.js");
        loadScript(`https://maps.googleapis.com/maps/api/js?key=${key}`);
    }

}
