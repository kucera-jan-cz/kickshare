import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {LayoutComponent} from "./layout.component";

const routes: Routes = [
    {
        path: '', component: LayoutComponent,
        children: [
            {path: 'dashboard', loadChildren: '../pages/dashboard/dashboard.module#DashboardModule'},
            {path: 'blank-page', loadChildren: './blank-page/blank-page.module#BlankPageModule'},
            {path: 'account/group', loadChildren: '../pages/group_register/groupRegistration.module#GroupRegistrationModule'},
            {path: 'group', loadChildren: '../pages/group/group.module#GroupModule'},
            {path: 'cities', loadChildren: 'app/pages/cities/cities.module#CitiesModule'//, canActivate: [AuthGuardService]
            },
            {path: 'campaign', loadChildren: 'app/pages/campaign/campaign.module#CampaignModule'},
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class LayoutRoutingModule {
}
