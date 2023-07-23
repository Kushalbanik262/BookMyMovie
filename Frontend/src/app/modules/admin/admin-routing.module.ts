import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminLandingComponent } from 'src/app/components/admin-services/admin-landing/admin-landing.component';
import { AddMovieComponent } from 'src/app/components/admin-services/add-movie/add-movie.component';
import { ManageMoviesComponent } from 'src/app/components/admin-services/manage-movies/manage-movies.component';
import { ManageTicketComponent } from 'src/app/components/admin-services/manage-ticket/manage-ticket.component';

const routes: Routes = [
  {
    path:'',
    component:AdminLandingComponent
  },
  {
    path:'addMovie',
    component:AddMovieComponent
  },
  {
    path:'manageMovie',
    component:ManageMoviesComponent
  },
  {
    path:'manageTicket',
    component:ManageTicketComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
