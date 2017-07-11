import {Component} from "@angular/core";

@Component({
  selector: 'app-root',
  template: `
      <div class="container">
          <div class="jumbotron">
              <h1>Welcome</h1>
              <h2>Angular & Bootstrap Demo</h2>
          </div>

          <div class="panel panel-primary">
              <div class="panel-heading">Status</div>
              <div class="panel-body">
                  <h3>{{title}}</h3>
              </div>
          </div>
      </div>
  `,
  styles: []
})
export class AppComponent {
  title = 'Kickshare prototype';
}