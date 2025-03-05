  import {Injectable} from '@angular/core';
import {HttpClient, HttpClientModule, HttpErrorResponse} from "@angular/common/http";
import {catchError, Observable, tap, throwError} from "rxjs";

import { User} from "../model/user";
import {CustomHttpResponse} from "../model/appstates";
import {Profile} from "../model/appstates";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly server = 'http://localhost:8020';

  constructor(private http: HttpClient) {
  }
  loginAdmin$ = (email: string, password: string) => <Observable<CustomHttpResponse<Profile>>>
    this.http.post<CustomHttpResponse<Profile>>(`${this.server}/user/adminLogin`, { email, password })
      .pipe(
        tap(response => console.log('Admin Login Response:', response)),
        catchError((error: HttpErrorResponse) => {
          return throwError(() => ({
            statusCode: error.status,
            reason: error.error.reason || 'Login failed.'
          }));
        })
      );


  login$ = (email: string, password: string) => <Observable<CustomHttpResponse<Profile>>>
    this.http.post<CustomHttpResponse<Profile>>(`${this.server}/user/login`, {email, password})
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  verifyCode$ = (email: string, code: string) => <Observable<CustomHttpResponse<Profile>>>
    this.http.get<CustomHttpResponse<Profile>>(`${this.server}/user/verify/code/${email}/${code}`)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );
  register$ = (user: User) => <Observable<CustomHttpResponse<User>>>
    this.http.post<CustomHttpResponse<User>>(`${this.server}/user/register`, user)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  getProfile$ = (): Observable<CustomHttpResponse<Profile>> =>
    this.http.get<CustomHttpResponse<Profile>>(`${this.server}/user/profile`)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage: string;
    if (error.error instanceof ErrorEvent) {
      errorMessage = `An client error occurred: ${error.error.message}`;
    } else {
      if (error.error.reason) {
        errorMessage = error.error.reason;
        console.log(errorMessage);
      } else {
        errorMessage = "an error occurred.- error code :" + error.status;
      }
    }
    return throwError(() => errorMessage);
  }
  updateProfile$ = (user: Partial<User>) => <Observable<CustomHttpResponse<User>>>
    this.http.put<CustomHttpResponse<User>>(`${this.server}/user/updateProfile`, user)
      .pipe(
        tap(response => console.log('Update Profile Response:', response)),
        catchError((error: HttpErrorResponse) => {
          return throwError(() => ({
            statusCode: error.status,
            reason: error.error.reason || 'Profile update failed.'
          }));
        })
      );



}
