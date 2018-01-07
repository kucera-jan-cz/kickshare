import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";

import {Dashboard} from "./dashboard.component";
import {routing} from "./dashboard.routing";
import {GMap} from "./gmap/gmap.component";
import {CategoryService} from "../../services/categories.service";
import {GroupService} from "../../services/group.service";
import {ProjectService} from "../../services/project.service";
import {KickstarterService} from "../../services/kickstarter.service";
import {Ng2CompleterModule} from "ng2-completer";
import {BaCard} from "../../components/baCard/baCard.component";
import {UserService} from "../../services/user.service";
import {SystemService} from "../../services/system.service";
import {CategoryModule} from "../../components/category/category.module";

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        // AppTranslationModule,
        // NgaModule,
        routing,
        Ng2CompleterModule,
        CategoryModule
    ],
    declarations: [
        BaCard,
        Dashboard,
        GMap,
    ],
    providers: [
        CategoryService,
        GroupService,
        ProjectService,
        KickstarterService,
        UserService,
        SystemService
    ]
})
export class DashboardModule {
}
