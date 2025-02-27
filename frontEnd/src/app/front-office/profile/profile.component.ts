import { Component, OnInit } from '@angular/core';
import { UserService } from '../../service/user-service.service';
import { User } from '../../model/user';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  profile: User | null = null;
  error: string | null = null;
  activeSection: string = 'profile'; // Default section

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  setActiveSection(section: string) {
    this.activeSection = section;
  }

  loadProfile(): void {
    this.userService.getProfile$().subscribe({
      next: (response) => {
        if (response && response.data && response.data.user) {
          const userData = response.data.user;

          // Transform backend response to match the frontend model

          this.profile = {
            ...userData,
            // @ts-ignore
            roleNames: userData.roleNames? userData.roleNames.split(',') : [], // Convert to array
            // @ts-ignore
            permissions: userData.permissions ? userData.permissions.split(',') : [] // Convert to array
          };

          console.log("Transformed Profile Data:", this.profile); // Debugging
        } else {
          this.error = "Unexpected response from the server.";
        }
      },
      error: (err) => {
        if (err.status === 401) {
          this.error = "Authentication required. Please log in.";
          window.location.href = '/login';
        } else {
          this.error = "An error occurred.";
        }
      }
    });
  }
}
