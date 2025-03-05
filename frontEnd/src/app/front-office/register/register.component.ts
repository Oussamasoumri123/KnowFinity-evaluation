import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { UserService } from '../../service/user-service.service';
import { Router } from '@angular/router';
import { catchError, map, of, Observable, startWith } from 'rxjs';
import { User } from '../../model/user';
import { CustomHttpResponse, DataState} from '../../model/appstates';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerState$: Observable<{ dataState: DataState; message?: string; error?: string }> = of({ dataState: DataState.LOADED });
  readonly DataState = DataState;

  constructor(private userService: UserService, private router: Router) {}

  register(registerForm: NgForm): void {
    if (registerForm.invalid) return;

    const { firstName, lastName, email, password } = registerForm.value;

    const newUser: User = {
      firstName,
      lastName,
      email,
      password,
    };

    this.registerState$ = this.userService.register$(newUser).pipe(
      map(response => {
        console.log('Registration successful:', response);
        setTimeout(() => this.router.navigate(['/login']), 3000);
        return { dataState: DataState.LOADED, message: 'Verification mail sent check your mail box' };
      }),
      startWith({ dataState: DataState.LOADING }),
      catchError((error: string) => {
        return of({ dataState: DataState.ERROR, error });
      })
    );
  }
}
