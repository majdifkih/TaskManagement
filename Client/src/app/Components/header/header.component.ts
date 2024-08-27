import { Component } from '@angular/core';
import { AuthService } from '../../services/auth/auth-service.service';
import { jwtDecode } from 'jwt-decode';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'] // Correction: utilisez "styleUrls" au lieu de "styleUrl"
})
export class HeaderComponent {
  showNotification: boolean = false;
  dropdowns: boolean = false;
  isAdmin: boolean = false;
  showlogoutAlert : boolean = false;
  constructor(private authService:AuthService, private router: Router){
    
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
    
  logout(){
    this.authService.logout().subscribe({
      next: (response: any) => {
        console.log(response);
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Error logging out:', error);
      }
    });
  }

  confirmationlogoutAlert() {
    this.showlogoutAlert = true;
  }

  closeAlert() {
    this.showlogoutAlert = false;
  }
}
