import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  username = '';
  password = '';
  email = '';
  error: string | null = null;

  constructor(private router: Router, private auth: AuthService) {}

  submit() {
    this.error = null;
    this.auth
      .register({ username: this.username, password: this.password, email: this.email })
      .subscribe({
        next: () => this.router.navigate(['/user']),
        error: (err) => (this.error = err?.error?.message || 'Registration failed'),
      });
  }
}
