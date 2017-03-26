import {Component} from "@angular/core";
@Component({
    selector: 'my-app',
    template: `
         <h1>{{title}}</h1>
         <a routerLink="/map">Map</a>
         <a routerLink="/gmap">Google</a>
         <a routerLink="/test">Test</a>
         <router-outlet></router-outlet>
       `
})
export class AppComponent {
    title = 'Tour of Heroes';
}