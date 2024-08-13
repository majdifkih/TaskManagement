import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '../auth/auth-service.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProfilService {

  private host = "http://localhost:8082/user/profil";
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

  
  getAllEmployees(): Observable<any[]> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.get<any[]>(`${this.host}/employees`, { headers });
  }
  
  getUserDetail(): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.get<any>(`${this.host}/userdetail`, { headers });
  }

  getUserByUsername(username: string): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.get<any>(`${this.host}/userbyusername?username=${username}`, { headers });
  }
  updateProfile(profileData: any): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.put<any>(`${this.host}/updateuser`, profileData, { headers });
  }

  deleteUser(userId: number): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.delete<any>(`${this.host}/deluser/${userId}`, { headers });
  }

}
