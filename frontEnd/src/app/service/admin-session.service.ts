import { Injectable } from '@angular/core';
import {Key} from "../model/appstates";

@Injectable({
  providedIn: 'root'
})
export class AdminSessionService {
  private adminTokenKey = 'admin_token';
  private adminUserKey = 'admin_user';

  constructor() {}

  // Store Admin Token & User Info
  setAdminSession(token: string, user: any) {
    localStorage.setItem(Key.TOKEN, token);
    localStorage.setItem(this.adminUserKey, JSON.stringify(user));
  }

  // Retrieve Admin Token
  getAdminToken(): string | null {
    return localStorage.getItem(Key.TOKEN);
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
    localStorage.removeItem(Key.TOKEN);
    localStorage.removeItem(this.adminUserKey);
  }
}
