import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css'],
})
export class ForgotPasswordComponent {
  securityQuestions = [
    { id: 1, question: 'What is your mother"s maiden name?' },
    { id: 2, question: 'What is your favorite color?' },
    { id: 3, question: 'What was the name of your first pet?' },
    { id: 4, question: 'In what city were you born?' },
    { id: 5, question: 'What is the name of your favorite teacher?' },
  ];
  isLoading: boolean = false;

  forgotPasswordForm = new FormGroup({
    userId: new FormControl('', [Validators.required]),
    securityQuestionId: new FormControl(1, [Validators.required]),
    answer: new FormControl('', [Validators.required]),
    newPassword: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
    ]),
  });

  userSubscription: Subscription = new Subscription();

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  get userId() {
    return this.forgotPasswordForm.get('userId');
  }

  get securityQuestionId() {
    return this.forgotPasswordForm.get('securityQuestionId');
  }

  get answer() {
    return this.forgotPasswordForm.get('answer');
  }

  get newPassword() {
    return this.forgotPasswordForm.get('newPassword');
  }

  ngOnInit() {
    this.userSubscription = this.authenticationService.user.subscribe(
      (user) => {
        if (user) {
          this.router.navigate(['./home']);
        }
      }
    );
  }

  ngOnDestroy() {
    this.userSubscription.unsubscribe();
  }

  onSubmit() {
    if (this.forgotPasswordForm.valid) {
      this.isLoading = true;
      this.authenticationService
        .forgotPassword(this.userId?.value, {
          securityQuestionId: this.securityQuestionId?.value,
          answer: this.answer?.value,
          newPassword: this.newPassword?.value,
        })
        .subscribe({
          complete: () => {
            this.isLoading = false;
            this.openSnackBar('Your password recovered successfully');
          },
          error: (errorMessage) => {
            this.isLoading = false;
            this.openSnackBar(errorMessage);
          },
        });
    }
  }

  openSnackBar(msg: string) {
    this.snackBar.open(msg, 'Ok', {
      horizontalPosition: 'center',
      verticalPosition: 'top',
      duration: 2500,
    });
  }
}
