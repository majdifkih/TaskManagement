import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private host: string = "http://localhost:8082/api/auth";
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  login(data: any): Observable<any> {
    return this.http.post(`${this.host}/signin`, data, this.httpOptions)
      .pipe(
        map((response: any) => {
          if (response) {
            this.storeTokens(response.accessToken, response.refreshToken);
          }
          return response;
        }),
        catchError(error => {
          console.error('Erreur lors de la connexion:', error);
          return throwError(() =>error);
        })
      );
  }

  signup(data: any): Observable<any> {
    return this.http.post(`${this.host}/signup`, data, this.httpOptions);
  }


  private addAuthorizationHeader(headers: HttpHeaders): HttpHeaders {
    const token = this.getAccessToken(); 
  
    if (token) {
      return headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }
  

  logout(): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);
    const options = {
      headers: headers
    };
  
    return this.http.post(`${this.host}/signout`, {}, options)
      .pipe(
        map((response: any) => {
          if (response) {
            this.clearTokens();
          }
          return response;
        }),
        catchError(error => {
          console.error('Erreur lors de la déconnexion:', error);
          return throwError(() =>error);
        })
      );
  }

  refreshToken(refreshToken: string): Observable<any> {
    return this.http.post(`${this.host}/refresh`, { refreshToken }, this.httpOptions).pipe(
      tap((response: any) => {
        if (response) {
          this.storeTokens(response.accessToken, response.refreshToken);
        }
      }),
      catchError(error => {
        console.error('Error during token refresh:', error);
        this.clearTokens(); 
        return throwError(() => error);
      })
    );
  }

  private storeTokens(accessToken: string, refreshToken: string): void {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
  }

  getAccessToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  getRefreshToken(): string | null {
    return localStorage.getItem('refreshToken');
  }

  clearTokens(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  }
}
