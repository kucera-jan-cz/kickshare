import {Injectable} from "@angular/core";
import {AuthHttp} from "./auth-http.service";
import "rxjs/Rx";
import "rxjs/add/operator/take";
import {LoggerFactory} from "../components/logger/loggerFactory.component";

/**
 * Created by KuceraJan on 2.5.2017.
 */
declare var geoplugin_countryName: any;
declare var geoplugin_countryCode: any;
declare var geoplugin_latitude: any;
declare var geoplugin_longitude: any;

@Injectable()
export class SystemService {
    private logger = LoggerFactory.getLogger('services:system');
    country: string;
    public countryCode: string;
    backerId: number = -1;
    private _current_lat: number;
    private _current_lon: number;

    constructor(private auth: AuthHttp) {
        try {
            this.country = geoplugin_countryName();
            this.countryCode = geoplugin_countryCode();
            this._current_lat = geoplugin_latitude();
            this._current_lon = geoplugin_longitude();
        } catch (ex) {
            this.logger.info("Failed to load geo plugin");
            this.country = 'Czech republic';
            this.countryCode = 'CZ';
            this._current_lat = 	50.05;
            this._current_lon = 	14.22;
        }
        this.auth.getUserIdEmitter().subscribe(userId => {
                this.logger.info("Emmiter ID: {0}", userId);
                this.backerId = userId;
            }
        );
        // this.init();
        // this.country = 'Czech republic';
    }
    get current_lat(): number {
        return this._current_lat;
    }
    get current_lon(): number {
        return this._current_lon;
    }

    getCountry(): string {
        return this.country;
    }

    getId(): number {
        return this.backerId
    }
}
