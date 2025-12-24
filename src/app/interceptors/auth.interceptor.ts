import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/AuthService';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = authService.getToken();

  // Empêche erreur includes sur null / undefined
  const url = req.url ?? '';

  // Ne pas ajouter le token pour les endpoints publics
  const publicEndpoints = ['/login', '/register', '/forgot-password', '/send-code', '/verify-code'];

  if (publicEndpoints.some(path => url.includes(path))) {
    return next(req);  // ne rien modifier
  }

  // Ajouter token seulement s'il existe
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(req).pipe(
    catchError(error => {
      if (error.status === 401 || error.status === 403) {
        // Token expiré ou invalide - déconnecter l'utilisateur
        authService.logout();
        router.navigate(['/login']);
      }
      return throwError(error);
    })
  );
};
