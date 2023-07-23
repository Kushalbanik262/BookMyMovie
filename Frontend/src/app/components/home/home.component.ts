import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Movie } from 'src/app/models/movie.model';
import { AllMovieResponse, MovieService } from 'src/app/services/movie.service';
import { Store } from '@ngrx/store';
import { movieLoading } from 'src/app/ngrx/MovieRedux/movie.actions';
import { Subscription } from 'rxjs';
import { MovieState } from 'src/app/ngrx/MovieRedux/movie.reducer';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent {
  isLoading: boolean = false;
  searchQuery: string = '';
  movies: Movie[] = [];
  interVal:any;
  subscribe!:Subscription;

  constructor(
    private movieService: MovieService,
    private snackBar: MatSnackBar,
    private store:Store<any>
  ) {}

  ngOnInit() {
    this.isLoading = true;
    this.store.dispatch(movieLoading());
    this.subscribe = this.store.subscribe({
      next:(mv)=>{this.movies = mv.movie.movies.movies
        this.isLoading = false;
        console.log(`All The Movies Are: ${JSON.stringify(mv.movie.movies.movies)}`);},
      error: (errorMessage) => {
            this.isLoading = false;
            this.openSnackBar(errorMessage);
      }
    });
    // this.movieService.getAllMovies().subscribe({
    //   next: (value) => {
    //     this.movies = value.movies;
    //     this.isLoading = false;
    //   },
    //   error: (errorMessage) => {
    //     this.isLoading = false;
    //     this.openSnackBar(errorMessage);
    //   },
    // });
  }

  get filteredMovies() {
    return this.movies;
    // return this.movies.filter(movie =>
    //   movie.title.toLowerCase().includes(this.searchQuery.toLowerCase())
    // );
  }

  loadMovies(){
    if(this.searchQuery.length == 0 || this.searchQuery.trim().length == 0){return;}
    this.isLoading = true;
    this.movieService.loadMovieByKeyword(this.searchQuery).subscribe({
      next:(val)=>{
        this.movies = val.movies;
      },
      complete:()=>{
        this.isLoading = false;
      },
      error:(errMsg)=>{
        this.isLoading = false;
        this.openSnackBar(errMsg);
      }
    })
  }

  //This is to restrict api calls
  debouncing(){
    clearInterval(this.interVal);
    this.interVal = setTimeout(()=>{
      this.loadMovies();
    },1000);
  }

  openSnackBar(msg: string) {
    this.snackBar.open(msg, 'Ok', {
      horizontalPosition: 'center',
      verticalPosition: 'top',
      duration: 2500,
    });
  }
}
