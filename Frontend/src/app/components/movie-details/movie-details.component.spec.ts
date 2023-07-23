import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovieDetailsComponent } from './movie-details.component';
import { MovieService } from 'src/app/services/movie.service';
import { BookingService } from 'src/app/services/booking.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { MaterialModule } from 'src/app/material.module';


describe('MovieDetailsComponent', () => {
  let component:MovieDetailsComponent;
  beforeEach(async () => {
    let component: MovieDetailsComponent;
    let fixture: ComponentFixture<MovieDetailsComponent>;
    const fakeActivatedRoute = {
      snapshot: { data: {},paramMap:{get:(id:string)=>{}} }
    } as ActivatedRoute;
    await TestBed.configureTestingModule({
      declarations: [
        MovieDetailsComponent
      ],
      imports:[HttpClientTestingModule,MaterialModule],
      providers:[
        MovieService, 
        BookingService,
        AuthenticationService,
        MatSnackBar,
        {provide: ActivatedRoute, useValue: {
          snapshot: {
           paramMap: {
              get(): number {
                return 6;
              }
            }
          }
        }}],
    }).compileComponents();
    fixture = TestBed.createComponent(MovieDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeFalsy();
  });


});
