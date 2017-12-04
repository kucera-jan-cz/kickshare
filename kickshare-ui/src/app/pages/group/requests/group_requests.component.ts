/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Component, Input} from "@angular/core";
import "rxjs/add/operator/switchMap";
import {Backer} from "../../../services/domain";

@Component({
  selector: 'group_requests',
  templateUrl: './group_requests.html'
})
export class GroupRequests {
    @Input() backers: Backer[];
}
