import {Injectable} from "@angular/core";
import {SystemService} from "./system.service";

@Injectable()
export class UrlService {
    private EMPTY = 'javascript:void(0)';
    private country: string;

    constructor(private system: SystemService) {
        this.country = system.countryCode.toLowerCase();
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
}