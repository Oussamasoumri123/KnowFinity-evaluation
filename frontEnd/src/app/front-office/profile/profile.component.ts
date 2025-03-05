import { Component, OnInit } from '@angular/core';
import { UserService } from '../../service/user-service.service';
import { User } from '../../model/user';
import { Profile } from "../../model/appstates";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  profile: Profile = { user: {} as User }; // Ensures `profile.user` is always defined

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
          this.profile.user = { ...response.data.user };
          console.log("Transformed Profile Data:", this.profile); // Debugging
        } else {
          this.error = "Unexpected response from the server.";
        }
      },
      error: (err) => {
        console.error("Error loading profile:", err);
        if (err.status === 401) {
          this.error = "Authentication required. Please log in.";
          window.location.href = '/login';
        } else {
          this.error = "An error occurred.";
        }
      }
    });
  }

  updateProfile() {
    if (!this.profile.user) {
      this.error = "Profile data is missing.";
      return;
    }

    // Prepare user object for update (excluding email)
    const updatedUser: Partial<User> = {
      firstName: this.profile.user.firstName,
      lastName: this.profile.user.lastName,
      phone: this.profile.user.phone,
      address: this.profile.user.address,
      title: this.profile.user.title,
      bio: this.profile.user.bio,
    };

    console.log("Updating Profile with:", updatedUser); // Debugging

    this.userService.updateProfile$(updatedUser).subscribe({
      next: (response) => {
        console.log("Profile updated successfully:", response);
        alert("Profile updated successfully!");
        this.loadProfile(); // Reload updated profile
      },
      error: (err) => {
        console.error("Error updating profile:", err);
        this.error = "Failed to update profile.";
      }
    });
  }
}
