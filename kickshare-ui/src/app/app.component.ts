import {Component, OnInit} from "@angular/core";
import {LoggerFactory} from "./components/logger/loggerFactory.component";

@Component({
    selector: 'app-root',
    //@TODO - maybe place <router-outlet></router-outlet> here since it's trivial
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
    ngOnInit(): void {
        LoggerFactory.initialize();
    }
}
