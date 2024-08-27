import { Component, OnInit } from '@angular/core';
import { ProfilService } from '../../services/profil/profil.service';
import { ToastrService } from 'ngx-toastr';
import { jwtDecode } from 'jwt-decode';
import { AuthService } from '../../services/auth/auth-service.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profil',
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.css'] // Corrigez le nom de la propriété ici
})
export class ProfilComponent implements OnInit {
  userInfo: any; 
  edituserInfo!: FormGroup;
  role: string = '';
  roleForEdit: string = '';
  isEditing = false;
  showOldPassword = false;
  showNewPassword = false;
  constructor(private router: Router,
    private fb: FormBuilder, private profilService: ProfilService, private authService: AuthService, private _toastr: ToastrService) {}

  ngOnInit(): void {
    this.edituserInfo = this.fb.group({
      username: [{ value: '', disabled: true }, [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      oldPassword: ['', [Validators.required]],
      newPassword: [''],
    });
    
    this.edituserInfo.get('username')?.disable();

    
    this.userDetail();
  }

  userDetail(): void {
    this.profilService.getUserDetail().subscribe({
      next: (response: any) => {
        this.userInfo = response;
        this.role = response.role;
        this.edituserInfo.patchValue({
          username: this.userInfo.username,
          email: this.userInfo.email
        });
        this.roleForEdit = this.getRoleTitle();
      },
      error: (error) => {
        console.error('Error fetching user:', error);
      }
    });
  }

  getRoleTitle(): string {
    if (this.role === 'ROLE_ADMIN') {
      return 'Project Manager';
    } else if (this.role === 'ROLE_EMPLOYEE') {
      return 'Developer';
    } else {
      return 'Unknown Role';
    }
  }

  togglePasswordVisibility(field: string): void {
    if (field === 'oldPassword') {
      this.showOldPassword = !this.showOldPassword;
    } else if (field === 'newPassword') {
      this.showNewPassword = !this.showNewPassword;
    }
  }
  
  toggleEditMode(): void {
    this.isEditing = !this.isEditing;
    this.edituserInfo.get('oldPassword')?.reset();
  }
  

  updateProfile(): void {
    this.edituserInfo.markAllAsTouched();
  
    if (this.edituserInfo.valid) {
      if (this.edituserInfo.get('oldPassword')?.invalid) {
        console.error('Old Password is required');
        return; // Arrêter la soumission si oldPassword est invalide
      }
  
      this.profilService.updateProfile(this.edituserInfo.value).subscribe({
        next: (response: any) => {
          console.log('User updated successfully:', response);
          this._toastr.success('Profile updated successfully');
          this.isEditing = false;
  
          // Réinitialiser les champs oldPassword et newPassword
          this.edituserInfo.get('oldPassword')?.reset();
          this.edituserInfo.get('newPassword')?.reset();
        },
        error: (error) => {
          if (error.message === 'Old password is incorrect') {
            this._toastr.error('Old password is incorrect. Please try again.');
          } else {
            this._toastr.error('Error updating profile');
          }
  
          // Réinitialiser les champs oldPassword et newPassword en cas d'erreur
          this.edituserInfo.get('oldPassword')?.reset();
          this.edituserInfo.get('newPassword')?.reset();
        }
      });
    } else {
      console.log('Form is invalid. Please check the form fields.');
    }
  }
  
  
  
}