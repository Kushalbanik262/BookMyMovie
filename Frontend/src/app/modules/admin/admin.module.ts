import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { AddMovieComponent } from 'src/app/components/admin-services/add-movie/add-movie.component';
import { ManageMoviesComponent } from 'src/app/components/admin-services/manage-movies/manage-movies.component';
import { ManageTicketComponent } from 'src/app/components/admin-services/manage-ticket/manage-ticket.component';
import { MaterialModule } from 'src/app/material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ProgressSpinnerComponent } from 'src/app/shared/progress-spinner/progress-spinner.component';
import { AdminLandingComponent } from 'src/app/components/admin-services/admin-landing/admin-landing.component';


@NgModule({
  declarations: [
    AddMovieComponent,
    ManageMoviesComponent,
    ManageTicketComponent,
    AdminLandingComponent

  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    MaterialModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    ProgressSpinnerComponent
  ]
})
export class AdminModule { }
