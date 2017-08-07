import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
declare let EventSource: any;
@Component({
    selector: 'app-layout',
    templateUrl: './layout.component.html',
    styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit {
    public isNavbarCollapsed = true;

    constructor(public router: Router) {
    }

    ngOnInit() {
        if (this.router.url === '/') {
            this.router.navigate(['/blank-page']);
        }
        var params = {headers: {
            Authorization: 'Basic eGF0cml4MTAxQGdtYWlsLmNvbTp1c2Vy',
            Accept : 'application/json',
            Cookie: 'test=test'}
        };
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
