import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {HttpModule, JsonpModule} from "@angular/http";
import {BrowserModule} from "@angular/platform-browser";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {AppRoutingModule} from "./app-routing.module";
import {AppComponent} from "./app.component";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {AuthGuardService} from "./services/auth-guard.service";
import {BasicAuthHttp} from "./services/auth-basic.service";
import {AuthHttp} from "./services/auth-http.service";
import {OAuthModule} from "angular-oauth2-oidc";

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        HttpModule,
        AppRoutingModule,
        NgbModule.forRoot(),
        OAuthModule.forRoot(),
        JsonpModule
    ],
    providers : [
        AuthGuardService,
        {provide: AuthHttp, useClass: BasicAuthHttp}
        // {provide: AuthHttp, useClass: OauthHttp}
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
