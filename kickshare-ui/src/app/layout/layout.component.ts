import {Component} from "@angular/core";
import {Router} from "@angular/router";
import {LoggerFactory} from "../components/logger/loggerFactory.component";
import {SystemService} from "../services/system.service";

declare let EventSource: any;

@Component({
    selector: 'app-layout',
    templateUrl: './layout.component.html',
    styleUrls: ['./layout.component.scss']
})
export class LayoutComponent {
    private logger = LoggerFactory.getLogger("components:layout");
    public sidebarCollapsed = true;

    constructor(public router: Router, private system: SystemService) {
        this.logger.debug("Initializing layout component")
    }

    login() {
        const snapshot = this.router.routerState.snapshot;
        this.logger.info("Routing to login from {0}", snapshot.url);
        this.router.navigate(['/', this.system.countryCode.toLowerCase(), 'login'], {queryParams: {returnUrl: snapshot.url}});
        this.sidebarCollapsed = !this.sidebarCollapsed
    }

    togg() {
        const dom: any = document.querySelector('body');
        dom.classList.toggle('navbarToggleExternalContent');
    }

    toggleSidebar(sidebarCollapsed: boolean) {
        this.sidebarCollapsed = sidebarCollapsed;
        const dom: any = document.querySelector('body');
        dom.classList.toggle('left-sidebar');
    }
}
