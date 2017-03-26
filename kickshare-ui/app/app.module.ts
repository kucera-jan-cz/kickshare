import {NgModule} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {BrowserModule} from "@angular/platform-browser";
import {AgmCoreModule} from "angular2-google-maps/core";
import {Map} from "./map.component";
import {AppRoutingModule} from "./app-routing.module";
import {GMap} from "./gmap.component";
import {AppComponent} from "./app.component";
import {Test} from "./test.component";
@NgModule({
    imports: [
        AppRoutingModule,
        AgmCoreModule.forRoot({
            apiKey: "AIzaSyA-NAJu2diDDRzMKz9jKTIj6HVXODMjXpk",
            libraries: ["places"]
        }),
        BrowserModule,
        FormsModule,
        ReactiveFormsModule
    ],
    declarations: [
        AppComponent,
        Map,
        GMap,
    Test],
    bootstrap: [AppComponent]
})
export class AppModule {
}