package com.cts.adminservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cts.adminservice.dto.AddMovieRequest;
import com.cts.adminservice.dto.Response;
import com.cts.adminservice.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;

@Tag(name = "Admin", description = "admin API")
@RestController
@CrossOrigin
public class AdminController {
	@Autowired
	private AdminService adminService;

	@Operation(summary = "Update ticket status", description = "Update an existing ticket status")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Ticket updation successful", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = "Ticket not found", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorize. Only a admin can perform this operation", content = @Content(schema = @Schema(implementation = Response.class))) })
	@PutMapping("/update/{ticketId}/{newStatus}")
	public ResponseEntity<Response> updateTicketStatus(
			@Parameter(description = "JWT auth token of the application user", required = true) @RequestHeader(name = "Authorization") String token,
			@Parameter(description = "Id of the ticket", required = true) @PathVariable String ticketId,
			@Parameter(description = "New updated ticket status", required = true) @PathVariable String newStatus) {
		adminService.updateTicketStatus(token, ticketId, newStatus);
		return ResponseEntity.ok(Response.builder().status("success").message("Ticket updated successfully").build());
	}

	@Operation(summary = "Add movie", description = "Add a new movie")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "New movie addition successful", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorize. Only a admin can perform this operation", content = @Content(schema = @Schema(implementation = Response.class))) })
	@PostMapping("/addmovie")
	public ResponseEntity<Response> addMovie(
			@Parameter(description = "JWT auth token of the application user", required = true) @RequestHeader(name = "Authorization") String token,
			@Parameter(description = "Add movie request with all details", required = true) @RequestBody @Valid AddMovieRequest addMovieRequest,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return errorResponse(bindingResult.getAllErrors());
		}
		adminService.addMovie(token, addMovieRequest);
		return new ResponseEntity<>(Response.builder().status("success").message("Movie added successsfully").build(),
				HttpStatus.CREATED);
	}

	@Operation(summary = "Delete movie", description = "Delete an existing movie")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Movie deletion successful", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = "Movie not found", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorize. Only a admin can perform this operation", content = @Content(schema = @Schema(implementation = Response.class))) })
	@DeleteMapping("/delete/{movieId}")
	public ResponseEntity<Response> deleteMovie(
			@Parameter(description = "JWT auth token of the application user", required = true) @RequestHeader(name = "Authorization") String token,
			@Parameter(description = "Id of the movie to be deleted", required = true) @PathVariable String movieId) {
		adminService.deleteMovie(token, movieId);
		return ResponseEntity.ok(Response.builder().status("success").message("Movie deleted successfully").build());
	}

	private ResponseEntity<Response> errorResponse(List<ObjectError> errors) {
		String errorMsg = errors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(". "));
		return new ResponseEntity<>(Response.builder().status("error").message(errorMsg).build(),
				HttpStatus.BAD_REQUEST);
	}
}
