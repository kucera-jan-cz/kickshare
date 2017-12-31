import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {LayoutComponent} from "./layout.component";
import {AuthGuardService} from "../services/auth-guard.service";
import {FaqComponent} from "../pages/faq/faq.component";
import {FaqModule} from "../pages/faq/faq.module";

const routes: Routes = [
    {
        path: '', component: LayoutComponent,
        children: [
            {path: 'dashboard', loadChildren: '../pages/dashboard/dashboard.module#DashboardModule'},
            {path: 'blank-page', loadChildren: '../pages/blank-page/blank-page.module#BlankPageModule'},
            {path: 'account/group', loadChildren: '../pages/group_register/groupRegistration.module#GroupRegistrationModule', canActivate: [AuthGuardService]},
            {path: 'group', loadChildren: '../pages/group/group.module#GroupModule'},
            {path: 'cities', loadChildren: 'app/pages/cities/cities.module#CitiesModule', canActivate: [AuthGuardService]
            },
            {path: 'campaign', loadChildren: 'app/pages/campaign/campaign.module#CampaignModule'},
            {path: 'login', loadChildren: 'app/pages/login/login.module#LoginModule'},
            {path: 'profile', loadChildren: 'app/pages/profile/profile.module#ProfileModule', canActivate: [AuthGuardService]},
            {path: 'faq', component: FaqComponent},
            {path: 'sign-in', loadChildren: 'app/pages/user_register/userRegistration.module#UserRegistrationModule'},
            {path: 'user/settings', loadChildren: 'app/pages/user/userSettings.module#UserSettingsModule'},
        ]
    }
];

@NgModule({
    imports: [
        FaqModule,
        RouterModule.forChild(routes)],
    exports: [RouterModule]
})

//@TODO - Rewrite this to routing module, it has nothing to do with layout
export class LayoutRoutingModule {
}
