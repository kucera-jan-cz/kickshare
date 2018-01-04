import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {BrowserModule} from "@angular/platform-browser";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {AppRoutingModule} from "./app-routing.module";
import {AppComponent} from "./app.component";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {AuthGuardService} from "./services/auth-guard.service";
import {AuthHttp} from "./services/auth-http.service";
import {OAuthModule} from "angular-oauth2-oidc";
import {OauthHttp} from "./services/auth-oauth.service";
import {HttpClientJsonpModule, HttpClientModule} from "@angular/common/http";
import {UrlService} from "./services/url.service";
import {SystemService} from "./services/system.service";
import {HttpModule, JsonpModule} from "@angular/http/http";

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        HttpClientModule,
        HttpClientJsonpModule,
        AppRoutingModule,
        NgbModule.forRoot(),
        OAuthModule.forRoot(),
    ],
    providers : [
        SystemService,
        UrlService,
        AuthGuardService,
        // {provide: AuthHttp, useClass: BasicAuthHttp}
        {provide: AuthHttp, useClass: OauthHttp}
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
