import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AddMovieRequest, AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-add-movie',
  templateUrl: './add-movie.component.html',
  styleUrls: ['./add-movie.component.css'],
})
export class AddMovieComponent {
  availableTheaters = [
    {
      id: 'T2b764eb8-7b5c-4e86-a119-d10734dfcc77',
      name: 'Cinepolis: Acropolis Mall, Kolkata',
    },
    {
      id: 'T71b82fc1-f31e-4bf5-bd7a-526f96bb1417',
      name: 'Miraj Cinemas: Newtown, Kolkata',
    },
    {
      id: 'T82326e44-37b4-4e10-8b63-41904a6eacc8',
      name: 'INOX: City Center, Salt Lake',
    },
    {
      id: 'Tb38ca66e-f538-4cbc-bd9f-f8365c0b2213',
      name: 'Hind INOX: Kolkata',
    },
  ];
  isLoading = false;

  addMovieRequest: AddMovieRequest = {
    title: '',
    description: '',
    releaseDate: '',
    runtime: 0.0,
    genre: '',
    language: '',
    country: '',
    director: '',
    cast: '',
    rating: '',
    posterUrl: '',
    trailerUrl: '',
    shows: [],
  };

  constructor(
    private adminService: AdminService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  addTheater() {
    this.addMovieRequest.shows.push({
      theaterId: '',
      showTime: '',
      totalSeats: 0,
    });
  }

  removeTheater(index: number) {
    this.addMovieRequest.shows.splice(index, 1);
  }

  submitMovieForm() {
    this.addMovieRequest.shows.forEach((show) => {
      const theater = this.availableTheaters.find(
        (t) => t.id === show.theaterId
      );
      if (theater) {
        show.theaterId = theater.id;
      }
    });
    console.log(this.addMovieRequest);
    this.isLoading = true;
    this.adminService.addNewMovie(this.addMovieRequest).subscribe({
      complete: () => {
        this.isLoading = false;
        this.openSnackBar('Movie added successfully');
        this.router.navigate(['/admin/manageMovie']);
      },
      error: (errorMsg) => {
        this.isLoading = false;
        this.openSnackBar(errorMsg);
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
