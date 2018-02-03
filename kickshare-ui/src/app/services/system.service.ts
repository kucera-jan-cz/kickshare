import {Injectable} from "@angular/core";
import {AuthHttp} from "./auth-http.service";
import "rxjs/Rx";
import "rxjs/add/operator/take";
import {LoggerFactory} from "../components/logger/loggerFactory.component";
import {loadScript, random} from "../utils/util";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {AsyncSubject} from "rxjs/AsyncSubject";
import {CountryConstants} from "../constants/country.constants";
import {Router} from "@angular/router";
import {environment} from "../../environments/environment";

/**
 * Created by KuceraJan on 2.5.2017.
 */
//@TODO - delete this?
declare var geoplugin_countryName: any;
declare var geoplugin_countryCode: any;
declare var geoplugin_latitude: any;
declare var geoplugin_longitude: any;

@Injectable()
export class SystemService {
    private logger = LoggerFactory.getLogger('services:system');

    public countryCode: string;
    backerId: number = -1;
    private _current_lat: number;
    private _current_lon: number;
    private static initialized = false;
    private initSubject = new BehaviorSubject(false);
    private asyncInit = new AsyncSubject<boolean>();
    private initObservable = this.initSubject.filter(it => it == true);

    constructor(private auth: AuthHttp, private router: Router) {
        this.logger.info("Initializing System Service");
        try {
            //     this.countryCode = geoplugin_countryCode();
            this._current_lat = geoplugin_latitude();
            this._current_lon = geoplugin_longitude();
        } catch (ex) {
            //     this.logger.info("Failed to load geo plugin");
            //     this.countryCode = 'CZ';
            this._current_lat = 50.05;
            this._current_lon = 14.22;
        }
        this.auth.getUserIdEmitter().subscribe(userId => {
                this.logger.info("Emmiter ID: {0}", userId);
                this.backerId = userId;
            }
        );
    }

    public setCountry(code: string): void {
        if(code.length == 2) {
            this.countryCode = code.toUpperCase();
        } else {
            throw new Error("Country code is not valid: " + code);
        }
    }

    public getCountry(): string {
        const country = this.countryCode || this.router.routerState.snapshot.url.substr(1, 2).toUpperCase();
        if (country == null || country.length != 2) {
            throw new Error("Country code is not yet resolved: " + country);
        }
        return country;
    }

    get current_lat(): number {
        return this._current_lat;
    }

    get current_lon(): number {
        return this._current_lon;
    }

    getId(): number {
        return this.backerId
    }

    public static initializeGoogleMaps() {
        if (SystemService.initialized || !environment.google_maps_enabled) {
            return;
        }
        try {
            const i = random(0, 10) % CountryConstants.countries().length;
            const key = "AIzaSyA-NAJu2diDDRzMKz9jKTIj6HVXODMjXpk";
            loadScript("https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/markerclusterer.js");
            loadScript(`https://maps.googleapis.com/maps/api/js?key=${key}`);
        } catch (ex) {
            console.error("Failed to load maps: {0}", ex);
        } finally {
            SystemService.initialized = true;
        }
    }
}
