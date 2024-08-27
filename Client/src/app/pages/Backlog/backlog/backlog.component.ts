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
import { jwtDecode } from 'jwt-decode';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-backlog',
  templateUrl: './backlog.component.html',
  styleUrl: './backlog.component.css'
})
export class BacklogComponent implements OnInit {

  showModal: boolean = false;
  openIndex: number | null = null;
  ProjectDescription: string = '';
  Sprints: any[] = [];
  SprintsOrder : any[] = [];
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

  isAdmin: boolean = false;

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
      sprintDescription: [''],
      startDate: ['', [Validators.required]],
      endDate: ['', [Validators.required]],
      projectId: [this.projectId],
    });
    this.editSprintForm = this.fb.group({
      sprintName: [''],
      sprintDescription: [''],
      startDate: [''],
      endDate: ['']
    });    
   this.GetAdmin();
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

  GetAdmin(){
    let role=this.getUserRole();
    if (role === 'ROLE_ADMIN') {
      this.isAdmin = true;
    } else {
      this.isAdmin = false;
    }
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
        this.sortSprintsByPriority();
      },
      error: (error) => {
        console.error('Error fetching sprints:', error);
      }
    });
  }
  sortSprintsByPriority() {
    this.Sprints.sort((a, b) => a.priority - b.priority);
  }
 
  get f() { return this.addSprintForm.controls; }
  get f2() { return this.editSprintForm.controls; }
  addSprint() {
    // Marquer tous les champs comme touchés pour afficher les erreurs de validation
    this.addSprintForm.markAllAsTouched();
  
    // Vérifier si le formulaire est valide
    if (this.addSprintForm.valid) {
        // Extraire les valeurs du formulaire
        const sprintData = this.addSprintForm.value;
        console.log("Form values before sending:", sprintData);
  
        // Appeler le service pour ajouter le sprint
        this.sprintService.addSprint(sprintData).subscribe({
            next: (data: any) => {
                // Afficher un message de succès et rafraîchir les données
                this._toastr.success('Sprint added successfully', 'Success');
                this.getAllSprints(); 
                this.closeModal();
            },
            error: (error) => {
                // Vérifier et afficher les messages d'erreur spécifiques
                let errorMessage: string;

                // Accéder au message d'erreur depuis l'objet d'erreur
                const errorResponse = error.error as { message: string };

                // Assigner les messages d'erreur en fonction des codes d'erreur ou des messages
                switch (errorResponse?.message) {
                    case "SprintName already exists":
                        errorMessage = 'Sprint Name already exists';
                        break;
                    case "Start date and end date must be provided":
                        errorMessage = 'Start date and end date must be provided';
                        break;
                    case "End date must be after start date":
                        errorMessage = 'End date must be after start date';
                        break;
                    case "Start date must be today or in the future":
                        errorMessage = 'Start date must be today or in the future';
                        break;
                    default:
                        errorMessage = 'An unknown error occurred';
                        break;
                }

                // Afficher le message d'erreur
                this._toastr.error(errorMessage, 'Error');
            }
        });
    } else {
        // Afficher un message d'erreur générique si le formulaire est invalide
        this._toastr.error('Please fill in all required fields correctly', 'Error');
    }
}


  //update sprint function
  updateSprint(id: number) {
    this.editSprintForm.markAllAsTouched();

    const sprintUpdate: any = {};

    if (this.editSprintForm.get('sprintName')?.dirty) {
        const sprintName = this.editSprintForm.get('sprintName')?.value;
        if (sprintName) {
            sprintUpdate.sprintName = sprintName;
        }
    }
    if (this.editSprintForm.get('startDate')?.dirty) {
        sprintUpdate.startDate = this.editSprintForm.get('startDate')?.value;
    }
    if (this.editSprintForm.get('endDate')?.dirty) {
        sprintUpdate.endDate = this.editSprintForm.get('endDate')?.value;
    }
    if (this.editSprintForm.get('sprintDescription')?.dirty) {
        sprintUpdate.sprintDescription = this.editSprintForm.get('sprintDescription')?.value;
    }

    if (Object.keys(sprintUpdate).length > 0) {
        console.log("Form values before sending:", sprintUpdate);

        this.sprintService.updateSprint(sprintUpdate, id).subscribe({
            next: (data: any) => {
                this._toastr.success('Sprint updated successfully', 'Success');
                this.getAllSprints();
                this.closeModalEdit();
            },
            error: (error) => {
              if(error.message="SprintName already exists"){
                this._toastr.error('Sprint Name already exists', 'Error');
              }
               
            }
        });
    } else {
        console.error('No changes detected');
        this._toastr.error('No changes detected', 'Error');
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
    const movedSprint = event.item.data;
  
    if (!movedSprint) {
      console.error('Sprint item data is undefined');
      return;
    }
  
    const previousIndex = this.Sprints.findIndex(sprint => sprint === movedSprint);
  
    if (previousIndex === -1) {
      console.error('Sprint item data is not found in Sprints');
      return;
    }
  
    const currentIndex = event.currentIndex;

  // Réorganiser les sprints
  this.Sprints.splice(previousIndex, 1);
  this.Sprints.splice(currentIndex, 0, movedSprint);

  // Créer le tableau d'ordre des sprints
  const SprintOrder = this.Sprints.map((sprint, index) => ({
    sprintId: sprint.sprintId,
    priority: index+1
  }));
  console.log('Sprint Order:', SprintOrder);
    console.log('Updated Sprints:', this.Sprints);
  
    // Mettre à jour l'ordre des sprints dans le backend si nécessaire
    this.sprintService.updateSprintOrder(SprintOrder).subscribe({
      next: (response) => 

        console.log('Order updated successful:', response),
      error: (error) => console.error('Update error:', error)
    });
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
    this.addSprintForm.patchValue({ projectId: this.projectId });
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
    this.sprintService.searchSprints(this.projectId,sprintName, endDate, status).subscribe({
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
