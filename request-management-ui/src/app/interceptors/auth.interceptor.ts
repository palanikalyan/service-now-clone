import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    try {
      const token = localStorage.getItem('accessToken');
      if (token) {
        const cloned = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
          },
        });
        return next.handle(cloned);
      }
    } catch (e) {
      // ignore and continue without auth header
    }
    return next.handle(req);
  }
}

// keep a default export for any code expecting a function
export const authInterceptor = AuthInterceptor;
