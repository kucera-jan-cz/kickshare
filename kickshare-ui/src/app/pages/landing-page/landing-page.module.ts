import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {LandingPageComponent} from './landing-page.component';
import {CountryService} from "../../services/country.service";
import {FormsModule} from "@angular/forms";

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
    ],
    declarations: [LandingPageComponent],
    exports: [LandingPageComponent],
    providers: [CountryService]
})
export class LandingPageModule {
}
