import { Movie } from "src/app/models/movie.model";
import { createReducer, on } from '@ngrx/store';
import { movieLoading, movieLoadingFailed, movieLoadingSuccessful } from "./movie.actions";
import { AllMovieResponse } from "src/app/services/movie.service";

export interface MovieState{
    movies:AllMovieResponse,
    isLoading:boolean,
    isLoaded:boolean,
    updated:string,
    error:string
};

function getCurrent(){
    return new Date().toLocaleTimeString();
}

export const initialState:MovieState ={
    movies:{movies:[]},
    isLoading:false,
    isLoaded:false,
    updated:getCurrent(),
    error:""
} 

export const movieReducer = createReducer(
    initialState,
    on(movieLoading,(state)=>({
        ...state,
        isLoaded:false,
        isLoading:true,
        updated:getCurrent()
    })),

    on(movieLoadingSuccessful,(state,action)=>({
        ...state,
        isLoaded:true,
        isLoading:false,
        movies:action.movies,
        updated:getCurrent()
    })),

    on(movieLoadingFailed,(state,action)=>({
        ...state,
        isLoaded:true,
        isLoading:false,
        error:action.error,
        updated:getCurrent()
    }))
);
