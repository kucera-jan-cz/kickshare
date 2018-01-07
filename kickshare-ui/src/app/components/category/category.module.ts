import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";

import {CategoryComponent} from "./category.component";
import {NgArrayPipesModule} from 'angular-pipes';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        NgArrayPipesModule
    ],
    declarations: [
        CategoryComponent
    ],
    exports: [
        CategoryComponent
    ]
})
export class CategoryModule {
}
