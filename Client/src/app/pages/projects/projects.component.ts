import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../services/projects/project-servcie.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

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
  dropdowns: { [key: number]: boolean } = {};
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
    });
  }

  get f() { return this.addProjectForm.controls; }

  getAllProjects(): void {
    this.projectService.getAllProjects().subscribe({
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

  addProject() {
    if (this.addProjectForm.valid) {
      this.projectService.addProject(this.addProjectForm.value).subscribe({
        next: (data: any) => {
          this._toastr.success('Project added successfully', 'Success');
          this.getAllProjects();
          this.closeModal();
        },
        error: (error) => {
          console.error('Error adding project:', error);
          this._toastr.error('Error adding project', 'Error');
        }
      });
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
          console.error('Error updating project:', error);
          this._toastr.error('Error updating project', 'Error');
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

  toggleDropdown(event: Event, id: number): void {
    event.stopPropagation();
    this.dropdowns[id] = !this.dropdowns[id];
  }

  closeDropdowns(event: Event): void {
    for (const key in this.dropdowns) {
      if (this.dropdowns.hasOwnProperty(key)) {
        this.dropdowns[key] = false;
      }
    }
  }

  onAddProject() {
    this.isEditMode = false;
    this.addProjectForm.reset();
    this.showModal = true;
  }

  onEditProject(project: any) {
    this.isEditMode = true;
    this.currentProjectId = project.id;
    this.addProjectForm.patchValue(project);
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
