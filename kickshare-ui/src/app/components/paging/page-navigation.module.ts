import {NgModule} from '@angular/core';
import {CommonModule} from "@angular/common";
import {PageNavigationComponent} from "./page-navigation.component";

@NgModule({
    imports: [
        CommonModule,
    ],
    exports:[PageNavigationComponent],
    declarations: [PageNavigationComponent],
    providers: []
})
export class PageNavigationModule {
}
