import {RouterModule, Routes} from "@angular/router";
import {UserSettings} from "./userSettings.component";

const routes: Routes = [
  {
    path: '',
    component: UserSettings
  }
];

export const routing = RouterModule.forChild(routes);
