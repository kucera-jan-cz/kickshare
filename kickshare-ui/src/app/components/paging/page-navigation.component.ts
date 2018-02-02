/**
 * Created by KuceraJan on 27.11.2017.
 */
import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from "@angular/core";
import {Page} from "app/services/domain";
import {LoggerFactory} from "../logger/loggerFactory.component";

@Component({
    selector: 'page-navigation',
    templateUrl: './page-navigation.html',
    styleUrls: ['./page-navigation.component.scss']

})
export class PageNavigationComponent implements OnChanges {

    private logger = LoggerFactory.getLogger("components:page:navigation");
    @Input() page: Page;
    @Output('value')
    pageEmitter: EventEmitter<Number> = new EventEmitter();
    pages: Array<Number>;
    initialized = false;


    ngOnChanges(changes: SimpleChanges): void {
        if (changes['page'] && changes['page'].currentValue) {
            this.page = changes['page'].currentValue;
        } else {
            return
        }
        const page: Page = this.page;
        let startPage: number, endPage: number;
        let total = page.totalPages;
        let current = page.number;
        if (total <= 10 || current <= 6) {
            // less than 10 total pages so show all
            // more than 10 total pages so calculate start and end pages
            startPage = 1;
            endPage = Math.min(total, 10);
        } else if (current + 4 >= total) {
            startPage = total - 9;
            endPage = total;
        } else {
            startPage = current - 5;
            endPage = current + 4;
        }
        this.logger.info("From: {0} To: {1}", startPage, endPage);
        this.pages = Array.from(new Array(endPage - startPage), (x, i) => i + startPage).reverse();
        this.initialized = true;
    }


    set(page: number) {
        this.logger.info("User requested page {0}", page);
        this.pageEmitter.emit(page);
    }
}
