import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RequestService } from '../../services/request.service';

@Component({
  selector: 'app-user-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.css'],
})
export class UserDashboardComponent {
  requests: any[] = [];
  title = '';
  description = '';

  constructor(private reqSvc: RequestService) {
    this.load();
  }

  load() {
    this.reqSvc.getUserRequests().subscribe({ next: (r) => (this.requests = r), error: () => (this.requests = []) });
  }

  create() {
    if (!this.title) return;
    this.reqSvc.createRequest({ title: this.title, description: this.description }).subscribe({ next: () => this.load() });
  }
}
