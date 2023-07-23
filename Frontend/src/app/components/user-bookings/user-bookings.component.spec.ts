import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UserBookingsComponent } from './user-bookings.component';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { BookingService } from 'src/app/services/booking.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';

import { MatStepperModule } from '@angular/material/stepper';
import { BehaviorSubject, of } from 'rxjs';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { User } from 'src/app/models/user.model';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

xdescribe('UserBookingsComponent', () => {
  let component: UserBookingsComponent;
  let fixture: ComponentFixture<UserBookingsComponent>;
  let authenticationService: AuthenticationService;
  let bookingService: BookingService;
  let snackBar: MatSnackBar;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserBookingsComponent],
      imports: [
        MatCardModule,
        MatDividerModule,
        MatIconModule,
        MatProgressBarModule,
        MatProgressSpinnerModule,
        MatStepperModule,
        HttpClientTestingModule
      ],
      providers: [
        AuthenticationService,
        BookingService,
        MatSnackBar
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserBookingsComponent);
    component = fixture.componentInstance;
    authenticationService = TestBed.inject(AuthenticationService);
    bookingService = TestBed.inject(BookingService);
    snackBar = TestBed.inject(MatSnackBar);
    fixture.detectChanges();
  });

  afterEach(() => {
    fixture.destroy();
  });

  it('should initialize component correctly', () => {
    expect(component).toBeTruthy();
    expect(component.userSubscription).toBeDefined();
    expect(component.isLoading).toBeFalse();
    expect(component.user).toBeUndefined();
    expect(component.load).toBeFalse();
    expect(component.bookings).toBeUndefined();
  });



  it('should unsubscribe userSubscription in ngOnDestroy', () => {
    spyOn(component.userSubscription, 'unsubscribe');

    component.ngOnDestroy();

    expect(component.userSubscription.unsubscribe).toHaveBeenCalled();
  });

  

  it('should call openSnackBar method with correct message', () => {
    const mockMessage = 'Test Message';

    spyOn(snackBar, 'open');

    component.openSnackBar(mockMessage);

    expect(snackBar.open).toHaveBeenCalledWith(mockMessage, 'Ok', {
      horizontalPosition: 'center',
      verticalPosition: 'top',
      duration: 2500
    });
  });
});
