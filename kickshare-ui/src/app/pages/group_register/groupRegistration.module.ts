import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {routing} from "./groupRegistration.routing";
import {GroupRegistration} from "./groupRegistration.component";
import {ProjectService} from "../../services/project.service";
import {SystemService} from "../../services/system.service";
import {KickstarterService} from "../../services/kickstarter.service";
import {GroupService} from "../../services/group.service";

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        FormsModule,
        // NgaModule,
        routing
    ],
    declarations: [
        GroupRegistration,
        // ModalComponent
    ],
    providers: [
        ProjectService,
        SystemService,
        KickstarterService,
        GroupService
    ]
})
export class GroupRegistrationModule {
}
