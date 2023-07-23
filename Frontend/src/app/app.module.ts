import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { HomeComponent } from './components/home/home.component';
import { HeaderComponent } from './shared/header/header.component';
import { MaterialModule } from './material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ProgressSpinnerComponent } from './shared/progress-spinner/progress-spinner.component';
import { ProfileComponent } from './components/profile/profile.component';
import { LoginModule } from './modules/login/login.module';
import { MatSpinner } from '@angular/material/progress-spinner';
import { MovieDetailsComponent } from './components/movie-details/movie-details.component';
import { UserBookingsComponent } from './components/user-bookings/user-bookings.component';
import { AuthInterceptorService } from './services/auth-interceptor.service';
import { AdminLandingComponent } from './components/admin-services/admin-landing/admin-landing.component';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { MovieEffects } from './ngrx/MovieRedux/movie.effets';
import { movieReducer } from './ngrx/MovieRedux/movie.reducer';


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    HeaderComponent,
    MovieDetailsComponent,
    UserBookingsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    ProgressSpinnerComponent,
    StoreModule.forRoot({"movie":movieReducer}),
    EffectsModule.forRoot([MovieEffects]),
    StoreDevtoolsModule.instrument()
  ],
  providers: [
    {
      provide:HTTP_INTERCEPTORS,
      useClass:AuthInterceptorService,
      multi:true
    }
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
