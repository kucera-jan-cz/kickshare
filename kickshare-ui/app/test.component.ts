import {Component} from "@angular/core";

@Component({
    selector: 'test',
    templateUrl: 'app/test.component.html'
})
export class Test {
    private index = 1;

    increment() {
        this.index = this.index + 1;
    }
}