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
import { AllMovieResponse, MovieService } from "src/app/services/movie.service";


describe("Testing All the Operations Related to Movie Service",()=>{
    let service:MovieService;
    let injector:TestBed;
    let httpMock: HttpTestingController;


    beforeEach(async()=>{
        await TestBed.configureTestingModule({
            imports:[
                HttpClientTestingModule,
                RouterTestingModule
            ],
            providers:[MovieService]
        }).compileComponents();
        service= TestBed.get(MovieService);
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

    
    it("Get All Movies",inject([HttpTestingController,MovieService],
        (httpMock:HttpTestingController,service:MovieService)=>{
    
    let item:AllMovieResponse = {
        movies:[
            {
            cast:"Om Raut",
            country:"India",
            description:"Based on Ramayana",
            director:"Om Raut",
            genre:"Thriller",
            language:"Hindi",
            posterUrl:"https://example.com",
            rating:3.4,
            releaseDate:"16-06-2022",
            runtime:179,
            shows:[
                {showTime:"04:30PM",totalSeats:120,bookedSeats:0,id:"SH101",location:"kolkata",name:"INOX"}
            ],
            title:"Adipurush",
            trailerUrl:"https://example.com",
            id:"MVE45455"
            }
        ]
    }
    service.getAllMovies().subscribe(resp=>{expect(resp).toBe(item);});
    
    const mock = httpMock.expectOne(`${service.MOVIE_CATALOG_SERVICE_URL}/all`);
    expect(mock.cancelled).toBeFalsy();
    expect(mock.request.method).toEqual('GET'); //Method testing
    mock.flush(item);
    }));

    it("Load Movies By Keyword",inject([HttpTestingController,MovieService],
        (httpMock:HttpTestingController,service:MovieService)=>{
    
    let item:AllMovieResponse = {
        movies:[
            {
            cast:"Om Raut",
            country:"India",
            description:"Based on Ramayana",
            director:"Om Raut",
            genre:"Thriller",
            language:"Hindi",
            posterUrl:"https://example.com",
            rating:3.4,
            releaseDate:"16-06-2022",
            runtime:179,
            shows:[
                {showTime:"04:30PM",totalSeats:120,bookedSeats:0,id:"SH101",location:"kolkata",name:"INOX"}
            ],
            title:"Adipurush",
            trailerUrl:"https://example.com",
            id:"MVE45455"
            }
        ]
    }
    service.loadMovieByKeyword("Adi").subscribe(resp=>{expect(resp).toBe(item);});
    
    const mock = httpMock.expectOne(`${service.MOVIE_CATALOG_SERVICE_URL}/movies/search/name/Adi`);
    expect(mock.cancelled).toBeFalsy();
    expect(mock.request.method).toEqual('GET'); //Method testing
    mock.flush(item);
    }));
  
    it("Get Movie By Id",inject([HttpTestingController,MovieService],
        (httpMock:HttpTestingController,service:MovieService)=>{
    
    let movie:Movie = 
            {
            cast:"Om Raut",
            country:"India",
            description:"Based on Ramayana",
            director:"Om Raut",
            genre:"Thriller",
            language:"Hindi",
            posterUrl:"https://example.com",
            rating:3.4,
            releaseDate:"16-06-2022",
            runtime:179,
            shows:[
                {showTime:"04:30PM",totalSeats:120,bookedSeats:0,id:"SH101",location:"kolkata",name:"INOX"}
            ],
            title:"Adipurush",
            trailerUrl:"https://example.com",
            id:"MVE45455"
        };

    service.getMovieById(movie.id).subscribe(resp=>{expect(resp).toBe(movie);});
    
    const mock = httpMock.expectOne(`${service.MOVIE_CATALOG_SERVICE_URL}/movies/search/id/${movie.id}`);
    expect(mock.cancelled).toBeFalsy();
    expect(mock.request.method).toEqual('GET'); //Method testing
    mock.flush(movie);
    }));
  
    


});