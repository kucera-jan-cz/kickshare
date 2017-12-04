import {CommonModule} from "@angular/common";
import {NgModule} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {routing} from "./userSettings.routing";
import {UserSettings} from "./userSettings.component";
import {CategoryService} from "../../services/categories.service";

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        routing,
    ],
    declarations: [
        UserSettings,
    ],
    providers: [
        CategoryService
    ]
})
export class UserSettingsModule {
}
