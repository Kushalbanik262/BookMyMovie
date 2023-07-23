package com.cts.ticketbookingservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.cts.ticketbookingservice.dto.TicketBookingResponse;
import com.cts.ticketbookingservice.model.TicketBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import com.cts.ticketbookingservice.dto.BookTicketRequest;
import com.cts.ticketbookingservice.dto.Response;
import com.cts.ticketbookingservice.service.TicketBookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Ticket Booking", description = "ticket booking API")
@RestController
@CrossOrigin
public class TicketBookingController {
	@Autowired
	private TicketBookingService bookingService;

	@Operation(summary = "Book ticket", description = "Book ticket for an upcoming show")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Ticket booking successful", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = "Movie not found", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorize. Only a customer can perform this operation", content = @Content(schema = @Schema(implementation = Response.class))) })
	@PostMapping("/book")
	public ResponseEntity<Response> bookTicket(
			@Parameter(description = "JWT auth token of the application user", required = true) @RequestHeader(name = "Authorization") String token,
			@Parameter(description = "Book ticket request including show id and no of seats", required = true) @RequestBody @Valid BookTicketRequest request,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return errorResponse(bindingResult.getAllErrors());
		}
		return ResponseEntity.ok(bookingService.bookTicket(token, request));
	}

	@Operation(summary = "Get Bookings By UserId", description = "Get All the Bookings A User has")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User Found", content = @Content(schema = @Schema(implementation = TicketBookingResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorize. Only a customer can perform this operation", content = @Content(schema = @Schema(implementation = Response.class))) })
	@GetMapping("/getbookings/{userId}")
	public ResponseEntity<TicketBookingResponse> getBookingsById(
			@RequestHeader(name = "Authorization") String token,
			@PathVariable ("userId") String userId
	){
		return ResponseEntity.ok(bookingService.findAllBookings(token,userId));
	}

	private ResponseEntity<Response> errorResponse(List<ObjectError> errors) {
		String errorMsg = errors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(". "));
		return new ResponseEntity<>(Response.builder().status("error").message(errorMsg).build(),
				HttpStatus.BAD_REQUEST);
	}
}
