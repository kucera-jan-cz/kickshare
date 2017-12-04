import {Component, OnInit} from "@angular/core";
import {CategoryService} from "../../services/categories.service";
import {Category} from "../../services/domain";
import {LoggerFactory} from "../../components/logger/loggerFactory.component";

@Component({
    selector: 'user-settings',
    templateUrl: './userSettings.html',
    styleUrls: ['./userSettings.scss']
})
export class UserSettings implements OnInit {
    private logger = LoggerFactory.getLogger("components:user:settings");
    private categories:Category[];
    constructor(private categoryService: CategoryService) {

    }
    async ngOnInit() {
        this.categories = await this.categoryService.getCategories();
    }
}
