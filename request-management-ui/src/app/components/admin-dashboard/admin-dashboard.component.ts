import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from '../../services/admin.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css'],
})
export class AdminDashboardComponent {
  requests: any[] = [];

  constructor(private admin: AdminService) {
    this.load();
  }

  load() {
    this.admin.getAllRequests().subscribe({ next: (r) => (this.requests = r), error: () => (this.requests = []) });
  }

  approve(id: number) {
    this.admin.approveRequest(id).subscribe({ next: () => this.load() });
  }

  reject(id: number) {
    this.admin.rejectRequest(id).subscribe({ next: () => this.load() });
  }
}
