import {RouterModule, Routes} from "@angular/router";
import {LoginComponent} from "./login.component";


// noinspection TypeScriptValidateTypes
const routes: Routes = [
  {
    path: '',
    component: LoginComponent
  }
];

export const routing = RouterModule.forChild(routes);
