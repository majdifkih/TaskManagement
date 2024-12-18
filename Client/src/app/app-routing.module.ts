import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { ProjectsComponent } from './pages/projects/projects.component';
import { BacklogComponent } from './pages/Backlog/backlog/backlog.component';
import { TasksComponent } from './pages/Tasks/tasks.component';
import { ProfilComponent } from './pages/profil/profil.component';
import { MyTasksComponent } from './pages/my-tasks/my-tasks.component';

const routes: Routes = [
  {
    path:'',
    redirectTo : 'login',
    pathMatch:'full'
  },
  {
    path:'login',
    component: LoginComponent
  },
  {
    path:'dash',
    component: DashboardComponent
  },
  {
    path:'projects',
    component: ProjectsComponent
  },
  {
    path:'tasks/:id',
    component: TasksComponent
  },
  {
    path:'mytasks',
    component: MyTasksComponent
  },
  {
    path:'backlog/:id',
    component: BacklogComponent
  },
  {
    path:'profil',
    component: ProfilComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
