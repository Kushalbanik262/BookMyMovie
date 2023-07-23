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

describe("Testing All the Operations Related to Authentication Service",()=>{
    let service:AuthenticationService;
    let injector:TestBed;
    let httpMock: HttpTestingController;


    beforeEach(async()=>{
        await TestBed.configureTestingModule({
            imports:[
                HttpClientTestingModule,
                RouterTestingModule.withRoutes([
                    { path: 'login', component: LoginComponent}
                ])
            ],
            providers:[AuthenticationService],
         
        }).compileComponents();
        service= TestBed.get(AuthenticationService);
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

    it("Login",()=>{
       let response:User = {
            email:"kushalbanik34@gmail.com",
            firstName:"kushal",
            lastName:"banik",
            jwtToken:"TOKENEXP16452342345",
            role:"USER",
            userId:"USID1147"
       };

       const fn=spyOn(service, 'login').and.returnValue( //Spying On The service For getCart Method
            of(response)
        );

        service.login({email:response.email,password:"PASSEXP124"}).subscribe(response=>{
            expect(response).toEqual(response); //Checking If Response is expected to our carts or not
        });

        expect(fn).toHaveBeenCalled();
    });


   

it("Forgot Password", inject([HttpTestingController,AuthenticationService],
    (httpMock:HttpTestingController,service:AuthenticationService)=>{


let item:any = {
    msg:"Password Recovered Successfully"
};

service.forgotPassword("ID1234",{
    answer:"What is Your Pet's Name",
    newPassword:"Kushal@1234",
    securityQuestionId:1
}).subscribe(resp=>{expect(resp).toBe(item);});

const mock = httpMock.expectOne(`${service.USER_AUTH_SERVICE_URL}/${"ID1234"}/forgot`);
expect(mock.cancelled).toBeFalsy();
expect(mock.request.method).toEqual('PUT'); //Method testing
mock.flush(item);
})
);


it("Register User", inject([HttpTestingController,AuthenticationService],
    (httpMock:HttpTestingController,service:AuthenticationService)=>{



let item:any = {
    msg:"Registration Done"
};

service.signup({
    firstName:"kushal",
    lastName:"banik",
    email:"kushalbanik93@gmail.com",
    answerToSecretQuestion:"Michu",
    password:"Kushal@123",
    secretQuestionId:1
}).subscribe(resp=>{expect(resp).toBe(item);});

const mock = httpMock.expectOne(`${service.USER_AUTH_SERVICE_URL}/register`);
expect(mock.cancelled).toBeFalsy();
expect(mock.request.method).toEqual('POST'); //Method testing
mock.flush(item);
})
);


it("Update Password", inject([HttpTestingController,AuthenticationService],
    (httpMock:HttpTestingController,service:AuthenticationService)=>{


let item:any = {
    msg:"Password Updated Successfully"
};

service.updatePassword("ID1234",{
    answer:"What is Your Pet's Name",
    newPassword:"Kushal@1234",
    securityQuestionId:1
}).subscribe(resp=>{expect(resp).toBe(item);});

const mock = httpMock.expectOne(`${service.USER_AUTH_SERVICE_URL}/${"ID1234"}/updatepassword`);
expect(mock.cancelled).toBeFalsy();
expect(mock.request.method).toEqual('PUT'); //Method testing
mock.flush(item);
})
);

it("Update Password", inject([HttpTestingController,AuthenticationService],
    (httpMock:HttpTestingController,service:AuthenticationService)=>{


let item:any = {
    msg:"Password Updated Successfully"
};

service.updatePassword("ID1234",{
    answer:"What is Your Pet's Name",
    newPassword:"Kushal@1234",
    securityQuestionId:1
}).subscribe(resp=>{expect(resp).toBe(item);});

const mock = httpMock.expectOne(`${service.USER_AUTH_SERVICE_URL}/${"ID1234"}/updatepassword`);
expect(mock.cancelled).toBeFalsy();
expect(mock.request.method).toEqual('PUT'); //Method testing
mock.flush(item);
})
);

   
})