import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {NgbCollapseModule, NgbDropdownModule} from "@ng-bootstrap/ng-bootstrap";

import {LayoutRoutingModule} from "./layout-routing.module";
import {LayoutComponent} from "./layout.component";
import {HeaderComponent, SidebarComponent} from "../components/";
import {UserService} from "../services/user.service";

@NgModule({
    imports: [
        CommonModule,
        NgbDropdownModule.forRoot(),
        LayoutRoutingModule,
        NgbCollapseModule.forRoot()
    ],
    declarations: [
        LayoutComponent,
        HeaderComponent,
        SidebarComponent,
    ],
    providers: [
        UserService
    ]
})
export class LayoutModule { }
