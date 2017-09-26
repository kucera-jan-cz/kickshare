import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {routing} from "./userRegistration.routing";
import {ProjectService} from "../../services/project.service";
import {SystemService} from "../../services/system.service";
import {KickstarterService} from "../../services/kickstarter.service";
import {GroupService} from "../../services/group.service";
import {UserRegistration} from "./userRegistration.component";
import {Ng2CompleterModule} from "ng2-completer";

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        FormsModule,
        routing,
        Ng2CompleterModule
    ],
    declarations: [
        UserRegistration,
    ],
    providers: [
        ProjectService,
        SystemService,
        KickstarterService,
        GroupService
    ]
})
export class UserRegistrationModule {
}
