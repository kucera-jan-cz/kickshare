import {Component} from '@angular/core';
import {Country} from "../../services/domain";
import {CountryService} from "../../services/country.service";
import {Router} from "@angular/router";
import {LoggerFactory} from "../../components/logger/loggerFactory.component";
import {CountryConstants} from "../../constants/country.constants";

@Component({
    selector: 'app-landing-page',
    templateUrl: './landing-page.component.html',
    styleUrls: ['./landing-page.component.scss']
})
export class LandingPageComponent {
    private logger = LoggerFactory.getLogger('component:landing-page');
    countries: Country[] = CountryConstants.countries();
    selectedCountry: Country = this.countries[0];
    rememberCountry: boolean = false;

    constructor(private router: Router, private countryService: CountryService) {
        const code = window.localStorage.getItem(CountryConstants.COUNTRY_LOCAL_STORAGE);
        if (code) {
            this.logger.debug("Using local storage({0}) and redirecting", code);
        } else {
            this.logger.debug("Detecting country....");
        }
    }

    public onSubmit() {
        const code = this.selectedCountry.code;
        if (this.rememberCountry) {
            this.logger.debug("Storing {0} to local storage", code);
            window.localStorage.setItem(CountryConstants.COUNTRY_LOCAL_STORAGE, code);
        }
        this.redirect(code);
    }

    private redirect(countryCode: string) {
        const code = countryCode.toLowerCase();
        const landingUrl = `/${code}/dashboard`;
        this.router.navigateByUrl(landingUrl);
    }
}

