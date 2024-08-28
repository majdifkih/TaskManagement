import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth/auth-service.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{


  isSignDivVisiable: boolean  = true;
  signupForm!: FormGroup;
  signinForm!: FormGroup;
  showPassword = false;
  showSigninPassword = false;
  constructor(private authService:AuthService,private router:Router,private fb: FormBuilder,private _toastr: ToastrService){}

  ngOnInit(): void {
    this.signupForm = this.fb.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      role: ['', [Validators.required]]
    });
    this.signinForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
    }
    
     get f() { return this.signupForm.controls; }
     get f2() { return this.signinForm.controls; }


     onRegister() {
      this.signupForm.markAllAsTouched();
      if (this.signupForm.valid) {
        this.authService.signup(this.signupForm.value).subscribe({
          next: (data: any) => {
            this._toastr.success('User registered successfully', 'Success');
            this.router.navigate(['/login']);
          },
          error: (error) => {
            if (error.meassge='Error: Username is already taken!') {
              this._toastr.error('User already exists', 'Error');
                console.error(error);
            }else if (error.message='Error: Email is already in use!') {
              this._toastr.error('Email already exists', 'Error');
              console.error(error);
            }
              
          
          }
        });
      }
    }
    
  
  
onLogin() {
  this.signinForm.markAllAsTouched();
  if(this.signinForm.valid) {
    console.log(this.signinForm.value);
    this.authService.login(this.signinForm.value).subscribe({
      next: (data: any) => {
        this.router.navigate(['/dash']);
        console.log(data);
      },
      error: (error) => {
        console.log(error);
      }
    })
  }
}



togglePasswordVisibility(): void {
  this.showPassword = !this.showPassword;
}

toggleSigninPasswordVisibility(): void {
  this.showSigninPassword = !this.showSigninPassword;
}

}