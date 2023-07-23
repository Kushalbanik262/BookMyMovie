import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent {
  userId: string = '';
  firstName: string = '';
  lastName: string = '';
  role: string = '';
  email: string = '';
  updateFlag:boolean = false;
  isLoading:boolean = false;

  securityQuestions = [
    { id: 1, question: 'What is your mother"s maiden name?' },
    { id: 2, question: 'What is your favorite color?' },
    { id: 3, question: 'What was the name of your first pet?' },
    { id: 4, question: 'In what city were you born?' },
    { id: 5, question: 'What is the name of your favorite teacher?' },
  ];

  userSubscription: Subscription = new Subscription();
  constructor(private authenticationService: AuthenticationService, private snackBar: MatSnackBar) {}

  updatePasswordForm = new FormGroup({
    userId:new FormControl(this.userId),
    securityQuestionId:new FormControl(1,[Validators.required]),
    answer: new FormControl('', [Validators.required]),
    newPassword: new FormControl('',[Validators.required,Validators.minLength(8)])
  })

  ngOnInit() {
    this.userSubscription = this.authenticationService.user.subscribe(
      (user) => {
        if (user) {
          this.userId = user.userId;
          this.firstName = user.firstName;
          this.lastName = user.lastName;
          this.role = user.role;
          this.email = user.email;
          this.updatePasswordForm.patchValue({userId:this.userId});
        }
      }
    );
  }

  ngOnDestroy() {
    this.userSubscription.unsubscribe();
  }

  updatePassword(){
    console.log("Updating Password");
    this.updateFlag = true;

  }


  get formUserId(){
    return this.updatePasswordForm.get("userId");
  }

  get answer(){
    return this.updatePasswordForm.get("answer");
  }

  get newPassword(){
    return this.updatePasswordForm.get("newPassword");
  }

  get SecurityQuestionId(){
    return this.updatePasswordForm.get("securityQuestionId");
  }

  onSubmit(){
    console.log(this.updatePasswordForm.value);
    if(!this.updatePasswordForm.valid){console.error("Form is Not Validated");return;}
    this.isLoading = true;
    this.authenticationService.updatePassword(this.formUserId?.value,{
      securityQuestionId:this.SecurityQuestionId?.value,
      answer:this.answer?.value,
      newPassword:this.newPassword?.value
    })
    .subscribe({
      complete:()=>{
        this.isLoading = false;
        this.openSnackBar(`Your Password has Been updated successfully`);
      },
      error:(err)=>{
        this.isLoading = false;
        this.openSnackBar(`${err}`);
      }
    })
  }

  openSnackBar(msg: string) {
    this.snackBar.open(msg, 'Ok', {
      horizontalPosition: 'center',
      verticalPosition: 'top',
      duration: 3000,
    });
  }
}
