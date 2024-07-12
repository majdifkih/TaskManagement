import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthServiceService } from '../../services/auth-service.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{


  isSignDivVisiable: boolean  = true;
  signupForm!: FormGroup;
  signinForm!: FormGroup;

  constructor(private authService:AuthServiceService,private router:Router,private fb: FormBuilder,private _toastr: ToastrService){}

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
      if (this.signupForm.valid) {
        this.authService.checkUserExistence(this.signupForm.value).subscribe({
          next: (exists: boolean) => {
            if (exists) {
              this._toastr.warning("User already exists");
            } else {
              this.authService.signup(this.signupForm.value).subscribe({
                next: (data: any) => {
                  this._toastr.success('User registered successfully', 'Success');
                  this.router.navigate(['/login']);
                },
                error: (error) => {
                  console.error(error);
                }
              });
            }
          },
          error: (error) => {
            console.error(error);
          }
        });
      } else {
        this._toastr.error("Please fill out all fields correctly.");
      }
    }
  
  
onLogin() {
  if(this.signinForm.valid) {
    console.log(this.signinForm.value);
    this.authService.login(this.signinForm.value).subscribe({
      next: (data: any) => {
        this.router.navigate(['/']);
        console.log(data);
      },
      error: (error) => {
        console.log(error);
      }
    })
  }else {
    this._toastr.error("Please fill out all fields correctly.");
  }
}


}