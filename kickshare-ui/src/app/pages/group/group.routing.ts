import {RouterModule, Routes} from "@angular/router";

import {ModuleWithProviders} from "@angular/core";
import {GroupComponent} from "./group.component";

export const routes: Routes = [
  {
    path: ':id',
    component: GroupComponent,
    children: [
    ]
  }
];

export const routing: ModuleWithProviders = RouterModule.forChild(routes);
