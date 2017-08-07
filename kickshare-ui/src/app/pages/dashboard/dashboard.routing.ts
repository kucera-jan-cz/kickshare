import {RouterModule, Routes} from "@angular/router";

import {Dashboard} from "./dashboard.component";
import {ModuleWithProviders} from "@angular/core";
// import {Campaign} from "./campaign/campaign.component";

// noinspection TypeScriptValidateTypes
export const routes: Routes = [
  {
    path: '',
    component: Dashboard,
    children: [
      //{ path: 'treeview', component: TreeViewComponent }
      // {path: 'campaign', component: Campaign},
      ]
  },
];

export const routing: ModuleWithProviders = RouterModule.forChild(routes);
