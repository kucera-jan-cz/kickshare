/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Component, Input} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import "rxjs/add/operator/switchMap";
import {Backer} from "../../../services/domain";

@Component({
  selector: 'group_members',
  // styleUrls: ['./group_members.scss'],
  templateUrl: './group_members.html'
})
export class GroupMembers {
  my_id: number = 1;
  @Input() backers: Backer[];

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit() {

  }

  private init() {
  }
}
