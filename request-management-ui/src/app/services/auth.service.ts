import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { LoginRequest, RegisterRequest, AuthResponse } from '../models/auth.models';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) {}

  login(req: LoginRequest): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/login`, req).pipe(
      tap((res) => {
        // Support both old and new backend response
        const accessToken = res.accessToken || res.token;
        const username = res.username || '';
        // role can be a string, roles can be an array
        let roles: string[] = [];
        if (Array.isArray(res.roles)) {
          roles = res.roles;
        } else if (typeof res.role === 'string') {
          roles = [res.role];
        }
        if (accessToken) {
          localStorage.setItem('accessToken', accessToken);
          localStorage.setItem('username', username);
          localStorage.setItem('roles', JSON.stringify(roles));
        }
      })
    );
  }

  isAdmin(): boolean {
    const roles = this.getRoles();
    return roles.includes('ADMIN');
  }

  getRoles(): string[] {
    try {
      return JSON.parse(localStorage.getItem('roles') || '[]');
    } catch {
      return [];
    }
  }

  register(req: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register`, req).pipe(
      tap((res) => {
        if (res?.accessToken) {
          localStorage.setItem('accessToken', res.accessToken);
          localStorage.setItem('username', res.username || '');
        }
      })
    );
  }

  logout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('username');
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('accessToken');
  }
}
