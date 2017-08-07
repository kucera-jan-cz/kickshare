/**
 * Created by KuceraJan on 9.4.2017.
 */
import {Component} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import "rxjs/add/operator/switchMap";

@Component({
  selector: 'group_discussion',
  styleUrls: ['./group_discussion.scss'],
  templateUrl: './group_discussion.html'
})
export class GroupDiscussion {

  constructor(private route: ActivatedRoute) {
  }

}
