import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Rx";
import {LoggerFactory} from "../components/logger/loggerFactory.component";

@Injectable()
export class CountryService {
    private logger = LoggerFactory.getLogger('services:country');
    constructor(private http: HttpClient) {
    }

    public getInfo(): Observable<MetaInformation> {
        const opts: Options = {observe: 'body', responseType: 'json'};
        const ipInfo = this.http.get("http://ipinfo.io/json", opts).map(it => new MetaInformation(it['ip'], it['country'].toUpperCase()));
        //@TODO - include commented services once we will run on HTTPS
        // const ipapi = this.http.get("http://ipapi.co/json", opts).map(it => new MetaInformation(it['ip'], it['country'].toUpperCase()));
        // const freegeoip = this.http.get("http://freegeoip.net", opts).map(it => new MetaInformation(it['ip'], it['country_code'].toUpperCase()));

        const infoObservable = Observable.merge(ipInfo).take(1);
        return infoObservable;
    }

}

export class MetaInformation {
    constructor(public ip: string, public country: string) {
    }
}

export class Options {
    observe: 'body';
    responseType: 'json';
}