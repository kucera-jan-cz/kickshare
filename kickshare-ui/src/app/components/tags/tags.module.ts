import {NgModule} from '@angular/core';

import {NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap";
import {FormsModule} from "@angular/forms";
import {TagsComponent} from "./tags.component";
import {CommonModule} from "@angular/common";

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        NgbTypeaheadModule,
    ],
    exports:[TagsComponent],
    declarations: [TagsComponent],
    providers: []
})
export class TagsModule {
}
