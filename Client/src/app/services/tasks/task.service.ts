import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '../auth/auth-service.service';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  
  private host = "http://localhost:8082/user/tasks";
  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };
  private addAuthorizationHeader(headers: HttpHeaders): HttpHeaders {
    const token = this.authService.getAccessToken(); 
    if (token) {
      return headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }

  constructor(
    private http: HttpClient,
    private authService: AuthService ) { }
    
  AllTaskBySprint(id:number): Observable<any[]> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.get<any[]>(`${this.host}/tasksprint/${id}`, { headers }).pipe(
      tap(_ => console.log("Tasks retrieved successfully"))
    );
  }

  MyTasks(): Observable<any[]> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.get<any[]>(`${this.host}/mytasks`, { headers }).pipe(
      tap(_ => console.log("My tasks retrieved successfully"))
    );
  }

  getTaskDetail(id: number): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.get<any>(`${this.host}/task/${id}`, { headers }).pipe(
      tap(_ => console.log("Task detail retrieved successfully"))
    );
  }

  addTask(taskData: any): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.post<any>(`${this.host}/addtask`, taskData, { headers }).pipe(
      tap(_ => console.log('Task added successfully'))
    );
  }

  updateTask(taskData: any,id: number): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.put<any>(`${this.host}/updatetask/${id}`, taskData, { headers }).pipe(
      tap(_ => console.log("Task updated successfully"))
    );
  }

  deleteTask(id: number): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.delete<any>(`${this.host}/deltask/${id}`, { headers }).pipe(
      tap(_ => console.log("Task deleted successfully"))
    );
  }


  assignUsersToTask(taskId: number, userId: number): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);
  
    return this.http.post<any>(`${this.host}/userstasks/${taskId}/${userId}`, {}, { headers }).pipe(
      tap(_ => console.log('User assigned to task successfully'))
    );
  }
  

  
  unassignUserFromTask(taskId: number,userId:number): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.delete<any>(`${this.host}/unassignuser/${taskId}/${userId}`, { headers }).pipe(
      tap(_ => console.log('User unassigned from task successfully'))
    );
  }

  updatestatus(taskData: any,id: number): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.put<any>(`${this.host}/updatestatus/${id}`, taskData, { headers });
}
getAssignedUsers(taskId: number): Observable<any> {
  let headers = this.httpOptions.headers;
  headers = this.addAuthorizationHeader(headers);

  return this.http.get<any>(`${this.host}/users/${taskId}`, { headers }).pipe(
    tap(_ => console.log("Users assigns retrieved successfully"))
  );
}

GetProjectNameOfTask(taskId: number): Observable<string> {
  let params = new HttpParams();
  let headers = this.httpOptions.headers;
  headers = this.addAuthorizationHeader(headers);

  if (taskId) {
    params = params.set('taskId', taskId.toString());
  }

  const options = {
    headers: headers,
    params: params,
    responseType: 'text' as 'json'  // Important: Indiquer que la réponse est de type texte
  };

  return this.http.get<string>(`${this.host}/projectnameoftask`, options);
}

getTaskCount(): Observable<any> {
  let headers = this.httpOptions.headers;
  headers = this.addAuthorizationHeader(headers);

  return this.http.get<any>(`${this.host}/taskcount`, { headers });
}

}
