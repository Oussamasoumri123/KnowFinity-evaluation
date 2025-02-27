import { Component } from '@angular/core';
import {BehaviorSubject, catchError, map, Observable, of, startWith} from "rxjs";
import { DataState, Key, LoginState } from '../../model/user';
import {Router} from "@angular/router";
import {UserService} from "../../service/user-service.service";
import {UserSessionService} from "../../service/user-session.service";
import {NgForm} from "@angular/forms";
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginState$: Observable<LoginState> = of({ dataState: DataState.LOADED });
  private phoneSubject = new BehaviorSubject<string | null>('');
  private emailSubject = new BehaviorSubject<string | null>('');
  readonly DataState = DataState;

  constructor(
    private router: Router,
    private userService: UserService,
    private userSessionService: UserSessionService
  ) {}

  login(loginForm: NgForm): void {
    if (!loginForm.value.email || !loginForm.value.password) {
      let errorMessage = 'Fields cannot be empty';
      if (!loginForm.value.email && !loginForm.value.password) {
        errorMessage = 'Email and Password cannot be empty';
      } else if (!loginForm.value.email) {
        errorMessage = 'Email cannot be empty';
      } else if (!loginForm.value.password) {
        errorMessage = 'Password cannot be empty';
      }

      this.loginState$ = of({
        dataState: DataState.ERROR,
        isUsingMfa: false,
        loginSuccess: false,
        error: errorMessage,
      });
      return;
    }

    this.loginState$ = this.userService.login$(loginForm.value.email, loginForm.value.password)
      .pipe(
        map(response => {
          // @ts-ignore
          const user = response.data.user;
          // @ts-ignore
          if (user.usingMfa) {
            // @ts-ignore
            this.phoneSubject.next(user.phone);
            // @ts-ignore
            this.emailSubject.next(user.email);

            return {
              dataState: DataState.LOADED,
              isUsingMfa: true,
              loginSuccess: false,
              // @ts-ignore
              phone: user.phone.substring(user.phone.length - 4),
            };
          } else {
            // @ts-ignore
            localStorage.setItem(Key.TOKEN, response.data.access_token);
            // @ts-ignore
            localStorage.setItem(Key.REFRESH_TOKEN, response.data.refresh_token);

            // @ts-ignore
            this.userSessionService.setUser(user);
            this.router.navigate(['/']);
            return { dataState: DataState.LOADED, loginSuccess: true };
          }
        }),
        startWith({ dataState: DataState.LOADING, isUsingMfa: false }),
        catchError((error: string) => {
          return of({
            dataState: DataState.ERROR,
            isUsingMfa: false,
            loginSuccess: false,
            error,
          });
        })
      );
  }

  verifyCode(verifyCodeForm: NgForm): void {
    // @ts-ignore
    this.loginState$ = this.userService.verifyCode$(this.emailSubject.value, verifyCodeForm.value.code)
      .pipe(
        map(response => {
          // @ts-ignore
          localStorage.setItem(Key.TOKEN, response.data.access_token);
          // @ts-ignore
          localStorage.setItem(Key.REFRESH_TOKEN, response.data.refresh_token);
          // @ts-ignore
          this.userSessionService.setUser(response.data.user);
          this.router.navigate(['/']);
          return { dataState: DataState.LOADED, loginSuccess: true };
        }),
        startWith({
          dataState: DataState.LOADING,
          isUsingMfa: true,
          loginSuccess: false,
          // @ts-ignore
          phone: this.phoneSubject.value.substring(this.phoneSubject.value.length - 4)
        }),
        catchError((error: string) => {
          return of({
            dataState: DataState.ERROR,
            isUsingMfa: true,
            loginSuccess: false,
            error,
            // @ts-ignore
            phone: this.phoneSubject.value.substring(this.phoneSubject.value.length - 4)
          });
        })
      );
  }

  loginPage(): void {
    this.loginState$ = of({ dataState: DataState.LOADED });
  }
}
