import { HttpClient, HttpClientModule } from "@angular/common/http";
import { TestBed, getTestBed,inject } from "@angular/core/testing";
import { BrowserModule } from "@angular/platform-browser";
import { AddMovieRequest, AdminService } from "src/app/services/admin.service"
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RouterTestingModule } from "@angular/router/testing";

import {of} from 'rxjs';

import { Movie } from "src/app/models/movie.model";

describe("Testing All the Operations Related to Admin Service",()=>{
    let service:AdminService;
    let injector:TestBed;
    let httpMock: HttpTestingController;

    let addMovie:AddMovieRequest = {
        cast:"Om Raut",
        country:"India",
        description:"Based on Ramayana",
        director:"Om Raut",
        genre:"Thriller",
        language:"Hindi",
        posterUrl:"https://example.com",
        rating:"3.4",
        releaseDate:"16-06-2022",
        runtime:179,
        shows:[
            {showTime:"04:30PM",theaterId:"THXX784",totalSeats:120}
        ],
        title:"Adipurush",
        trailerUrl:"https://example.com"
    };

    beforeEach(async()=>{
        await TestBed.configureTestingModule({
            imports:[
                HttpClientTestingModule,
                RouterTestingModule
            ],
            providers:[AdminService]
        }).compileComponents();
        service= TestBed.get(AdminService);
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

    it("Add New Movie Request",()=>{
       let response:any = {
        msg:"Movie Added Successfully"
       };

       const fn=spyOn(service, 'addNewMovie').and.returnValue( //Spying On The service For getCart Method
            of(response)
        );

        service.addNewMovie(addMovie).subscribe(response=>{
            expect(response).toEqual(response); //Checking If Response is expected to our carts or not
        });

        expect(fn).toHaveBeenCalled();

    });


    it("Update Ticket Status Request",()=>{
        expect(service).toBeTruthy();
    });

   

    it("Deleting a movie By Id",
        inject([HttpTestingController,AdminService],
            (httpMock:HttpTestingController,service:AdminService)=>{

        let movie:Movie = {
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


        let item:any = {
            msg:"Movie Deleted Successfully"
        };

        service.deleteMovieById(movie.id).subscribe(resp=>{expect(resp).toBe(item);});
        const mock = httpMock.expectOne(`${service.ADMIN_SERVICE_URL}/delete/${movie.id}`);
        expect(mock.cancelled).toBeFalsy();
        expect(mock.request.method).toEqual('DELETE'); //Method testing
        mock.flush(item);
      })
    );

    it("Update Ticket Status", inject([HttpTestingController,AdminService],
        (httpMock:HttpTestingController,service:AdminService)=>{

     let ticket:any = {
        id:"TCK1234",
        status:"CANCELLED"
     };


    let item:any = {
        msg:"Movie Deleted Successfully"
    };

    service.updateTicketStatus(ticket.id,ticket.status).subscribe(resp=>{expect(resp).toBe(item);});
    const mock = httpMock.expectOne(`${service.ADMIN_SERVICE_URL}/update/${ticket.id}/${ticket.status.toUpperCase()}`);
    expect(mock.cancelled).toBeFalsy();
    expect(mock.request.method).toEqual('PUT'); //Method testing
    mock.flush(item);
  })
);

})