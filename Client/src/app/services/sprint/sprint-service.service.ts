import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '../auth/auth-service.service';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SprintService {

  constructor(
    private http: HttpClient,
    private authService: AuthService 
  ) { }

  private host = "http://localhost:8082/user/sprints";
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




  // getAllSprint(): Observable<any[]> {
  //   let headers = this.httpOptions.headers;
  //   headers = this.addAuthorizationHeader(headers);

  //   return this.http.get<any[]>(`${this.host}/allsprint`, { headers }).pipe(
  //     tap(_ => console.log("Sprints retrieved successfully"))
  //   );
  // }

  AllSprintByProject(id:number): Observable<any[]> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.get<any[]>(`${this.host}/sprintbacklog/${id}`, { headers }).pipe(
      tap(_ => console.log("Sprints retrieved successfully"))
    );
  }

  getSprintDetail(id: number): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.get<any>(`${this.host}/sprintdetail/${id}`, { headers }).pipe(
      tap(_ => console.log("Sprint detail retrieved successfully"))
    );
  }

  addSprint(sprintData: any): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.post<any>(`${this.host}/addsprint`, sprintData, { headers }).pipe(
      tap(_ => console.log('Sprint added successfully'))
    );
  }

  updateSprint(sprintData: any,id: number): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.put<any>(`${this.host}/updatesprint/${id}`, sprintData, { headers }).pipe(
      tap(_ => console.log("Sprint updated successfully"))
    );
  }

  deleteSprint(id: number): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.delete<any>(`${this.host}/delsprint/${id}`, { headers }).pipe(
      tap(_ => console.log("Sprint deleted successfully"))
    );
  }
  searchSprints(projectId: number,sprintName?: string, endDate?: string, status?: string): Observable<any[]> {
    let params = new HttpParams();
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);
    if (projectId) {
      params = params.set('projectId', projectId);
    }
    if (sprintName) {
      params = params.set('sprintName', sprintName);
    }
    if (endDate) {
      params = params.set('endDate', endDate);
    }
    if (status) {
      params = params.set('status', status);
    }

    const options = {
      headers: headers,
      params: params
    };
    
    return this.http.get<any[]>(`${this.host}/search`, options);
  }

  updateSprintOrder(sprintData: any[]): Observable<void> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);
    return this.http.post<any>(`${this.host}/order`, sprintData, { headers });
  }
  
}
