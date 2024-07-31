import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AuthServiceService } from '../auth/auth-service.service';


@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private host = "http://localhost:8082/admin/projects";
  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(
    private http: HttpClient,
    private authService: AuthServiceService 
  ) { }

  private addAuthorizationHeader(headers: HttpHeaders): HttpHeaders {
    const token = this.authService.getAccessToken(); 

    if (token) {
      return headers.set('Authorization', `Bearer ${token}`);
      
    }
    
    return headers;

  }




  // getAllProjects(): Observable<any[]> {
  //   let headers = this.httpOptions.headers;
  //   headers = this.addAuthorizationHeader(headers);

  //   return this.http.get<any[]>(`${this.host}/allproject`, { headers }).pipe(
  //     tap(_ => console.log("Projects retrieved successfully"))
  //   );
  // }
  AllProjectsByUser(id: number): Observable<any[]> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.get<any[]>(`${this.host}/project/${id}`, { headers }).pipe(
      tap(_ => console.log("Projects retrieved successfully"))
    );
  }

  getProjectDetail(id: number): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.get<any>(`${this.host}/projectdetail/${id}`, { headers }).pipe(
      tap(_ => console.log("Project detail retrieved successfully"))
    );
  }

  addProject(projectData: any): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.post<any>(`${this.host}/addproject`, projectData, { headers }).pipe(
      tap(_ => console.log('Project added successfully'))
    );
  }

  updateProject(id: number, projectData: any): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.put<any>(`${this.host}/updateproject/${id}`, projectData, { headers }).pipe(
      tap(_ => console.log("Project updated successfully"))
    );
  }

  deleteProject(id: number): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.delete<any>(`${this.host}/delproject/${id}`, { headers }).pipe(
      tap(_ => console.log("Project deleted successfully"))
    );
  }
}
