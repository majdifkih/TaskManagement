import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../services/projects/project-servcie.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import {jwtDecode} from 'jwt-decode';
@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css']
})
export class ProjectsComponent implements OnInit {
  projects: any[] = [];
  showModal = false;
  isEditMode = false;
  projectToEdit: any = null;
  addProjectForm!: FormGroup;
  currentProjectId: number = 0;
  delId: number = 0;
  showAlert = false;

  constructor(
    private projectService: ProjectService,
    private fb: FormBuilder,
    private _toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.getAllProjects();
    this.addProjectForm = this.fb.group({
      projectName: ['', [Validators.required]],
      description: ['', [Validators.required]],
      admin: []
    });
  }

  get f() { return this.addProjectForm.controls; }

  getAllProjects(): void {
    const userId = this.getUserId();
    this.projectService.AllProjectsByUser(userId).subscribe({
      next: (response: any) => {
        console.log(response);
        this.projects = response;
       
      },
      error: (error) => {
        console.error('Error fetching projects:', error);
      }
    });
  }

  getProjectDetail(id: number): void {
    this.projectService.getProjectDetail(id).subscribe({
      next: (response: any) => {
        console.log(response);
      },
      error: (error) => {
        console.error('Error fetching project detail:', error);
      }
    });
  }

  getUserId(){
    const accessToken = localStorage.getItem('accessToken'); 
    if (!accessToken) {
      return null; 
    }
  
    try {
      const decodedToken: any = jwtDecode(accessToken);
      return decodedToken.userId || null; 
    } catch (error) {
      console.error('Error decoding JWT:', error);
      return null;
    }
  }
  
  addProject() {
    if (this.addProjectForm.valid) {
        const userId = this.getUserId();
  
        if (userId !== null && userId > 0) {
            this.addProjectForm.patchValue({ admin: userId });
            console.log("Form values before sending:", this.addProjectForm.value);

            this.projectService.addProject(this.addProjectForm.value).subscribe({
                next: (data: any) => {
                    this._toastr.success('Project added successfully', 'Success');
                    this.getAllProjects(); 
                    this.closeModal();
                },
                error: (error) => {
                    
                    if (error.status === 400) {
                        this._toastr.error('The project name already exists, please choose another one.', 'Duplicate Project Name');
                    } else {
                        this._toastr.error('Error adding project', 'Error');
                    }
                }
            });
        } else {
            console.error('Invalid user ID retrieved from local storage');
            this._toastr.error('Cannot add project without valid user ID', 'Error');
        }
    } else {
        console.error('Project form is invalid');
        this._toastr.error('Please fill out all required fields', 'Error');
    }
}

  

  updateProject(id: number) {
    if (this.addProjectForm.valid) {
      this.projectService.updateProject(id, this.addProjectForm.value).subscribe({
        next: (data: any) => {
          this._toastr.success('Project updated successfully', 'Success');
          this.getAllProjects();
          this.closeModal();
        },
        error: (error) => { 
          if (error.status === 400)  {
          this._toastr.error('The project name already exists, please choose another one.', 'Duplicate Project Name');
      } else {
          this._toastr.error('Error adding project', 'Error');
      }
        }
      });
    }
  }

  onSubmit() {
    if (this.isEditMode) {
      this.updateProject(this.currentProjectId);

    } else {
      this.addProject();
    }
  }

  onDeleteProject(id: number) {
    this.projectService.deleteProject(id).subscribe({
      next: (data: any) => {
        this._toastr.success('Project deleted successfully', 'Success');
        this.getAllProjects();
        this.closeAlert();
      },
      error: (error) => {
        console.error('Error deleting project:', error);
        this._toastr.error('Error deleting project', 'Error');
      }
    });
  }
  


  

  onAddProject() {
    this.isEditMode = false;
    this.addProjectForm.reset();
    this.showModal = true;
  }

  onEditProject(projects: any) {
    console.log(projects);
    this.isEditMode = true;
    this.currentProjectId = projects.projectId;
    this.addProjectForm.patchValue(projects);
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  confirmationAlert(id: number) {
    this.showAlert = true;
    this.delId = id;
  }

  closeAlert() {
    this.showAlert = false;
  }

  confirmDelete() {
    this.onDeleteProject(this.delId);
  }
}
