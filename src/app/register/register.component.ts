import { Component } from '@angular/core';
import { AuthService } from '../services/AuthService';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone:true,
  imports: [CommonModule,FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  firstname = '';
  lastname = '';
  email = '';
  password = '';
  role='USER';
  error = '';

  constructor(private auth: AuthService, private router: Router) {}

  register() {
    this.error = '';

    // Validation: check if firstname is empty
    if (!this.firstname.trim()) {
      this.error = 'First name is required';
      return;
    }

    // Validation: check if lastname is empty
    if (!this.lastname.trim()) {
      this.error = 'Last name is required';
      return;
    }

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

    this.auth.register({ firstname: this.firstname, lastname: this.lastname, email: this.email, password: this.password, role: this.role }).subscribe({
      next: () => this.router.navigate(['/login']),
      error: (err) => this.error = err.error?.message || 'Registration failed'
    });
  }

}
