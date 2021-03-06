import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from "@angular/core";
import {NavigationEnd, Router} from "@angular/router";
import {Observable} from "rxjs";
import "rxjs/Rx";
import {UserService} from "../../services/user.service";
import {Notification} from "../../services/domain";
import {AuthHttp} from "../../services/auth-http.service";
import {SystemService} from "../../services/system.service";
import {UrlService} from "../../services/url.service";
import {Subscription} from "rxjs/Rx";

@Component({
    selector: 'application-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {
    displayNotification: boolean = false;
    @Input() sidebarCollapsed: boolean;
    @Output() toggleEvent: EventEmitter<boolean> = new EventEmitter();
    private routerSubscription: Subscription;
    private timer: Observable<number>;
    private alive: boolean = true;
    private notifications: Notification[] = [];
    country: string;
    backerId: number;

    constructor(private userService: UserService, private authHttp: AuthHttp, public router: Router, private system: SystemService, public url: UrlService) {
        this.timer = Observable.timer(0, 5000);
        this.routerSubscription = this.router.events.subscribe((val) => {
            if (val instanceof NavigationEnd && window.innerWidth <= 992) {
                this.toggleSidebar();
            }
        });
        this.country = this.system.getCountry().toLowerCase();
    }

    ngOnInit() {
        this.backerId = this.system.getId();
        //@TODO - figure out whether periodic checking is really needed
        // this.timer
        //     .takeWhile(() => this.alive)
        //     .subscribe(() => {
        //         this.checkoutNewNotifications();
        //     });
    }

    ngOnDestroy(): void {
        this.routerSubscription.unsubscribe();
        this.alive = false;
    }

    private async checkoutNewNotifications() {
        //@TODO - userId getResponse it??
        let backerId = 1;
        let notifications = await this.userService.getLatestNotifications(backerId);
        notifications.sort((a,b) => a.id - b.id);
        let notificationChange = this.notifications.filter(item => notifications.indexOf(item) < 0).length > 0;
        if(notificationChange) {
            this.notifications = notifications;
        }
    }

    toggleSidebar() {
        const dom: any = document.querySelector('body');
        dom.classList.toggle('left-sidebar');
        this.sidebarCollapsed = !this.sidebarCollapsed;
        this.toggleEvent.next(this.sidebarCollapsed);
    }

    onLoggedout() {
        this.authHttp.logout();
    }

    changeLang(language: string) {
    }
}
