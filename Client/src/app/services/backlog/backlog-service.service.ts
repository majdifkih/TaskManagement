import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthServiceService } from '../auth/auth-service.service';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BacklogServiceService {

  
  constructor(
    private http: HttpClient,
    private authService: AuthServiceService 
  ) { }

  private host = "http://localhost:8082/user/backlogs";
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


  addBacklog(backlogData: any): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.post<any>(`${this.host}/addbacklog`, backlogData, { headers }).pipe(
      tap(_ => console.log('Backlog added successfully'))
    );
  }

  
  updateBacklog(id: number, backlogData: any): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.put<any>(`${this.host}/updatebacklog/${id}`, backlogData, { headers }).pipe(
      tap(_ => console.log("Backlog updated successfully"))
    );
  }

 getBacklog(projectId:number): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.get<any>(`${this.host}/backlog/${projectId}`, { headers }).pipe(
      tap(_ => console.log("Backlog retrieved successfully"))
    );
  }

 }

