import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";

import {Dashboard} from "./dashboard.component";
import {routing} from "./dashboard.routing";
// import {PopularApp} from "./popularApp";
// import {PieChart} from "./pieChart";
// import {TrafficChart} from "./trafficChart";
// import {UsersMap} from "./usersMap";
// import {LineChart} from "./lineChart";
// import {Feed} from "./feed";
// import {Todo} from "./todo";
// import {Calendar} from "./calendar";
// import {CalendarService} from "./calendar/calendar.service";
// import {FeedService} from "./feed/feed.service";
// import {LineChartService} from "./lineChart/lineChart.service";
// import {PieChartService} from "./pieChart/pieChart.service";
// import {TodoService} from "./todo/todo.service";
// import {TrafficChartService} from "./trafficChart/trafficChart.service";
// import {UsersMapService} from "./usersMap/usersMap.service";
// import {HoverTable} from "../tables/components/basicTables/components/hoverTable/hoverTable.component";
// import {BasicTablesService} from "../tables/components/basicTables/basicTables.service";
// import {BorderedTable} from "../tables/components/basicTables/components/borderedTable/borderedTable.component";
import {GMap} from "./gmap/gmap.component";
import {CategoryService} from "../../services/categories.service";
import {GroupService} from "../../services/group.service";
import {HttpModule} from "@angular/http";
import {ProjectService} from "../../services/project.service";
import {KickstarterService} from "../../services/kickstarter.service";
import {Ng2CompleterModule} from "ng2-completer";
import {BaCard} from "../../components/baCard/baCard.component";
import {UserService} from "../../services/user.service";
import {SystemService} from "../../services/system.service";

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        // AppTranslationModule,
        // NgaModule,
        routing,
        HttpModule,
        Ng2CompleterModule
        // CampaignModule
    ],
    declarations: [
        BaCard,
        // PopularApp,
        // PieChart,
        // TrafficChart,
        // UsersMap,
        // LineChart,
        // Feed,
        // Todo,
        // Calendar,
        Dashboard,
        // HoverTable,
        // BorderedTable,
        GMap,
        // Maps,
        // GoogleMaps,
    ],
    providers: [
        // CalendarService,
        // FeedService,
        // LineChartService,
        // PieChartService,
        // TodoService,
        // TrafficChartService,
        // UsersMapService,
        // BasicTablesService,
        CategoryService,
        GroupService,
        ProjectService,
        KickstarterService,
        UserService,
        SystemService
    ]
})
export class DashboardModule {
}
