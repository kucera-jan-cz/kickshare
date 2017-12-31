import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {FaqComponent} from './faq.component';
import {NgbAccordionModule} from "@ng-bootstrap/ng-bootstrap";

@NgModule({
    imports: [
        CommonModule,
        NgbAccordionModule
    ],
    exports: [FaqComponent],
    declarations: [FaqComponent]
})
export class FaqModule {
}
