import {Component, OnInit} from "@angular/core";
import {LoggerFactory} from "./components/logger/loggerFactory.component";
import {SystemService} from "./services/system.service";

@Component({
    selector: 'app-root',
    template: '<router-outlet></router-outlet>',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
    ngOnInit(): void {
        console.info("Initializing Kickshare UI");
        LoggerFactory.initialize();
        SystemService.initializeGoogleMaps();
    }
}
