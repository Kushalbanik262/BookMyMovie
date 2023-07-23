package com.cts.ticketbookingservice.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "tb_ticket_booking")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketBooking {
	@Id
	private String id;

	@DocumentReference
	private Showing showing;

	@Field
	private User user;

	@Field
	private int numSeats;

	@Field
	private String status;
}
