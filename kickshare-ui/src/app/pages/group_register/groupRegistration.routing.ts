import {RouterModule, Routes} from "@angular/router";
import {GroupRegistration} from "./groupRegistration.component";


// noinspection TypeScriptValidateTypes
const routes: Routes = [
  {
    path: '',
    component: GroupRegistration
  }
];

export const routing = RouterModule.forChild(routes);
