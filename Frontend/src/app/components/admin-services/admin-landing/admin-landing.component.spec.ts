import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminLandingComponent } from './admin-landing.component';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { MaterialModule } from 'src/app/material.module';
import { ProgressSpinnerComponent } from 'src/app/shared/progress-spinner/progress-spinner.component';
xdescribe('AdminLandingComponent', () => {
  let component: AdminLandingComponent;
  let fixture: ComponentFixture<AdminLandingComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdminLandingComponent],
      imports:[HttpClientTestingModule,MaterialModule,ProgressSpinnerComponent],
      providers:[AuthenticationService]
    });
    fixture = TestBed.createComponent(AdminLandingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
