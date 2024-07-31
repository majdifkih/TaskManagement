import {  Component, OnInit, SimpleChanges } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {
  CdkDragDrop,
  moveItemInArray,
  transferArrayItem,
  CdkDrag,
} from '@angular/cdk/drag-drop';
import { ProjectService } from '../../../services/projects/project-servcie.service';
import { SprintService } from '../../../services/sprint/sprint-service.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-backlog',
  templateUrl: './backlog.component.html',
  styleUrl: './backlog.component.css'
})
export class BacklogComponent implements OnInit {

  showModal: boolean = false;
  isOpen:boolean = false;
  openIndex: number | null = null;
  ProjectDescription: string = '';
  Sprints: any[] = [];
  projectId: number = 0;
  addSprintForm!: FormGroup;
  editSprintForm!: FormGroup;
  delId: number = 0;
  showAlert = false;
  currentSprintId : number = 0;
  
  showEdit: boolean = false;
  dropdowns: boolean = false;
  noResult:boolean = false;
  searchCriteria = {
    sprintName: '',
    endDate: '',
    status: ''
  };

  constructor(
    private route: ActivatedRoute,
     private projectService: ProjectService,
      private sprintService: SprintService,
      private fb: FormBuilder,
      private _toastr: ToastrService) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.projectId = id !== null ? +id : 0; 
    this.getProjectDescription();
    this.getAllSprints();
    this.addSprintForm = this.fb.group({
      sprintName: ['', [Validators.required]],
      sprintDescription: ['', [Validators.required]],
      startDate: ['', [Validators.required]],
      endDate: ['', [Validators.required]],
      project: [this.projectId],
    });
    this.editSprintForm = this.fb.group({
      sprintName: ['', [Validators.required]],
      sprintDescription: ['', [Validators.required]],
      startDate: ['', [Validators.required]],
      endDate: ['', [Validators.required]]
    });
  }
  
  
  getProjectDescription(): void {
    this.projectService.getProjectDetail(this.projectId).subscribe({
      next: (response: any) => {
        this.ProjectDescription = response.description;
        console.log(response);
      },
      error: (error) => {
        console.error('Error fetching project detail:', error);
      }
    });
  }

  //get all sprints by project
  getAllSprints(): void {
    this.sprintService.AllSprintByProject(this.projectId).subscribe({
      next: (response: any) => {
        this.Sprints=response;
        console.log(response);
      },
      error: (error) => {
        console.error('Error fetching sprints:', error);
      }
    });
  }
 
  
  addSprint() {
    if (this.addSprintForm.valid) {
      console.log("Form values before sending:", this.addSprintForm.value);
  
      this.sprintService.addSprint(this.addSprintForm.value).subscribe({
        next: (data: any) => {
          this._toastr.success('Sprint added successfully', 'Success');
          this.getAllSprints(); 
          this.closeModal();
        },
        error: (error) => {
          console.error('Error adding Sprint:', error);
          this._toastr.error('Error adding Sprint', 'Error');
        }
      });
    } else {
      console.error('Sprint form is invalid');
      this._toastr.error('Please fill out all required fields', 'Error');
    }
  }

  //update sprint function
  updateSprint(id: number) {
    if (this.editSprintForm.valid) {
        console.log("Form values before sending:", this.editSprintForm.value);

        // Correction de l'URL en utilisant l'ID du sprint
        this.sprintService.updateSprint(this.editSprintForm.value, id).subscribe({
            next: (data: any) => {
                this._toastr.success('Sprint updated successfully', 'Success');
                this.getAllSprints();
                this.closeModalEdit();
            },
            error: (error) => {
                console.error('Error updating sprint:', error);
                this._toastr.error('Error updating sprint', 'Error');
            }
        });
    } else {
        console.error('Sprint form is invalid');
        this._toastr.error('Please fill out all required fields', 'Error');
    }
}

  
  
  
  //delete sprint function
  deleteSprint(id: number) {
    this.sprintService.deleteSprint(id).subscribe({
      next: (response: any) => {
        this._toastr.success('Sprint deleted successfully', 'Success');
        this.getAllSprints();
      },
      error: (error) => {
        console.error('Error deleting sprint:', error);
        this._toastr.error('Error deleting sprint', 'Error');
      }
    });
  }





  drop(event: CdkDragDrop<any[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex,
      );
    }
  }

  evenPredicate(item: CdkDrag<number>) {
    return item.data % 2 === 0;
  }

  noReturnPredicate() {
    return false;
  }

 
  showDatePicker(event: Event): void {
    const input = event.target as HTMLInputElement;
    input.showPicker(); // Déclencher l'affichage du calendrier
  }
  toggleAccordion(index: number) {
    this.openIndex = this.openIndex === index ? null : index;
  }
  onAddSprint() {
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.addSprintForm.reset();
  }

  confirmationAlert(id: number) {
    this.showAlert = true;
    this.delId = id;
  }

  closeAlert() {
    this.showAlert = false;
  }

  confirmDelete() {
    this.deleteSprint(this.delId);
    this.closeAlert();
  }

  onModalEdit(sprint: any) {
    this.showEdit = true;
    this.currentSprintId = sprint.sprintId;
    this.editSprintForm.patchValue(sprint);
  }

  closeModalEdit() {
    this.showEdit = false;
    this.editSprintForm.reset();
  }

  ngOnChanges(changes: SimpleChanges) {
    if ('searchCriteria' in changes) {
      this.autoSearch();
    }
  }

  autoSearch(): void {
    const { sprintName, endDate, status } = this.searchCriteria;

    if (!sprintName && !endDate && !status) {
      this.getAllSprints();
    } else {
      this.search();
    }
  }

  search(): void {
    const { sprintName, endDate, status } = this.searchCriteria;
    this.sprintService.searchSprints(sprintName, endDate, status).subscribe({
      next: (data: any[]) => {
        this.Sprints = data;
        this.noResult = data.length === 0;
      },
      error: (error) => {
        if (error.status === 404) {
          this.noResult = true;
          this.Sprints = [];
        } 
        console.error('Error searching sprints:', error);
      }
    });
  }
  
  }
