package com.cts.adminservice.service;

import java.util.ArrayList;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.InvalidIsolationLevelException;

import com.cts.adminservice.dto.AddMovieRequest;
import com.cts.adminservice.dto.ShowingDto;
import com.cts.adminservice.dto.ValidationDto;
import com.cts.adminservice.exceptions.InvalidTokenException;
import com.cts.adminservice.exceptions.ResourceNotFoundException;
import com.cts.adminservice.feign.AuthClient;
import com.cts.adminservice.model.Movie;
import com.cts.adminservice.model.Showing;
import com.cts.adminservice.model.Theater;
import com.cts.adminservice.model.TicketBooking;
import com.cts.adminservice.repository.MovieRepository;
import com.cts.adminservice.repository.ShowingRepository;
import com.cts.adminservice.repository.TheaterRepository;
import com.cts.adminservice.repository.TicketBookingRepository;

@Service
@Slf4j
public class AdminService {
	@Autowired
	private AuthClient authClient;
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private ShowingRepository showingRepository;
	@Autowired
	private TheaterRepository theaterRepository;
	@Autowired
	private TicketBookingRepository bookingRepository;
	@Autowired
	private MovieProducer movieProducer;

	public boolean updateTicketStatus(String token, String ticketId, String newStatus) {
		if (isAdmin(token)) {
			TicketBooking ticket = bookingRepository.findById(ticketId)
					.orElseThrow(() -> new ResourceNotFoundException("No ticket found with id: " + ticketId));
			ticket.setStatus(newStatus);
			bookingRepository.save(ticket);
			return true;

		} else
			throw new InvalidTokenException("Only admin can update ticket status");
	}

	public Movie addMovie(String token, AddMovieRequest request) {
		if (isAdmin(token)) {
			// Create movie entity object from the request
			Movie movie = Movie.builder().id("M" + generateRandomId()).title(request.getTitle())
					.description(request.getDescription()).releaseDate(request.getReleaseDate())
					.runtime(request.getRuntime()).genre(request.getGenre()).language(request.getLanguage())
					.country(request.getCountry()).director(request.getDirector()).cast(request.getCast())
					.rating(request.getRating()).posterUrl(request.getPosterUrl()).trailerUrl(request.getTrailerUrl())
					.build();

			// Save the movie in the database
			movie.setShows(new ArrayList<Showing>());

			// Create the new movie creation kafka event
			movieProducer.sendMessage("New movie created. Id: " + movie.getId());

			// Foe each show for the movie, create show entity object and save in database
			System.out.println("--------------------------------------------------------------");
			for (ShowingDto dto : request.getShows()) {
				Theater theater = theaterRepository.findById(dto.getTheaterId()).orElseThrow(
						() -> new ResourceNotFoundException("No theater found with id: " + dto.getTheaterId()));

				Showing show = Showing.builder().id("MT" + generateRandomId()).movie(movie).theater(theater)
						.showTime(dto.getShowTime()).totalSeats(dto.getTotalSeats()).bookedSeats(0).build();

				movie.getShows().add(show);
				showingRepository.save(show);
			}
			movieRepository.save(movie);
			return movie;
		} else
			throw new InvalidIsolationLevelException("Only admin can add new movie");

	}

	public boolean deleteMovie(String token, String movieId) {
		if (isAdmin(token)) {
			Movie movie = movieRepository.findById(movieId)
					.orElseThrow(() -> new ResourceNotFoundException("No movie found with id: " + movieId));
			movieRepository.delete(movie);
			return true;
		} else
			throw new InvalidTokenException("Only admin can perform delete movie action");
	}

	public boolean isAdmin(String token) {
		ValidationDto authResponse = authClient.validateAuthToken(token);
		return authResponse.isStatus() && authResponse.getRole().equals("ADMIN");
	}

	private String generateRandomId() {
		return UUID.randomUUID().toString();
	}
}
