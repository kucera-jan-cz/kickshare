/**
 * Created by KuceraJan on 21.5.2017.
 */
import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, CanActivateChild, PRIMARY_OUTLET, Router, RouterStateSnapshot} from "@angular/router";
import {LoggerFactory} from "../components/logger/loggerFactory.component";
import {Subject} from "rxjs/Rx";
import {Observable} from "rxjs/Observable";
import {SystemService} from "./system.service";
import {CountryConstants} from "../constants/country.constants";
import {Country} from "./domain";
import {UrlService} from "./url.service";

@Injectable()
export class CountryGuardService implements CanActivateChild, CanActivate {
    private logger = LoggerFactory.getLogger('services:country:guard');
    private countrySubject: Subject<Country> = new Subject();
    private countryObservable: Observable<Country>;

    constructor(private router: Router, private systemService: SystemService, private url: UrlService) {
        this.logger.debug("Initializing Country Guard Service");
        this.countryObservable = this.countrySubject.distinctUntilChanged();
        this.countryObservable.subscribe(country => {
            systemService.setCountry(country.code);
        });
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        this.logger.debug("Can activate {0}", state.url);

        //@TODO - externalize to constant class
        // We retrieve country from data (f.e from url /cz) or we have already set country in local store
        const code: string = route.data['code'] || window.localStorage.getItem(CountryConstants.COUNTRY_LOCAL_STORAGE);
        if (code) {
            this.logger.debug("Initializing {0}", code);
            //Navigate directly to dashboard since user chose permanently country
            this.validateAndInitialize(code);
            const url = this.url.landingPageUrl(code.toLowerCase());
            this.router.navigateByUrl(url);
            return false;
        } else {
            this.logger.debug("Simply passing through");
            return true;
        }
    }

    canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        this.logger.debug("Can activate child {0}", state.url);
        const countryCode = this.extractCountryCode(state.url);
        this.validateAndInitialize(countryCode);
        return true;
    }

    private validateAndInitialize(code: string): Country {
        const country = CountryConstants.country(code);
        if (country) {
            this.logger.debug("Initializing dependencies with country: {0}", country);
            this.countrySubject.next(country);
            return country;
        } else {
            throw "Failed to identify country";
        }
    }

    private extractCountryCode(url: string): string {
        const urlTree = this.router.parseUrl(url);
        const countryCode = urlTree.root.children[PRIMARY_OUTLET].segments[0].toString().toUpperCase();
        this.logger.info("Setting country code {0}", countryCode);
        return countryCode;
    }
}
