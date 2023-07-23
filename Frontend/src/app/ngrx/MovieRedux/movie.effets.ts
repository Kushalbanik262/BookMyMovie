import { Injectable } from '@angular/core';
import { Actions,createEffect,ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { MovieService } from 'src/app/services/movie.service';
import { MovieState } from './movie.reducer';
import { movieLoading, movieLoadingFailed, movieLoadingSuccessful } from './movie.actions';

import {mergeMap,concatMap,tap,map,catchError,of} from 'rxjs';

@Injectable({providedIn:"root"})
export class MovieEffects{
    constructor(private action$:Actions,private service:MovieService,private store:Store<MovieState>){}

    loadMovies$ = createEffect(()=>{
        return this.action$.pipe(
            ofType(movieLoading),
            mergeMap(()=>
                   this.service.getAllMovies().pipe(
                    tap((d)=>{console.log(`Movie Fetching ${d}`);}),
                    map(movies=>movieLoadingSuccessful({movies})),
                    catchError((err)=>of(movieLoadingFailed(err)))
                )
            )
        );
    });
}