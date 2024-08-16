import { Component } from '@angular/core';
import { AuthService } from '../../services/auth/auth-service.service';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'] // Correction: utilisez "styleUrls" au lieu de "styleUrl"
})
export class HeaderComponent {
  showNotification: boolean = false;
  dropdowns: boolean = false;
  isAdmin: boolean = false;
  constructor(private authService:AuthService){
    
  }
  ngOnInit(): void {

    this.RoleCheck();
  }

  getUserRole(){
    const accessToken = localStorage.getItem('accessToken'); 
    if (!accessToken) {
      return null; 
    }
  
    try {
      const decodedToken: any = jwtDecode(accessToken);
      return decodedToken.role || null; 
    } catch (error) {
      console.error('Error decoding JWT:', error);
      return null;
    }
  }

  RoleCheck(): void {
    let role = this.getUserRole();
    this.isAdmin = role === 'ROLE_ADMIN';
  }

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
