// services/AuthService.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AuthenticationResponse {
  access_token: string;
  refresh_token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8081/api/v1/auth';

  constructor(private http: HttpClient) {}

  // -------------------------
  // 🔐 LOGIN
  // -------------------------
  login(email: string, password: string): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(
      `${this.apiUrl}/authenticate`,
      { email, password }
    );
  }

  // -------------------------
  // 📝 REGISTER
  // -------------------------
  register(userData: any): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(
      `${this.apiUrl}/register`,
      userData
    );
  }

  // -------------------------
  // 📌 TOKEN
  // -------------------------
  setToken(token: string): void {
    localStorage.setItem('access_token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('access_token');
  }

  removeToken(): void {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
  }

  logout(): void {
    this.removeToken();
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  // -------------------------
  // 🔄 FORGOT PASSWORD
  // -------------------------

  
  // 2️⃣ Étape : le client tape le code et le nouveau mot de passe
  
 forgotPassword(email: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/forgot-password`, { email });
  }

  verifyCode(email: string, code: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/verify-code`, { email, code });
  }

  resetPassword(email: string, code: string, newPassword: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/reset-password`, { email, code, newPassword });
  }
  // 2️⃣ Vérifier le code envoyé
 
}
