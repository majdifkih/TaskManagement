import { Component } from '@angular/core';
import { ProjectService } from '../../services/projects/project-servcie.service';
import { jwtDecode } from 'jwt-decode';
import { TaskService } from '../../services/tasks/task.service';
import { Observable, of } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  projects: any[] = [];
  isAdmin: boolean = false;
  taskCounts$: Observable<any> | null = null;

constructor(private projectService: ProjectService,private taskService:TaskService) {}

ngOnInit(): void {
  
  this.loadProjects();
  this.loadTaskCounts();
}

loadTaskCounts(): void {
  this.taskService.getTaskCount().subscribe(data => {
    console.log(data); // Vérifiez la structure des données reçues
    // Assurez-vous que chaque statut a une valeur par défaut de 0 si absent
    const taskCounts = {
      'To-Do': data['To-Do'] || 0,
      'Doing': data['Doing'] || 0,
      'Done': data['Done'] || 0
    };
    this.taskCounts$ = of(taskCounts);
  });
}

getKeys(obj: any): string[] {
  return Object.keys(obj);
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

loadProjects(): void {
  let role=this.getUserRole();
  console.log('role',role);
    if (role === 'ROLE_ADMIN') {
      this.isAdmin = true;
      this.getAllProjects();
    } else {
      this.ProjectsAssignToUser();
      this.isAdmin = false;
    }
}

getAllProjects(): void {
  const userId = this.getUserId();
  this.projectService.AllProjectsByUser(userId).subscribe({
    next: (response: any) => {
      console.log(response);
      this.projects = this.getLastThreeProjects(response);
    },
    error: (error) => {
      console.error('Error fetching projects:', error);
    }
  });
}

ProjectsAssignToUser(): void {
  this.projectService.AllProjectsAssignToUser().subscribe({
    next: (response: any) => {
      console.log(response);
      this.projects = this.getLastThreeProjects(response);
     
    },
    error: (error) => {
      console.error('Error fetching projects:', error);
    }
  });
}

getLastThreeProjects(projects: any[]): any[] {
  return projects
    .sort((a, b) => new Date(b.creationDate).getTime() - new Date(a.creationDate).getTime())
    .slice(0, 3);
}




}
