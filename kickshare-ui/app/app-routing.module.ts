import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {Map} from "./map.component";
import {GMap} from "./gmap.component";
import {AppComponent} from "./app.component";
import {Test} from "./test.component";


const routes: Routes = [
    {path: '', redirectTo: '/my-app', pathMatch: 'full'},
    {path: 'my-app', component: AppComponent},
    {path: 'map', component: Map},
    {path: 'gmap', component: GMap},
    {path: 'test', component: Test}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}