import { Component } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'] // Correction: utilisez "styleUrls" au lieu de "styleUrl"
})
export class HeaderComponent {
  showNotification: boolean = false;
  dropdowns: boolean = false;

  onNotification() {
    this.showNotification = !this.showNotification;
    this.dropdowns = true;
  }

  closeNotification() {
    this.showNotification = false;
  }


  closeDropdowns(event: Event): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.dropdown')) { 
      this.dropdowns = false;
    }
  }
  

  clickOutside(event: Event): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.notification') && this.showNotification) {
      this.closeNotification();
    }
  }
}
