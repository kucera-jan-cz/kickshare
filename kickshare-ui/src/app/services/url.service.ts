import {Injectable} from "@angular/core";
import {SystemService} from "./system.service";
import {environment} from "../../environments/environment";
import * as format from "string-template";

@Injectable()
export class UrlService {
    private EMPTY = 'javascript:void(0)';
    private _country: string;

    constructor(private system: SystemService) { }

    get country() {
        if (!this._country) {
            this._country = this.system.getCountry().toLowerCase();
        }
        return this._country;
    }

    public landingPageUrl(country: string = null): string {
        country = country || this.country;
        const landingUrl = format(environment.landing_page, country);
        return landingUrl;
    }

    public campaign(id: number): string {
        if(id) {
            const path = `/${this.country}/campaign/${id}`;
            return path;
        } else {
            return this.EMPTY;
        }
    }

    public group(id: number): string {
        if(id) {
            const path = `/${this.country}/group/${id}`;
            return path;
        } else {
            return this.EMPTY;
        }
    }

    public login(): string {
        return `/${this.country}/login`;
    }

    public register(): string {
        return `/${this.country}/sign-in`;
    }
}