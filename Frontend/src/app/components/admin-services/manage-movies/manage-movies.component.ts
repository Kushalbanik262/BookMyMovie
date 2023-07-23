import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Movie } from 'src/app/models/movie.model';
import { AdminService } from 'src/app/services/admin.service';
import { MovieService } from 'src/app/services/movie.service';

@Component({
  selector: 'app-manage-movies',
  templateUrl: './manage-movies.component.html',
  styleUrls: ['./manage-movies.component.css'],
})
export class ManageMoviesComponent {
  isLoading: boolean = false;
  movies: Movie[] = [];

  constructor(
    private movieService: MovieService,
    private adminService: AdminService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.isLoading = true;
    this.movieService.getAllMovies().subscribe({
      next: (value) => {
        this.movies = value.movies;
        this.isLoading = false;
      },
      error: (errorMessage) => {
        this.isLoading = false;
        this.openSnackBar(errorMessage);
      },
    });
  }

  deleteMovie(id: string) {
    this.isLoading = true;
    this.adminService.deleteMovieById(id).subscribe({
      next: (value) => {
        this.isLoading = false;
        this.openSnackBar('Movie deleted successfully!');
        this.ngOnInit();
      },
      error: (errorMessage) => {
        this.isLoading = false;
        this.openSnackBar(errorMessage);
      },
    });
  }

  openSnackBar(msg: string) {
    this.snackBar.open(msg, 'Ok', {
      horizontalPosition: 'center',
      verticalPosition: 'top',
      duration: 2500,
    });
  }
}
