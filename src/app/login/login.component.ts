import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AuthService } from '../services/AuthService';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone:true,
  imports: [CommonModule,FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
inscription() {
this.router.navigate(['/register']);
}
forgotPassword() {
  this.router.navigate(['/forgot-password']);
 
}

  email = '';
  password = '';
  error = '';

  constructor(private auth: AuthService, private router: Router) {}

  
  login() {
    this.error = '';
    this.auth.login(this.email, this.password).subscribe({
      next: (res) => {
        // Utiliser access_token (avec underscore) au lieu de accessToken
        this.auth.setToken(res.access_token); // ← CORRECTION ICI
        
        // Stocker aussi le refresh token si nécessaire
        if (res.refresh_token) {
          localStorage.setItem('refresh_token', res.refresh_token);
        }
        
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.error = err.error?.message || 'Erreur de connexion';
        console.error('Login error:', err);
      }
    });
  }
}
