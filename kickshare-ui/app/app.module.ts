import {NgModule} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {BrowserModule} from "@angular/platform-browser";
import {AgmCoreModule} from "angular2-google-maps/core";
import {App} from "./app.component";
@NgModule({
    imports: [
        AgmCoreModule.forRoot({
            apiKey: "AIzaSyA-NAJu2diDDRzMKz9jKTIj6HVXODMjXpk",
            libraries: ["places"]
        }),
        BrowserModule,
        FormsModule,
        ReactiveFormsModule
    ],
    declarations: [ App ],
    bootstrap: [ App ]
})
export class AppModule {}