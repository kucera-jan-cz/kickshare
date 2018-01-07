import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {routing} from "./groupRegistration.routing";
import {GroupRegistration} from "./groupRegistration.component";
import {ProjectService} from "../../services/project.service";
import {SystemService} from "../../services/system.service";
import {KickstarterService} from "../../services/kickstarter.service";
import {GroupService} from "../../services/group.service";
import {CityService} from "../../services/city.service";
import {CategoryModule} from "../../components/category/category.module";
import {CategoryService} from "../../services/categories.service";

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        FormsModule,
        routing,
        CategoryModule
    ],
    declarations: [
        GroupRegistration,
    ],
    providers: [
        //@TODO - consider whether exporting this service with CategoryModule
        CategoryService,
        ProjectService,
        SystemService,
        KickstarterService,
        GroupService,
        CityService
    ]
})
export class GroupRegistrationModule {
}
