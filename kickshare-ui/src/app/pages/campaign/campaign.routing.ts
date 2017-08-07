import {RouterModule, Routes} from "@angular/router";

import {ModuleWithProviders} from "@angular/core";
import {Campaign} from "./campaign.component";

export const routes: Routes = [
  {
    path: ':id',
    component: Campaign,
    children: [
    ]
  }
];

export const routing: ModuleWithProviders = RouterModule.forChild(routes);
