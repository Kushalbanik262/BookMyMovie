package com.cts.ticketbookingservice.service;

import java.util.List;
import java.util.UUID;

import com.cts.ticketbookingservice.dto.TicketBookingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.ticketbookingservice.dto.BookTicketRequest;
import com.cts.ticketbookingservice.dto.Response;
import com.cts.ticketbookingservice.dto.ValidationDto;
import com.cts.ticketbookingservice.exceptions.InvalidTokenException;
import com.cts.ticketbookingservice.exceptions.ResourceNotFoundException;
import com.cts.ticketbookingservice.feign.AuthClient;
import com.cts.ticketbookingservice.model.Showing;
import com.cts.ticketbookingservice.model.TicketBooking;
import com.cts.ticketbookingservice.model.User;
import com.cts.ticketbookingservice.repository.ShowingRepository;
import com.cts.ticketbookingservice.repository.TicketBookingRepository;
import com.cts.ticketbookingservice.repository.UserRepository;

@Service
@Slf4j
public class TicketBookingService {
	@Autowired
	private AuthClient authClient;
	@Autowired
	private ShowingRepository showingRepository;
	@Autowired
	private TicketBookingRepository bookingRepository;
	@Autowired
	private UserRepository userRepository;

	@Transactional
	public Response bookTicket(String token, BookTicketRequest request) {
		String invalidMsg = "Invalid Token";
		try {
			ValidationDto authResponse = authClient.validateAuthToken(token);
			if (authResponse.isStatus()) {

				// Only CUSTOMER can perform the book ticket operation
				if (authResponse.getRole().equals("CUSTOMER")) {

					// Retrieve the show details
					Showing show = showingRepository.findById(request.getShowingId()).orElseThrow(
							() -> new ResourceNotFoundException("No show found with id: " + request.getShowingId()));

					// Find the remaining seats available for the show
					int remainingSeats = show.getTotalSeats() - show.getBookedSeats();

					// If requested seat count is less than or equal to remaining seats
					if (request.getSeats() <= remainingSeats) {
						show.setBookedSeats(show.getBookedSeats() + request.getSeats());

						// Retrieve the user details
						User user = userRepository.findById(authResponse.getUserId()).orElse(null);

						TicketBooking ticket = TicketBooking.builder().id(generateRandomId()).showing(show).user(user)
								.numSeats(request.getSeats()).status("CONFIRM").build();

						// Save the ticket and show record
						bookingRepository.save(ticket);
						showingRepository.save(show);
						log.info("show is:{}",show);
						// Return response
						return Response.builder().status("success").message("Ticket booking successfull").build();
					} else
						throw new RuntimeException("Ticket not available!");
				} else
					throw new InvalidTokenException("Only a customer can perform ticket book");
			} else
				throw new InvalidTokenException(invalidMsg);
		} catch (Exception e) {
			throw e;
		}
	}

	public TicketBookingResponse findAllBookings(String jwtToken,String userId){
		try{
			ValidationDto authResponse = authClient.validateAuthToken(jwtToken);
			if(!authResponse.isStatus()) {throw new InvalidTokenException("Token Not Valid");}
			//Admin Can't book any ticket
			if(authResponse.getRole().equalsIgnoreCase("ADMIN")){throw new RuntimeException("Admin Can't Book Tickets");}
			List<TicketBooking> bookings = bookingRepository.findBookingsByUserId(userId);
			log.info("Bokkings Are:{}",bookings);
			//Returning the bookings of this specific User
			return TicketBookingResponse.builder().allBookings(bookings).build();
		}
		catch (Exception e){
			throw e;
		}
	}

	private String generateRandomId() {
		return "TB" + UUID.randomUUID().toString();
	}

}
