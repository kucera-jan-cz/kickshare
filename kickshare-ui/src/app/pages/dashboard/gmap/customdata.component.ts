/**
 * Created by KuceraJan on 11.6.2017.
 */

import {Subject} from "rxjs/Subject";

import {ProjectService} from "../../../services/project.service";
import {CompleterData, CompleterItem} from "ng2-completer";
import {Injectable} from "@angular/core";
import {Project} from "../../../services/domain";

@Injectable()
export class CustomData extends Subject<CompleterItem[]> implements CompleterData {
    constructor(private projectService: ProjectService) {
        super();
        const empty: CompleterItem[] = [];
        this.next(empty);
    }

    submit(projects: Project[]): void {
        let matches: CompleterItem[] = projects.map((item: any) => this.convertToItem(item));
        this.next(matches);
    }

    public async search(term: string) {
        //@TODO - insert category via subsription
        let data = await this.projectService.searchProjectsByName(34, term);
        // Now we can slice/sort/change or manipulate the result in any way that we want
        data = data.slice(0, 10);

        console.info("Converting projects...");
        // Convert the result to CompleterItem[]
        let matches: CompleterItem[] = data.map((item: any) => this.convertToItem(item));
        // pass the result to ng2-completer
        console.info("Passing: " + JSON.stringify(matches));
        this.next(matches);
    }

    public cancel() {
        // Handle cancel if needed
    }

    public convertToItem(data: Project): CompleterItem {
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
