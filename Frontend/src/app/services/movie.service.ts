import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { throwError,tap, Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Movie } from '../models/movie.model';

export interface AllMovieResponse {
  movies: Movie[];
}

@Injectable({ providedIn: 'root' })
export class MovieService {
  MOVIE_CATALOG_SERVICE_URL: string =
    'http://localhost:8082/api/v1.0/moviebooking';

  constructor(private http: HttpClient) {}

  getAllMovies():Observable<AllMovieResponse> {
    return this.http
      .get<AllMovieResponse>(this.MOVIE_CATALOG_SERVICE_URL + '/all')
      .pipe(catchError(this.handleError));
  }

  loadMovieByKeyword(keyword:string){
    return this.http
      .get<AllMovieResponse>(`${this.MOVIE_CATALOG_SERVICE_URL}/movies/search/name/${keyword}`)
      .pipe(
        tap((d)=>{console.log(`Received:${JSON.stringify(d)}`)}),
        catchError(this.handleError));
  }

  getMovieById(id: string|null) {
    return this.http
      .get<Movie>(`${this.MOVIE_CATALOG_SERVICE_URL}/movies/search/id/${id}`)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    return throwError(() => new Error(error.error.message));
  }
}
