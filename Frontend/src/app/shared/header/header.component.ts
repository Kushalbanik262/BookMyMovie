import { Component } from '@angular/core';
import { Subscription } from 'rxjs';
import { User } from 'src/app/models/user.model';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent {
  isLoggedIn: boolean = false;
  isAdmin: boolean = false;
  userSubscription: Subscription = new Subscription();
  routerLink:string = "/user";

  constructor(private authenticationService: AuthenticationService) {}

  ngOnInit() {
    this.userSubscription = this.authenticationService.user.subscribe(
      (user) => {
        this.isLoggedIn = user ? true : false;
        this.isAdmin = user?.role === 'ADMIN';
      }
    );
  }

  ngOnDestroy() {
    this.userSubscription.unsubscribe();
  }

  onLogout() {
    this.authenticationService.logout();
  }
}
