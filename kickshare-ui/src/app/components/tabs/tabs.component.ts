/**
 * Created by KuceraJan on 28.6.2017.
 */
import {AfterContentInit, Component, ContentChildren, QueryList} from "@angular/core";
import {Tab} from "./tab.component";

@Component({
  selector: 'tabs',
  template: `
    <ul class="nav nav-tabs">
      <li *ngFor="let tab of tabs" (click)="selectTab(tab)" [class.active]="tab.active">
        <a href="javascript:void(0)">{{tab.title}}
          <span class="round-tabs one">
                              <i class="glyphicon glyphicon-home"></i>
                      </span>
        </a>
      </li>
    </ul>
    <ng-content></ng-content>
  `,
  styles: [`
    span.round-tabs{
      width: 70px;
      height: 70px;
      line-height: 70px;
      display: inline-block;
      border-radius: 100px;
      background: white;
      z-index: 2;
      position: absolute;
      left: 0;
      text-align: center;
      font-size: 25px;
    }

    span.round-tabs.one{
      color: rgb(34, 194, 34);border: 2px solid rgb(34, 194, 34);
    }
  `]
})
export class Tabs implements AfterContentInit {

  @ContentChildren(Tab) tabs: QueryList<Tab>;

  // contentChildren are set
  ngAfterContentInit() {
    // get all active tabs
    let activeTabs = this.tabs.filter((tab) => tab.active);

    // if there is no active tab set, activate the first
    if (activeTabs.length === 0) {
      this.selectTab(this.tabs.first);
    }
  }

  selectTab(tab: Tab) {
    // deactivate all tabs
    this.tabs.toArray().forEach(tab => tab.active = false);

    // activate the tab the user has clicked on.
    tab.active = true;
  }

}
