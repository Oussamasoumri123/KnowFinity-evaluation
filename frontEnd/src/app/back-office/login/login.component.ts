import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../service/user-service.service';
import { AdminSessionService } from '../../service/admin-session.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  error: string | null = null;

  constructor(
    private userService: UserService,
    private adminSessionService: AdminSessionService,
    private router: Router
  ) {}

  loginAdmin(): void {
    this.userService.loginAdmin$(this.email, this.password).subscribe({
      next: (response) => {
        if (response && response.data) {
          const { access_token, user } = response.data;

          // Store admin session
          // @ts-ignore
          this.adminSessionService.setAdminSession(access_token, user);

          // Redirect to admin dashboard
          this.router.navigate(['/admin/dashboard']);
        } else {
          this.error = 'Login failed. Please try again.';
        }
      },
      error: () => {
        this.error = 'Invalid credentials. Please try again.';
      }
    });
  }
}
