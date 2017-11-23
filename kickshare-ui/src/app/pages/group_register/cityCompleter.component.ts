import {Subject} from "rxjs/Subject";

import {CompleterData, CompleterItem} from "ng2-completer";
import {Injectable} from "@angular/core";
import {CityService} from "../../services/city.service";
import {City} from "../../services/domain";

@Injectable()
export class CityCompleter extends Subject<CompleterItem[]> implements CompleterData {
    constructor(private cityService: CityService) {
        super();
        const empty: CompleterItem[] = [];
        this.next(empty);
    }
    async search(term: string) {
        let data = await this.cityService.getCities(term);
        // Now we can slice/sort/change or manipulate the result in any way that we want
        data = data.slice(0, 10);

        let matches: CompleterItem[] = data.map((item: any) => this.convertToItem(item));
        // pass the result to ng2-completer
        console.info("Passing: " + JSON.stringify(matches));
        this.next(matches);
    }

    cancel(): void {
    }

    public convertToItem(data: City): CompleterItem {
        if (!data) {
            return null;
        }
        if (!data.name) {
            console.error("There is no name !!!!!");
        }
        // data will be string if an initial value is set
        return {
            title: data.name,
            originalObject: data
        }
    }
}