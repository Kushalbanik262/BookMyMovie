import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Movie } from 'src/app/models/movie.model';
import { show } from 'src/app/models/show.model';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { BookingService } from 'src/app/services/booking.service';
import { MovieService } from 'src/app/services/movie.service';

@Component({
  selector: 'app-movie-details',
  templateUrl: './movie-details.component.html',
  styleUrls: ['./movie-details.component.css']
})
export class MovieDetailsComponent{
  movie!:Movie;
  isLoading:boolean = true;
  shows!:show[];
  selected =new Array();;
  seats = new Array();
  co:number = 0;

  constructor(private sanitizer:DomSanitizer,
    private _router:ActivatedRoute,
    private movieService:MovieService,
    private snackBar:MatSnackBar,
    private bookingService:BookingService,
    private loginService:AuthenticationService){}
  
  
  isLoggedIn():boolean{
    return this.loginService.isLoggedIn;
  }


  get user(){
    return this.loginService?.actualUser;
  }

  getUserPriviledge():string{
    return this.user?.role;
  }

  makeUrlSafe(url:string){
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

  ngOnInit(): void {
      console.log(this._router.snapshot.paramMap.get("id"));
      let movieId:string | null = this._router.snapshot.paramMap.get("id");
      let subscribe = this.movieService.getMovieById(movieId).subscribe(
        {
          next:(mv)=>{this.movie = mv;},
          complete:()=>{console.log(`Shows:${JSON.stringify(this.movie.shows)}`);this.isLoading = false;subscribe.unsubscribe();
            this.shows = this.movie.shows;
          },
          error:(err)=>{
            console.error(`Error From Movie Loading:${err}`);
            this.openSnackBar(err);
          }
        }
      )
  }

  reserveSeat(i:number){
   
    if(this.selected[i] == 0){this.selected[i] = 1;this.co++;}
    else{this.selected[i] = 0;this.co--;}
    if(this.co > 5){this.openSnackBar("You can only book mat Max five seats at a time");}
  }

  book(show:show){
    this.bookShow(show,this.co);
  }

  private bookShow(currentShow:show,seats:number){
    this.bookingService.bookShow({showingId:currentShow.id,seats:seats})
    .subscribe({
        next:(data)=>{
          this.openSnackBar(data.message);
          console.info(data);
        },
        complete:()=>{console.log(`Completed`);},
        error:(err)=>{
          this.openSnackBar(err);
          console.error(`Error While Booking Tickets:${err}`);
        }
    })
  }
  generateRandom(maxLimit = 100){
    let rand = Math.random() * maxLimit;
  // say 99.81321410836433
  
    rand = Math.floor(rand); // 99
  
    return rand;
  }
  getAvailableSeats(show:show){
    this.seats.splice(0,this.seats.length);
    let availible = show.totalSeats - show.bookedSeats;
    for(let i=0;i<show.totalSeats;i++){
      this.seats.push(i+1);
      this.selected.push(0);
    }
    for(let i=0;i<show.bookedSeats;i++){
       let idx = this.generateRandom(show.totalSeats);
        if(idx >= show.totalSeats){continue;}
        this.seats[idx] = -1;
    }
    //console.log(`Seats Are:${this.seats}`);
  }

  openSnackBar(msg: string) {
    this.snackBar.open(msg, 'Ok', {
      horizontalPosition: 'center',
      verticalPosition: 'top',
      duration: 2500,
    });
  }

  parseInt(num:string):number{
    return this.parseInt(num);
  }

}


