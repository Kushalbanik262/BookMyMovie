package com.cts.ticketbookingservice.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookTicketRequest {
	@NotBlank(message = "Show id is required")
	private String showingId;
	
	@NotNull(message = "No of seats to book is required")
	@Max(value = 5,message = "One cannot book more then 5 tickets for a show")
	@Min(value = 1,message = "Ateast one ticket can be booked")
	private Integer seats;
}
