import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AuthGuard } from './guards/auth.guard';


export const routes: Routes = [
     { path: '', redirectTo: 'login', pathMatch: 'full' },     // 👉 Page par défaut = Login
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },

  // Dashboard protégé par JWT
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },

  // Route si URL inconnue
  { path: '**', redirectTo: 'login' }
];
