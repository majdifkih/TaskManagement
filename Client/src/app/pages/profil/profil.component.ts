import { Component } from '@angular/core';
import { ProfilService } from '../../services/profil/profil.service';
import { FormBuilder } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-profil',
  templateUrl: './profil.component.html',
  styleUrl: './profil.component.css'
})
export class ProfilComponent {
  user: any[] = [];
  constructor(private profilService: ProfilService, 
    private fb: FormBuilder,
    private _toastr: ToastrService){}
  ngOnInit(): void {
    this.userDetail();
  }

//userDetail
userDetail(): void {
  this.profilService.getUserDetail().subscribe({
    next: (response: any) => {
      console.log(response);
      this.user = response;
     
    },
    error: (error) => {
      console.error('Error fetching user:', error);
    }
  });
}

}
