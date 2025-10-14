import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  username = '';
  password = '';
  error: string | null = null;

  constructor(private router: Router, private auth: AuthService) {}

  submit() {
    this.error = null;
    if (!this.username || !this.password) {
      this.error = 'Please enter both username and password';
      return;
    }
    this.auth
      .login({ username: this.username, password: this.password })
      .subscribe({
        next: (response) => {
          console.log('Login successful');
          console.log('Login response:', response);
          if (response.roles && Array.isArray(response.roles)) {
            if (response.roles.includes('ADMIN')) {
              this.router.navigate(['/admin']);
            } else {
              this.router.navigate(['/user']);
            }
          } else {
            // fallback: always go to user dashboard if roles missing
            this.router.navigate(['/user']);
          }
        },
        error: (err) => {
          console.error('Login error:', err);
          this.error = err?.error?.message || 'Login failed';
        },
      });
  }
}
