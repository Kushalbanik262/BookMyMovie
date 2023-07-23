import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-manage-ticket',
  templateUrl: './manage-ticket.component.html',
  styleUrls: ['./manage-ticket.component.css'],
})
export class ManageTicketComponent {
  isLoading: boolean = false;
  ticketStatusForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private adminService: AdminService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.ticketStatusForm = this.formBuilder.group({
      ticketId: ['', Validators.required],
      ticketStatus: ['', Validators.required],
    });
  }

  updateTicketStatus() {
    if (this.ticketStatusForm.valid) {
      const ticketId = this.ticketStatusForm.value.ticketId;
      const ticketStatus = this.ticketStatusForm.value.ticketStatus;
      this.isLoading = true;

      this.adminService.updateTicketStatus(ticketId, ticketStatus).subscribe({
        complete: () => {
          this.isLoading = false;
          this.openSnackBar('Ticket updated successfully!');
          this.ticketStatusForm.reset();
        },
        error: (errorMsg) => {
          this.isLoading = false;
          this.openSnackBar(errorMsg);
        },
      });
    }
  }

  openSnackBar(msg: string) {
    this.snackBar.open(msg, 'Ok', {
      horizontalPosition: 'center',
      verticalPosition: 'top',
      duration: 2500,
    });
  }
}
