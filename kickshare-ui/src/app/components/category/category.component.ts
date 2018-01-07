import {Component, EventEmitter, OnInit, Output} from "@angular/core";
import "rxjs/Rx";
import {Category} from "../../services/domain";
import {CategoryService} from "../../services/categories.service";
import {UserDefaults} from "../../constants/user.defaults";

@Component({
    selector: 'category',
    templateUrl: './category.component.html',
})
export class CategoryComponent implements OnInit {
    public initialized: boolean = false;
    selectedCategory: Category;
    categories: Category[];
    @Output() selected: EventEmitter<Category> = new EventEmitter();

    constructor(private categoryService: CategoryService) {
    }

    async ngOnInit() {
        this.categories = await this.categoryService.getCategories();
        this.categories.forEach(it => {
            var order = UserDefaults.DEFAULT_CATEGORY_PREFERENCES[it.id] || 0;
            it.order = it.order || order;
        });
        this.selectedCategory = this.categories.reduce((prev, current) => (prev.order > current.order) ? prev : current);
        this.selected.next(this.selectedCategory);
        this.initialized = true;
    }

    itemSelected(category: Category) {
        this.selected.next(category);
    }
}
