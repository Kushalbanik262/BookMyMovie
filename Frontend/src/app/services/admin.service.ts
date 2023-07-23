import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Movie } from '../models/movie.model';

export interface Shows {
  theaterId: string;
  showTime: string;
  totalSeats: number;
}

export interface AddMovieRequest {
  title: string;
  description: string;
  releaseDate: string;
  runtime: number;
  genre: string;
  language: string;
  country: string;
  director: string;
  cast: string;
  rating: string;
  posterUrl: string;
  trailerUrl: string;
  shows: Shows[];
}

@Injectable({ providedIn: 'root' })
export class AdminService {
  ADMIN_SERVICE_URL: string = 'http://localhost:8084/api/v1.0/moviebooking';

  constructor(private http: HttpClient) {}

  updateTicketStatus(ticketId: string, status: string) {
    return this.http
      .put(
        `${this.ADMIN_SERVICE_URL}/update/${ticketId}/${status.toUpperCase()}`,
        {}
      )
      .pipe(catchError(this.handleError));
  }

  addNewMovie(movie: AddMovieRequest) {
    return this.http
      .post(`${this.ADMIN_SERVICE_URL}/addmovie`, movie)
      .pipe(catchError(this.handleError));
  }

  deleteMovieById(id: string) {
    return this.http
      .delete(`${this.ADMIN_SERVICE_URL}/delete/${id}`)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    return throwError(() => new Error(error.error.message));
  }
}
