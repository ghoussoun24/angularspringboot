import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/AuthService';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  standalone:true,
  imports: [CommonModule,FormsModule],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {
   step = 1; // 1=email, 2=code + password
  email = '';
  code = '';
  newPassword = '';
  message = '';
  error = '';

  constructor(private auth: AuthService, private router: Router) {}
sendCode() {
  this.error = '';
  this.message = '';
  this.auth.forgotPassword(this.email).subscribe({
    next: res => {
      this.message = res.message;
      this.step = 2; // passer à l'étape suivante
    },
    error: err => this.error = err.error.message
  });
}

// Étape 2 : vérifier code
verifyCode() {
  this.error = '';
  this.message = '';
  this.auth.verifyCode(this.email, this.code).subscribe({
    next: res => {
      this.message = res.message;
      this.step = 3; // passer à l'étape 3
    },
    error: err => this.error = err.error.message
  });
}

// Étape 3 : réinitialiser mot de passe
resetPassword() {
  this.error = '';
  this.message = '';
  this.auth.resetPassword(this.email, this.code, this.newPassword).subscribe({
    next: res => {
      this.message = res.message;
      setTimeout(() => {
        this.router.navigate(['/login']); // redirection vers login
      }, 1500);
    },
    error: err => this.error = err.error.message
  });

}
}
