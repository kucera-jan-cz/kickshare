import { Component } from '@angular/core';

@Component({
    selector: 'hello-world',
    // template: '<h1>Hello {{name}}!</h1>'
    templateUrl: 'app/map.component.html'
})
export class HelloWorldComponent {
    private name = "World";
}