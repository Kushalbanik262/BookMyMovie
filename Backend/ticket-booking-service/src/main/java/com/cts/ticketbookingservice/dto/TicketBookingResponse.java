package com.cts.ticketbookingservice.dto;

import com.cts.ticketbookingservice.model.TicketBooking;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TicketBookingResponse {
    private List<TicketBooking> allBookings;

}
