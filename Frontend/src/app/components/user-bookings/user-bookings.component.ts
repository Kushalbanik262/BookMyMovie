import { Component } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { BookingService } from 'src/app/services/booking.service';
import { Subscription } from 'rxjs';
import { User } from 'src/app/models/user.model';
import { Booking } from 'src/app/models/booking.model';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-user-bookings',
  templateUrl: './user-bookings.component.html',
  styleUrls: ['./user-bookings.component.css']
})
export class UserBookingsComponent {
  userSubscription: Subscription = new Subscription();
  isLoading: boolean = false;
  user!:User;
  load:boolean = false;
  bookings!:any[];
  constructor(
    private bookingService:BookingService,
    private authenticationService: AuthenticationService,
    private snackBar:MatSnackBar){}

  ngOnInit(): void {
    this.userSubscription = this.authenticationService.user.subscribe({
      next:(user)=>{
        if(user){this.user = user;}
      }
    });
  }

  ngOnDestroy(): void {
    this.userSubscription.unsubscribe();
  }

  ngAfterViewChecked(): void {
    //Called after every check of the component's view. Applies to components only.
    //Add 'implements AfterViewChecked' to the class.
    console.log(this.user);
  }

  seeBookings(){
    this.load = true;
    this.isLoading = true;
    this.bookingService.getAllBookingsByUserId(this.user.userId).subscribe({
      next:(bookings)=>{this.bookings = bookings.allBookings;},
      complete:()=>{
        console.log(this.bookings);
        this.isLoading = false;
        //this.openSnackBar(``);
      },
      error:(err)=>{
        console.error(`${err}`);
        this.openSnackBar(err);
      }
    })
  }

  openSnackBar(msg: string) {
    this.snackBar.open(msg, 'Ok', {
      horizontalPosition: 'center',
      verticalPosition: 'top',
      duration: 2500,
    });
  }

}
