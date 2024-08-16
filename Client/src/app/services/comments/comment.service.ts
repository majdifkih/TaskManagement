import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '../auth/auth-service.service';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(
    private http: HttpClient,
    private authService: AuthService 
  ) { }

  private host = "http://localhost:8082/user/comments";
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
  AllCommentsByTask(taskId:number): Observable<any[]> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.get<any[]>(`${this.host}/allcomment/${taskId}`, { headers }).pipe(
      tap(_ => console.log("Comments retrieved successfully"))
    );
  }

  addComment(taskId:number,commentData: any): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.post<any>(`${this.host}/addcomment/${taskId}`, commentData, { headers }).pipe(
      tap(_ => console.log('Comment added successfully'))
    );
  }

  updateComment(commentData: any,commmentId: number): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.put<any>(`${this.host}/updatecomment/${commmentId}`, commentData, { headers }).pipe(
      tap(_ => console.log("Comment updated successfully"))
    );
  }

  deleteComment(commentId: number): Observable<any> {
    let headers = this.httpOptions.headers;
    headers = this.addAuthorizationHeader(headers);

    return this.http.delete<any>(`${this.host}/delcomment/${commentId}`, { headers }).pipe(
      tap(_ => console.log("Comment deleted successfully"))
    );
  }
}
