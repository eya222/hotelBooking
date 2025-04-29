import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Login } from '../models/login';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private apiUrl = 'http://localhost:8085/api/login'; // <- check your backend route
  private userId: any;
  private loginStatus = false;

  constructor(private http: HttpClient) {}

  public checkUser(data: Login): Observable<boolean> {
    return this.http.post<boolean>(this.apiUrl, data);
  }

  public setId(id: any): void {
    this.userId = id;
  }

  public getId(): any {
    return this.userId;
  }

  public setLoginStatus(status: boolean): void {
    this.loginStatus = status;
  }

  public getLoginStatus(): boolean {
    return this.loginStatus;
  }
}
