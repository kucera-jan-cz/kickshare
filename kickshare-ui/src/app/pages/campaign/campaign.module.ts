import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";

import {Campaign} from "./campaign.component";
import {routing} from "./campaign.routing";
import {ProjectService} from "../../services/project.service";
import {GroupService} from "../../services/group.service";
/**
 * Created by KuceraJan on 9.4.2017.
 */
@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    routing,
  ],

  declarations: [
    Campaign
  ],
  providers: [
    ProjectService,
    GroupService
  ]
})
export class CampaignModule {

}
