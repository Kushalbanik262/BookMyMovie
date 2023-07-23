import { HttpClient, HttpErrorResponse, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, throwError,tap,map } from "rxjs";
import { Movie } from "../models/movie.model";
import { Booking } from "../models/booking.model";

export interface bookingRequest{
    showingId:string,
    seats:number
}


@Injectable({
    providedIn:"root"
})
export class BookingService{
    SHOW_BOOK_URL: string =
    'http://localhost:8083/api/v1.0/moviebooking';

    constructor(private http:HttpClient){}

    bookShow(request:bookingRequest) {
        const httpOptions = {
            headers:new HttpHeaders({
                'Authorization':'Bearer '+this.getJwtDetails()
            })
        };
        console.log(httpOptions);
        return this.http
          .post<any>(`${this.SHOW_BOOK_URL}/book`,request)
          .pipe(catchError(this.handleError));
    }

    getAllBookingsByUserId(userId:string){
        return this.http
        .get<any>(`${this.SHOW_BOOK_URL}/getbookings/${userId}`)
        .pipe(
            tap((val)=>{console.log(val);}),
            // map(val=>val.allBookings),
            catchError(this.handleError));
    }

    private handleError(error: HttpErrorResponse) {
        return throwError(() => new Error(error.error.message));
    }

    private getJwtDetails():string{
        let storage = window.localStorage;
        let authToken:string | null = storage.getItem("authData");
        if(!authToken){return "";}
        return JSON.parse(authToken)["jwtToken"];
    }
}