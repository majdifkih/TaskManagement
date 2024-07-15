import { Injectable } from '@angular/core';
import { HttpClient,HttpEvent,HttpHeaders, HttpRequest, HttpResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {
  private host: string = "http://localhost:8082/api/auth";
  httpOptions = {
    headers: new HttpHeaders({
    'Content-Type': 'application/json'
    })
    }
  constructor(private http: HttpClient) { }

  

  login(data: any): Observable<any> {
    return this.http.post(`${this.host}/signin`, data, this.httpOptions)
      .pipe(
        map((response: any) => {
          if (response) {
          
            localStorage.setItem('refreshToken', response.refreshToken);
            localStorage.setItem('accessToken', response.accessToken);
          }
          return response;
        }),
        catchError(error => {
          console.error('Erreur lors de la connexion:', error);
          return throwError(error);
        })
      );
  }
  

  signup(data: any): Observable<any> {
    
    return this.http.post(`${this.host}/signup`, data);
  }

  logout(): Observable<any> {
    
    return this.http.post(`${this.host}/logout`, {})
      .pipe((response: any) => {
        if (response) {
          localStorage.removeItem('currentUser');
        }
        return response;
      });
  }

  
  
 
    
  
}
