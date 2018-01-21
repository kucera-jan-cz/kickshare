import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {LayoutComponent} from "./layout/layout.component";
import {FaqComponent} from "./pages/faq/faq.component";
import {AuthGuardService} from "./services/auth-guard.service";
import {FaqModule} from "./pages/faq/faq.module";
import {LayoutModule} from "./layout/layout.module";
import {CountryGuardService} from "./services/country-guard.service";
import {LandingPageComponent} from "./pages/landing-page/landing-page.component";
import {LandingPageModule} from "./pages/landing-page/landing-page.module";


const routes: Routes = [
            {path: 'dashboard', loadChildren: './pages/dashboard/dashboard.module#DashboardModule'},
            {path: 'blank-page', loadChildren: './pages/blank-page/blank-page.module#BlankPageModule'},
            {path: 'account/group', loadChildren: './pages/group_register/groupRegistration.module#GroupRegistrationModule', canActivate: [AuthGuardService]},
            {path: 'group', loadChildren: './pages/group/group.module#GroupModule'},
            {
                path: 'cities', loadChildren: './pages/cities/cities.module#CitiesModule', canActivate: [AuthGuardService]
            },
            {path: 'campaign', loadChildren: './pages/campaign/campaign.module#CampaignModule'},
            {path: 'login', loadChildren: './pages/login/login.module#LoginModule'},
            {path: 'profile', loadChildren: './pages/profile/profile.module#ProfileModule', canActivate: [AuthGuardService]},
            {path: 'faq', component: FaqComponent},
            {path: 'sign-in', loadChildren: './pages/user_register/userRegistration.module#UserRegistrationModule'},
            {path: 'user/settings', loadChildren: './pages/user/userSettings.module#UserSettingsModule'},
];

const countryRoutes: Routes = [
    {
        path: '',
        component: LandingPageComponent,
        canActivate: [CountryGuardService],
    },
    {
        path: 'cz',
        component: LayoutComponent,
        children: routes,
        canActivateChild: [CountryGuardService],
        runGuardsAndResolvers: 'always'
    },
];

@NgModule({
    imports: [
        FaqModule,
        LandingPageModule,
        LayoutModule,
        RouterModule.forRoot(countryRoutes),
    ],
    providers: [CountryGuardService],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
