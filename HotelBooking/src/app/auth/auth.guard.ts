import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true; // User is authenticated, allow navigation
  } else {
    // User is not authenticated, redirect to login page
    console.warn('AuthGuard: User not authenticated, redirecting to /login'); // Add log
    router.navigate(['/login']);
    return false; // Prevent navigation
  }
};
