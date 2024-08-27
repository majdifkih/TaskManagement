import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { TaskService } from '../../services/tasks/task.service';
import { ToastrService } from 'ngx-toastr';
import { ProfilService } from '../../services/profil/profil.service';
import { CommentService } from '../../services/comments/comment.service';
import { jwtDecode } from 'jwt-decode';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-my-tasks',
  templateUrl: './my-tasks.component.html',
  styleUrl: './my-tasks.component.css'
})

export class MyTasksComponent implements OnInit  {
    sprintId: number = 0;
    EditTaskForm!: FormGroup;
    searchUserForm!: FormGroup;
    TaskName: string = '';
    MyTasks: any[] = [];
    task: any;
    ToDoTasks: any[] = [];
    DoingTasks: any[] = [];
    DoneTasks: any[] = [];
    delId: number = 0;
    showAlert = false;
    currentTaskId : number = 0;
    currentviewTaskId : number = 0;
    showEdit: boolean = false;
    showdetail: boolean = false;
    openAccordions: { [key: string]: boolean } = {};
    assignedUsers: any[] = [];
    allUsers: any[] = [];
    filteredUsers: any[] = [];
  
    Comments: any[] = [];
    addCommentForm!: FormGroup;
    editingCommentId: number | null = null;
    currentusername: string = '';
    ProjectName:string = '';
    constructor(private route: ActivatedRoute,
      private taskService:TaskService,
      private fb: FormBuilder,
      private _toastr: ToastrService,
      private commentService:CommentService) { }
  
    ngOnInit(): void {
      const id = this.route.snapshot.paramMap.get('id');
      this.sprintId = id !== null ? +id : 0;
      
      this.EditTaskForm = this.fb.group({
        taskName: [''],
        taskDescription: [''],
        startDate: [''],
        endDate: ['']
      }); 
  
      this.addCommentForm = this.fb.group({
        content: [''],
      }); 
      this.getMyTasks();
      this.GetUserName();
    }
  
  
  
  GetUserName(){
    let userName = this.getUserName();
     this.currentusername=userName;
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
          this.getMyTasks(); // Recharger les tâches après mise à jour
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
    
    getMyTasks(): void {
      this.taskService.MyTasks().subscribe({
        next: (response: any) => {
          this.MyTasks = response;
    
          // Parcourir les tâches pour récupérer le nom du projet associé
          this.MyTasks.forEach(task => {
            this.taskService.GetProjectNameOfTask(task.taskId).subscribe({
              next: (projectName: string) => {
                this.ProjectName = projectName; 
              },
              error: (error) => {
                console.error('Error fetching project name:', error);
              }
            });
          });
    
          // Trier et filtrer les tâches selon leur statut
          this.MyTasks.sort((a: any, b: any) => a.taskOrder - b.taskOrder);
          this.ToDoTasks = this.MyTasks.filter(task => task.status === 'To-Do');
          this.DoingTasks = this.MyTasks.filter(task => task.status === 'Doing');
          this.DoneTasks = this.MyTasks.filter(task => task.status === 'Done');
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
    
  
    //update sprint function
    updateTask(id: number) {
      if (this.EditTaskForm.valid) {
          console.log("Form values before sending:", this.EditTaskForm.value);
          // Correction de l'URL en utilisant l'ID du sprint
          this.taskService.updateTask(this.EditTaskForm.value, id).subscribe({
              next: (data: any) => {
                  this._toastr.success('Task updated successfully', 'Success');
                  this.getMyTasks();
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
          this.getMyTasks();
        },
        error: (error) => {
          console.error('Error deleting task:', error);
          this._toastr.error('Error deleting task', 'Error');
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