import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {LoggerFactory} from "../components/logger/loggerFactory.component";
import {SystemService} from "../services/system.service";

declare let EventSource: any;

@Component({
    selector: 'app-layout',
    templateUrl: './layout.component.html',
    styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit {
    private logger = LoggerFactory.getLogger("components:layout");
    public isNavbarCollapsed = true;

    constructor(public router: Router, private system: SystemService) {
    }

    ngOnInit() {
        if (this.router.url === '/') {
            this.router.navigate(['/blank-page']);
        }
    }

    login() {
        const snapshot = this.router.routerState.snapshot;
        this.logger.info("Routing to login from {0}", snapshot.url);
        this.router.navigate(['/', this.system.countryCode.toLowerCase(), 'login'], {queryParams: {returnUrl: snapshot.url}});
        this.isNavbarCollapsed = !this.isNavbarCollapsed
    }

    togg() {
        const dom: any = document.querySelector('body');
        dom.classList.toggle('navbarToggleExternalContent');
    }

    toggleSidebar(navbarCollapsed: boolean) {
        this.isNavbarCollapsed = navbarCollapsed;
        const dom: any = document.querySelector('body');
        dom.classList.toggle('left-sidebar');
    }
}
