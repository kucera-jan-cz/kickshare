import {NgModule} from "@angular/core";
import {GroupService} from "../../services/group.service";
import {ProfileComponent} from "./profile.component";
import {UserService} from "../../services/user.service";
import {SystemService} from "../../services/system.service";
import {CommonModule} from "@angular/common/common";
import {routing} from "./profile.routing";

/**
 * Created by KuceraJan on 9.4.2017.
 */
@NgModule({
    imports: [
        CommonModule,
        routing
    ],

    declarations: [
        ProfileComponent
    ],
    providers: [
        GroupService,
        UserService,
        SystemService
    ]
})
export class ProfileModule {

}
