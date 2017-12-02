import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";

const routes: Routes = [
    {
        path: '',
        redirectTo: '/cz/blank-page',
        pathMatch: 'full'
    },
    {
        path: 'cz',
        loadChildren: './layout/layout.module#LayoutModule',
    },
    {
        path: 'sk',
        loadChildren: './layout/layout.module#LayoutModule',
    },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
