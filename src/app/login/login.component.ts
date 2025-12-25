import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { AuthService } from '../services/AuthService';
import { Router } from '@angular/router';
import { FormsModule, NgModel } from '@angular/forms';

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
  emailModel: any; // For template reference

  constructor(private auth: AuthService, private router: Router) {}


  login() {
    this.error = '';

    // Validation: check if email is empty
    if (!this.email.trim()) {
      this.error = 'Email is required';
      return;
    }

    // Validation: check email format
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.email)) {
      this.error = 'Invalid email format';
      return;
    }

    // Validation: check if password is empty
    if (!this.password.trim()) {
      this.error = 'Password is required';
      return;
    }

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
        if (err.status === 401) {
          this.error = 'Email ou mot de passe incorrect';
        } else if (err.status === 400) {
          this.error = err.error?.message || 'Données invalides';
        } else {
          this.error = 'Erreur de connexion';
        }
        console.error('Login error:', err);
      }
    });
  }
}
