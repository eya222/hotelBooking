import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() { }

  // Basic check: assumes user info is stored in localStorage on login
  isAuthenticated(): boolean {
    // Replace 'userData' with the actual key you use to store user info
    return localStorage.getItem('userData') !== null;
  }

  // Example login function (replace with your actual login logic)
  login(userData: any): void {
    localStorage.setItem('userData', JSON.stringify(userData));
    // Navigate to dashboard or home page after login
  }

  // Example logout function
  logout(): void {
    localStorage.removeItem('userData');
    // Navigate to login page after logout
  }

  // Optional: Get user data if needed elsewhere
  getUserData(): any | null {
    const userData = localStorage.getItem('userData');
    return userData ? JSON.parse(userData) : null;
  }
}
