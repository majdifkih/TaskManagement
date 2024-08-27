import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { AuthService } from '../../services/auth/auth-service.service';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css']
})
export class SideBarComponent implements OnInit {
  isDrawerOpen = false;
  isAdmin: boolean = false;
  showlogoutAlert: boolean = false;
  constructor(private router: Router,private authService:AuthService) {
    // Ecouter les événements de routage pour gérer l'état de la barre latérale
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.closeDrawer();
      }
    });
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
  toggleDrawer() {
    this.isDrawerOpen = !this.isDrawerOpen;
    this.updateDrawerState();
  }

  closeDrawer() {
    this.isDrawerOpen = false;
    this.updateDrawerState();
  }

  updateDrawerState() {
    const drawer = document.getElementById('drawer-navigation');
    const overlay = document.getElementById('drawer-overlay');
    if (drawer && overlay) {
      if (this.isDrawerOpen) {
        drawer.classList.remove('-translate-x-full');
        overlay.classList.add('show');
      } else {
        drawer.classList.add('-translate-x-full');
        overlay.classList.remove('show');
      }
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
