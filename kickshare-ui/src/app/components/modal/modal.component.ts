import {Component} from "@angular/core";
/**
 * Created by KuceraJan on 28.5.2017.
 */

@Component({
  selector: 'modal-window',
  templateUrl: './modal.html',
  styles: [`
    .modal-window {
      background: rgba(0,0,0,0.6);
      color: #222222;
    }
  `]
})

export class ModalComponent {

  public visible = false;
  private visibleAnimate = false;

  constructor(){}

  public show(): void {
    this.visible = true;
    setTimeout(() => this.visibleAnimate = true, 100);
  }

  public hide(): void {
    this.visibleAnimate = false;
    setTimeout(() => this.visible = false, 300);
  }

  public onContainerClicked(event: MouseEvent): void {
    if ((<HTMLElement>event.target).classList.contains('modal-window')) {
      this.hide();
    }
  }

}
