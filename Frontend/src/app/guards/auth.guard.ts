import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { AuthenticationService } from '../services/authentication.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) {}

  canActivate():
    | boolean
    | Promise<boolean | UrlTree>
    | Observable<boolean | UrlTree> {
    return this.authenticationService.user.pipe(
      take(1),
      map((user) => {
        const isAuthenticated = !!user;

        if (isAuthenticated) return true;

        return this.router.createUrlTree(['/login']);
      })
    );
  }
}
