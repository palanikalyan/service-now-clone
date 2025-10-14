import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CreateRequestDto {
  title: string;
  description: string;
}

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
export class RequestService {
  private baseUrl = 'http://localhost:8080/requests';

  constructor(private http: HttpClient) {}

  createRequest(dto: CreateRequestDto): Observable<SoftwareRequestDto> {
    return this.http.post<SoftwareRequestDto>(this.baseUrl, dto);
  }

  getUserRequests(): Observable<SoftwareRequestDto[]> {
    return this.http.get<SoftwareRequestDto[]>(this.baseUrl);
  }

  getRequestById(id: number): Observable<SoftwareRequestDto> {
    return this.http.get<SoftwareRequestDto>(`${this.baseUrl}/${id}`);
  }
  }
