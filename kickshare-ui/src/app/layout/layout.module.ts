import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {NgbAlertModule, NgbCollapseModule, NgbDropdownModule} from "@ng-bootstrap/ng-bootstrap";

import {LayoutRoutingModule} from "./layout-routing.module";
import {LayoutComponent} from "./layout.component";
import {HeaderComponent, SidebarComponent} from "../components/";
import {UserService} from "../services/user.service";
import {AuthGuardService} from "../services/auth-guard.service";
import {SystemService} from "../services/system.service";

@NgModule({
    imports: [
        CommonModule,
        NgbDropdownModule.forRoot(),
        LayoutRoutingModule,
        NgbCollapseModule.forRoot(),
        NgbAlertModule.forRoot()
    ],
    declarations: [
        LayoutComponent,
        HeaderComponent,
        SidebarComponent,
    ],
    providers: [
        UserService,
        AuthGuardService,
        SystemService
    ]
})
export class LayoutModule { }
