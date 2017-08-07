import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";

import {routing} from "./group.routing";
import {GroupService} from "../../services/group.service";
import {ProjectService} from "../../services/project.service";
import {Group} from "./group.component";
import {Tabs} from "../../components/tabs/tabs.component";
import {Tab} from "../../components/tabs/tab.component";
import {GroupMembers} from "./members/group_members.component";
import {GroupDiscussion} from "./discussion/group_discussion.component";
import {NgbTabsetModule} from "@ng-bootstrap/ng-bootstrap";
/**
 * Created by KuceraJan on 9.4.2017.
 */
@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        routing,
        NgbTabsetModule.forRoot()
    ],

    declarations: [
        Group,
        Tabs,
        Tab,
        GroupMembers,
        GroupDiscussion
    ],
    providers: [
        ProjectService,
        GroupService
    ]
})
export class GroupModule {

}
