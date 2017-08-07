import {RouterModule, Routes} from "@angular/router";

import {ModuleWithProviders} from "@angular/core";
import {Cities} from "./citites.component";

// noinspection TypeScriptValidateTypes
export const routes: Routes = [
  {
    path: '',
    component: Cities,
    children: []
  }
];

export const routing: ModuleWithProviders = RouterModule.forChild(routes);
