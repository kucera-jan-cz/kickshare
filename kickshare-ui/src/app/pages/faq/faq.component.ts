import {Component, OnInit} from '@angular/core';
import {LoggerFactory} from "../../components/logger/loggerFactory.component";

@Component({
    selector: 'app-faq',
    templateUrl: './faq.component.html',
    styleUrls: ['./faq.component.scss']
})
export class FaqComponent implements OnInit {
    private logger = LoggerFactory.getLogger("components:faq");

    ngOnInit() {
        this.logger.trace("Initializing FAQ component")
    }
}
