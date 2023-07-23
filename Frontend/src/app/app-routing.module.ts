import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { ProfileComponent } from './components/profile/profile.component';
import { AuthGuard } from './guards/auth.guard';
import { MovieDetailsComponent } from './components/movie-details/movie-details.component';
import { UserBookingsComponent } from './components/user-bookings/user-bookings.component';
import { AdminGuard } from './guards/admin.guard';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { 
    path: 'login',
    loadChildren: () => import('./modules/login/login.module').then(m => m.LoginModule)
  },
  {
    path:'details/:id',
    component:MovieDetailsComponent
  },
  {
    path:'user',
    component:UserBookingsComponent,
    canActivate:[AuthGuard]
  },
  {
    path:"admin",
    loadChildren:()=>import("./modules/admin/admin.module").then(m=>m.AdminModule),
    canActivate:[AdminGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
