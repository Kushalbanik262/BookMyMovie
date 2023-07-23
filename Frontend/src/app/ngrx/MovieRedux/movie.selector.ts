import { Movie } from "src/app/models/movie.model";
import { MovieAppState } from "./movie.state";
import { createSelector } from '@ngrx/store';
import { MovieState } from "./movie.reducer";

export const moviesAll = (movies:MovieAppState)=>movies.movie;


export const movieSelector:any = createSelector(
    moviesAll,
    (movie:MovieState)=>movie.movies
);