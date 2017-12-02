import {Component, OnInit} from "@angular/core";
import {LoggerFactory} from "./components/logger/loggerFactory.component";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
    ngOnInit(): void {
        LoggerFactory.initialize();
    }
}
