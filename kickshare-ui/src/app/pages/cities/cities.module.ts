import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";

import {Cities} from "./citites.component";
import {routing} from "./cities.routing";
import {ProjectService} from "../../services/project.service";
/**
 * Created by KuceraJan on 16.4.2017.
 */
@NgModule({
    imports: [
      CommonModule,
      FormsModule,
      routing,
    ],

  declarations: [
    Cities
  ],
  providers: [
    ProjectService
  ]
})
export class CitiesModule {

}
