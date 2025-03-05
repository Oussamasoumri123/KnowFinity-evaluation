import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Key } from './model/appstates';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem(Key.TOKEN); // Retrieve token

    if (token) {
      // Clone request to add the Authorization header
      const clonedRequest = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)

      });
      console.log('Request with token:', clonedRequest);

      return next.handle(clonedRequest);
    }

    // If no token, pass the request as is
    return next.handle(req);
  }
}
