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
  role='';
  error = '';

  constructor(private auth: AuthService, private router: Router) {}

  register() {
    this.error = '';
    this.auth.register({ firstname: this.firstname, lastname: this.lastname, email: this.email, password: this.password, role: 'USER' }).subscribe({
      next: () => this.router.navigate(['/login']),
      error: (err) => this.error = err.error
    });
  }

}
