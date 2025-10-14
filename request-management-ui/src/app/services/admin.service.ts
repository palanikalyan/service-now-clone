import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SoftwareRequestDto {
  id?: number;
  title: string;
  description: string;
  status?: string;
  createdAt?: string;
}

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  private baseUrl = 'http://localhost:8080/admin';

  constructor(private http: HttpClient) {}

  getAllRequests(): Observable<SoftwareRequestDto[]> {
    return this.http.get<SoftwareRequestDto[]>(`${this.baseUrl}/requests`);
  }

  approveRequest(id: number): Observable<SoftwareRequestDto> {
    return this.http.put<SoftwareRequestDto>(`${this.baseUrl}/requests/${id}/approve`, {});
  }

  rejectRequest(id: number): Observable<SoftwareRequestDto> {
    return this.http.put<SoftwareRequestDto>(`${this.baseUrl}/requests/${id}/reject`, {});
  }
}
