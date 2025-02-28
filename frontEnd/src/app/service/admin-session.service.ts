import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AdminSessionService {
  private adminTokenKey = 'admin_token';
  private adminUserKey = 'admin_user';

  constructor() {}

  // Store Admin Token & User Info
  setAdminSession(token: string, user: any) {
    localStorage.setItem(this.adminTokenKey, token);
    localStorage.setItem(this.adminUserKey, JSON.stringify(user));
  }

  // Retrieve Admin Token
  getAdminToken(): string | null {
    return localStorage.getItem(this.adminTokenKey);
  }

  // Retrieve Admin User Data
  getAdminUser(): any | null {
    const userData = localStorage.getItem(this.adminUserKey);
    return userData ? JSON.parse(userData) : null;
  }

  // Check if Admin is Logged In
  isAdminLoggedIn(): boolean {
    return this.getAdminToken() !== null;
  }

  // Clear Admin Session
  logout() {
    localStorage.removeItem(this.adminTokenKey);
    localStorage.removeItem(this.adminUserKey);
  }
}
