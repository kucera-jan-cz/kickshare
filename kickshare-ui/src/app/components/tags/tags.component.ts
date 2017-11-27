/**
 * Created by KuceraJan on 27.11.2017.
 */
import {Component, Input} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {NgbTypeaheadSelectItemEvent} from "@ng-bootstrap/ng-bootstrap";
import {Tag} from "../../services/domain";

@Component({
    selector: 'tags',
    templateUrl: './tags.html',
    styleUrls: ['./tags.component.scss']

})
export class TagsComponent {
    @Input('category') categoryId: number;
    public tags: Set<Tag> = new Set();

    tagFormatter = (tag: Tag) => tag.name;

    searchTagFunction = (text: Observable<string>) => {
        const tagsObservable: Observable<Tag[]> = text
            .debounceTime(250)
            .distinctUntilChanged()
            .map(term => [new Tag(1, "Action"), new Tag(2, "Sci-fi"), new Tag(3, "Wargame"), new Tag(4, "Area Control")]);
        return tagsObservable;
    };

    tagSelected(value: NgbTypeaheadSelectItemEvent, input: any) {
        value.preventDefault();
        const selectedTag: Tag = value.item;
        this.tags.add(selectedTag);
        input.value = '';
    }

    removeTag(tag: Tag) {
        this.tags.delete(tag);
    }
}
