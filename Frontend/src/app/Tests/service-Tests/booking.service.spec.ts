import { HttpClient, HttpClientModule } from "@angular/common/http";
import { TestBed, getTestBed,inject } from "@angular/core/testing";
import { BrowserModule } from "@angular/platform-browser";
import { AddMovieRequest, AdminService } from "src/app/services/admin.service"
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RouterTestingModule } from "@angular/router/testing";

import {of} from 'rxjs';

import { Movie } from "src/app/models/movie.model";
import { AuthenticationService } from "src/app/services/authentication.service";
import { User } from "src/app/models/user.model";
import { LoginComponent } from "src/app/components/login/login.component";
import { BookingService, bookingRequest } from "src/app/services/booking.service";
import { show } from "src/app/models/show.model";

describe("Testing All the Operations Related to MovieBooking Service",()=>{
    let service:BookingService;
    let injector:TestBed;
    let httpMock: HttpTestingController;


    beforeEach(async()=>{
        await TestBed.configureTestingModule({
            imports:[
                HttpClientTestingModule,
                RouterTestingModule
            ],
            providers:[BookingService]
        }).compileComponents();
        service= TestBed.get(BookingService);
        injector = getTestBed();
        httpMock = injector.get(HttpTestingController);
    });

    afterEach(() => { //Verify Must be Called After each test to close the http connection for testing
        console.log("After Each Called");
        httpMock.verify();
    });


    it("To Be Created",()=>{
        expect(service).toBeTruthy();
    });

    it("book a show",inject([HttpTestingController,BookingService],
        (httpMock:HttpTestingController,service:BookingService)=>{
    
    let booking:bookingRequest = {
        seats:4,
        showingId:"SHOW1203"
    };
    
    let item:any = {
      msg:"Booking Request Successful"
    };
    
    service.bookShow(booking).subscribe(resp=>{expect(resp).toBe(item);});
    
    const mock = httpMock.expectOne(`${service.SHOW_BOOK_URL}/book`);
    expect(mock.cancelled).toBeFalsy();
    expect(mock.request.method).toEqual('POST'); //Method testing
    mock.flush(item);
    }));



    it("Get Shows By User",inject([HttpTestingController,BookingService],
        (httpMock:HttpTestingController,service:BookingService)=>{
    
    
    
    let item:any = [{
      bookedSeats:4,
      id:"SHOW101",
      url:"http://example.com"
    }];
    
    service.getAllBookingsByUserId("USER1234").subscribe(resp=>{expect(resp).toBe(item);});
    
    const mock = httpMock.expectOne(`${service.SHOW_BOOK_URL}/getbookings/USER1234`);
    expect(mock.cancelled).toBeFalsy();
    expect(mock.request.method).toEqual('GET'); //Method testing
    mock.flush(item);
    }));
   
})