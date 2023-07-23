import { CanActivate, CanActivateFn, Router, UrlTree } from '@angular/router';
import { AuthenticationService } from '../services/authentication.service';
import { Observable, map, take } from 'rxjs';
import { Injectable } from '@angular/core';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class AdminGuard implements CanActivate {
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
        const isAuthenticated:User | null = user;

        if (isAuthenticated != null && isAuthenticated.role == "ADMIN") return true;
        return this.router.createUrlTree(['/login']);
      })
    );
  }
}
