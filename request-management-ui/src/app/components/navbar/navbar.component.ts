import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {
  username: string | null = null;
  isAdmin = false;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit() {
    this.updateUsername();
    this.isAdmin = this.authService.isAdmin();
  }

  private updateUsername() {
    this.username = localStorage.getItem('username');
  }

  logout() {
    this.authService.logout();
    this.username = null;
    this.isAdmin = false;
    this.router.navigate(['/login']);
  }
}
