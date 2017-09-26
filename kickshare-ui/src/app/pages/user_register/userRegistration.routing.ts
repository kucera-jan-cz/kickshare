import {RouterModule, Routes} from "@angular/router";
import {UserRegistration} from "./userRegistration.component";

const routes: Routes = [
  {
    path: '',
    component: UserRegistration
  }
];

export const routing = RouterModule.forChild(routes);
