import { CdkDragDrop,
  CdkDrag,
  CdkDropList,
  CdkDropListGroup,
  moveItemInArray,
  transferArrayItem} from '@angular/cdk/drag-drop';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { TaskService } from '../../services/tasks/task.service';
import { ProfilService } from '../../services/profil/profil.service';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs';
import { CommentService } from '../../services/comments/comment.service';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrl: './tasks.component.css'
})
export class TasksComponent implements OnInit  {
  sprintId: number = 0;
  addTaskForm!: FormGroup;
  EditTaskForm!: FormGroup;
  searchUserForm!: FormGroup;
  TaskName: string = '';
  Tasks: any[] = [];
  task: any;
  ToDoTasks: any[] = [];
  DoingTasks: any[] = [];
  DoneTasks: any[] = [];
  showModal: boolean = false;
  delId: number = 0;
  showAlert = false;
  currentTaskId : number = 0;
  currentviewTaskId : number = 0;
  showEdit: boolean = false;
  showdetail: boolean = false;
  showAssignUser: boolean = false;
  openAccordions: { [key: string]: boolean } = {};
  assignedUsers: any[] = [];
  allUsers: any[] = [];
  filteredUsers: any[] = [];
  searchControl = new FormControl();

  Comments: any[] = [];
  addCommentForm!: FormGroup;
  editingCommentId: number | null = null;
  constructor(private route: ActivatedRoute,
    private taskService:TaskService,
    private fb: FormBuilder,
    private _toastr: ToastrService,
    private profilService:ProfilService,
    private commentService:CommentService) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.sprintId = id !== null ? +id : 0;
    this.addTaskForm = this.fb.group({
      taskName: ['', [Validators.required]],
      taskDescription: ['', [Validators.required]],
      startDate: ['', [Validators.required]],
      endDate: ['', [Validators.required]],
      sprintId: [this.sprintId],
    });
    this.EditTaskForm = this.fb.group({
      taskName: [''],
      taskDescription: [''],
      startDate: [''],
      endDate: ['']
    }); 
    this.searchUserForm = this.fb.group({
      username: [''],
    });    

    this.addCommentForm = this.fb.group({
      content: [''],
    }); 
    this.getAllTasks();
    
   
}
  
  drop(event: CdkDragDrop<any[]>) {
    const movedTask = event.item.data;
  
    if (!movedTask) {
      console.error('Task item data is undefined');
      return;
    }
  
    const previousContainer = event.previousContainer;
    const currentContainer = event.container;
  
    // Réorganiser les tâches dans la même colonne
    if (previousContainer === currentContainer) {
      moveItemInArray(currentContainer.data, event.previousIndex, event.currentIndex);
    } else {
      // Déplacer les tâches entre les colonnes
      transferArrayItem(
        previousContainer.data,
        currentContainer.data,
        event.previousIndex,
        event.currentIndex
      );
    }
  
    // Mettre à jour l'état et l'ordre de la tâche déplacée
    this.updateTaskStatus(movedTask, event.container.id, event.currentIndex, currentContainer.data);
  }
  
 
 
  updateTaskStatus(task: any, containerId: string, currentIndex: number, allTasksInColumn: any[]) {
    const updatedStatus = this.getStatusFromContainerId(containerId);
    const updatedTask = { status: updatedStatus, taskOrder: currentIndex + 1 };
  
    console.log('Updated Task:', updatedTask); // Debugging line
  
    // Mettre à jour la tâche déplacée
    this.taskService.updatestatus(updatedTask, task.taskId).subscribe({
      next: () => {
        console.log('Task status updated successfully');
  
        // Réorganiser les tâches dans la colonne cible
        allTasksInColumn.forEach((t: any, index: number) => {
          if (t.taskId !== task.taskId) {
            t.taskOrder = index + 1;
            this.taskService.updatestatus(t, t.taskId).subscribe({
              error: (error) => console.error('Error updating task order:', error)
            });
          }
        });
  
        this._toastr.success('Task updated successfully', 'Success');
        this.getAllTasks(); // Recharger les tâches après mise à jour
      },
      error: (error) => {
        console.error('Error updating task status:', error);
      }
    });
  }
  
  getStatusFromContainerId(containerId: string): string {
    switch (containerId) {
      case 'cdk-drop-list-to-do':
        return 'To-Do';
      case 'cdk-drop-list-doing':
        return 'Doing';
      case 'cdk-drop-list-done':
        return 'Done';
      default:
        return 'To-Do';
    }
  }
  
  
  

  
  getAllTasks(): void {
    this.taskService.AllTaskBySprint(this.sprintId).subscribe({
      next: (response: any) => {
        this.Tasks = response;

      this.Tasks.sort((a: any, b: any) => a.taskOrder - b.taskOrder);
        this.ToDoTasks = this.Tasks.filter(task => task.status === 'To-Do');
        this.DoingTasks = this.Tasks.filter(task => task.status === 'Doing');
        this.DoneTasks = this.Tasks.filter(task => task.status === 'Done');
        console.log('ToDoTasks:', this.ToDoTasks);
        console.log('DoingTasks:', this.DoingTasks);
        console.log('DoneTasks:', this.DoneTasks);
      },
      error: (error) => {
        console.error('Error fetching tasks:', error);
      }
    });
  }
  
  getTaskDetail(taskId: number): void {
    this.taskService.getTaskDetail(taskId).subscribe({
      next: (response: any) => {
        this.task = response; // Assurez-vous de stocker la réponse dans une variable pour l'utiliser dans le template
        console.log('Task Detail Response:', response);
      },
      error: (error) => {
        console.error('Error fetching task:', error);
      }
    });
  }
  


  addTask() {
    if (this.addTaskForm.valid) {
      console.log("Form values before sending:", this.addTaskForm.value);
  
      this.taskService.addTask(this.addTaskForm.value).subscribe({
        next: (data: any) => {
          this._toastr.success('Task added successfully', 'Success');
          this.getAllTasks(); 
         this.closeModal();
        },
        error: (error) => {
          console.error('Error adding task:', error);
          this._toastr.error('Error adding task', 'Error');
        }
      });
    } else {
      console.error('Task form is invalid');
      this._toastr.error('Please fill out all required fields', 'Error');
    }
  }

  //update sprint function
  updateTask(id: number) {
    if (this.EditTaskForm.valid) {
        console.log("Form values before sending:", this.EditTaskForm.value);

        // Correction de l'URL en utilisant l'ID du sprint
        this.taskService.updateTask(this.EditTaskForm.value, id).subscribe({
            next: (data: any) => {
                this._toastr.success('Task updated successfully', 'Success');
                this.getAllTasks();
               this.closeModalEdit();
            },
            error: (error) => {
                console.error('Error updating task:', error);
                this._toastr.error('Error updating task', 'Error');
            }
        });
    } else {
        console.error('Task form is invalid');
        this._toastr.error('Please fill out all required fields', 'Error');
    }
}

  
  
  deleteTask(id: number) {
    this.taskService.deleteTask(id).subscribe({
      next: (response: any) => {
        this._toastr.success('Task deleted successfully', 'Success');
        this.getAllTasks();
      },
      error: (error) => {
        console.error('Error deleting task:', error);
        this._toastr.error('Error deleting task', 'Error');
      }
    });
  }


  
  allEmployees(): void {
    this.profilService.getAllEmployees().subscribe({
      next: (response: any) => {
        this.allUsers = response;
        this.loadAssignedUsers(); // Assurez-vous de charger les utilisateurs assignés
      },
      error: (error) => {
        console.error('Error fetching employees:', error);
      }
    });
  }
  

  filterEmployees(searchValue: string) {
    return this.allUsers.filter(user =>
      user.username.toLowerCase().startsWith(searchValue.toLowerCase())
    );
  }
  


  assignUserToTask(userId: number): void {
    if (userId && this.currentviewTaskId) {
      this.taskService.assignUsersToTask(this.currentviewTaskId, userId).subscribe({
        next: (response: any) => {
          this._toastr.success('User assigned to task successfully', 'Success');
          this.loadAssignedUsers(); // Recharge les utilisateurs assignés
         
        },
        error: (error) => {
          console.error('Error assigning user to task:', error);
          this._toastr.error('Error assigning user to task', 'Error');
        }
      });
    } else {
      this._toastr.error('Invalid user or task ID', 'Error');
    }
  }
  

  unassignUser(userId: number): void {
    
    console.log("assignedUsers=",this.assignedUsers);
    this.taskService.unassignUserFromTask(this.currentviewTaskId, userId).subscribe({
      next: (response: any) => {
        this._toastr.success('User unassigned from task successfully', 'Success');
        this.loadAssignedUsers(); // Recharge les utilisateurs assignés
      },
      error: (error) => {
        console.error('Error unassigning user from task:', error);
        this._toastr.error('Error unassigning user from task', 'Error');
      }
    });
  }

  loadAssignedUsers(): void {
    this.taskService.getAssignedUsers(this.currentviewTaskId).subscribe({
      next: (response: any) => {
        this.assignedUsers = response;
        this.filterUnassignedUsers(); // Filtre les utilisateurs non assignés après avoir récupéré les assignés
      },
      error: (error) => {
        console.error('Error fetching assigned users:', error);
      }
    });
  }
  filterUnassignedUsers(): void {
    this.filteredUsers = this.allUsers.filter(user =>
      !this.assignedUsers.some(assignedUser => assignedUser.id === user.id)
    );
  }
    

  isUserAssigned(userId: number): boolean {
    return this.assignedUsers.some(user => user.id === userId);
  }


  onAddTask() {
    this.addTaskForm.patchValue({ sprintId: this.sprintId });
    this.showModal = true;
  }

  noReturnPredicate() {
    return false;
  }
  closeModal() {
    this.showModal = false;
    this.addTaskForm.reset();
  }

  confirmationAlert(id: number) {
    this.showAlert = true;
    this.delId = id;
  }

  closeAlert() {
    this.showAlert = false;
  }

  confirmDelete() {
    this.deleteTask(this.delId);
    this.closeAlert();
  }

  onModalEdit(task: any) {
    this.showEdit = true;
    this.currentTaskId = task.taskId;
    this.EditTaskForm.patchValue(task);
  }

  closeModalEdit() {
    this.showEdit = false;
    this.EditTaskForm.reset();
  }
  
  onModalDetail(task: any) {
    this.showdetail = true;
    this.currentviewTaskId = task.taskId;
    this.getTaskDetail(task.taskId); 
    this.loadAssignedUsers();
    this.getAllComments(task.taskId);
  }
  
  closeModalDetail() {
    this.showdetail = false;
  }

  onModalAssignUser(task: any) {
    this.showAssignUser = true;
    this.TaskName=task.taskName;
    this.currentviewTaskId = task.taskId;
    this.allEmployees(); // Récupérez tous les employés au démarrage
    this.loadAssignedUsers();
    this.searchControl.valueChanges.subscribe(value => {
      this.filteredUsers = this.filterEmployees(value);
    });
  }
  
  closeModalAssignUser() {
    this.showAssignUser = false;
  }
  
  showDatePicker(event: Event): void {
    const input = event.target as HTMLInputElement;
    input.showPicker(); 
  }


  toggleAccordion(taskId: number): void {
    this.openAccordions[taskId] = !this.openAccordions[taskId];
   
  }
  

  isAccordionOpen(taskId: number): boolean {
    return this.openAccordions[taskId] || false;
  }


//Comment Management


 getAllComments(taskId:number): void {
  this.commentService.AllCommentsByTask(taskId).subscribe({
    next: (response: any) => {
      this.Comments=response;
      console.log(response);
    },
    error: (error) => {
      console.error('Error fetching comments:', error);
    }
  });
}

addComment(taskId: number) {
  if (this.addCommentForm.valid) {
    console.log("Form values before sending:", this.addCommentForm.value);

    this.commentService.addComment(taskId,this.addCommentForm.value).subscribe({
      next: (data: any) => {
        this.getAllComments(taskId);
        this.addCommentForm.reset();
        this._toastr.success('Comment added successfully', 'Success');
      },
      error: (error) => {
        console.error('Error adding Comment:', error);
        this._toastr.error('Error adding Comment', 'Error');
      }
    });
  } else {
    console.error('Sprint form is invalid');
    this._toastr.error('Please fill out all required fields', 'Error');
  }
}
deleteComment(comment: any) {
  let userName = this.getUserName();
  if (comment.userComment === userName) {
    this.commentService.deleteComment(comment.commentId).subscribe({
      next: (response: any) => {
        this._toastr.success('Comment deleted successfully', 'Success');
        this.getAllComments(this.currentviewTaskId);
      },
      error: (error) => {
        console.error('Error deleting Comment:', error);
        this._toastr.error('Error deleting Comment', 'Error');
      }
    });
  } else {
    this._toastr.error('You can only delete your own comments', 'Error');
  }
}

getUserName(){
  const accessToken = localStorage.getItem('accessToken'); 
  if (!accessToken) {
    return null; 
  }

  try {
    const decodedToken: any = jwtDecode(accessToken);
    return decodedToken.username || null; 
  } catch (error) {
    console.error('Error decoding JWT:', error);
    return null;
  }
}

updateComment(commentId: number) {
  const updatedContent = this.Comments.find(comment => comment.commentId === commentId)?.content;

  if (updatedContent) {
    this.commentService.updateComment({ content: updatedContent }, commentId).subscribe({
      next: (data: any) => {
        this.getAllComments(this.currentviewTaskId);
        this.editingCommentId = null;
        this._toastr.success('Comment updated successfully', 'Success');
      },
      error: (error) => {
        console.error('Error updating Comment:', error);
        this._toastr.error('Error updating Comment', 'Error');
      }
    });
  }
}

OnEditComment(comment: any) {
  let userName = this.getUserName();

  if (comment.userComment === userName) {
    this.editingCommentId = comment.commentId;
  } else {
    this._toastr.error('You can only edit your own comments', 'Error');
  }
}

}
