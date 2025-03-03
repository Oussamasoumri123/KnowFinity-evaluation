import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AdminSessionService } from '../service/admin-session.service';

export const adminAuthGuard: CanActivateFn = (route, state) => {
  const adminSessionService = inject(AdminSessionService);
  const router = inject(Router);

  if (adminSessionService.isAdminLoggedIn()) {
    return true; // Allow access if admin is logged in
  } else {
    router.navigate(['/admin/login']); // Redirect to login if not authenticated
    return false;
  }
};
