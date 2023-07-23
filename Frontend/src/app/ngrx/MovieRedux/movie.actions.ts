import { createAction,props } from '@ngrx/store';
import { Movie } from 'src/app/models/movie.model';
import { AllMovieResponse } from 'src/app/services/movie.service';

export const movieLoading = createAction(
    "[Movie] Loading"
);

export const movieLoadingSuccessful = createAction(
    "[Movie] Loading Successful",
    props<{movies:AllMovieResponse}>()
);

export const movieLoadingFailed = createAction(
    "[Movie] Loading Failed",
    props<{error:string}>()
);
