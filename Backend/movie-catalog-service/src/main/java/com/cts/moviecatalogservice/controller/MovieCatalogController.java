package com.cts.moviecatalogservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cts.moviecatalogservice.dto.AllMovieResponse;
import com.cts.moviecatalogservice.dto.ErrorResponse;
import com.cts.moviecatalogservice.dto.MovieDto;
import com.cts.moviecatalogservice.service.MovieCatalogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Movie Catalog", description = "movie API")
@RestController
@CrossOrigin
public class MovieCatalogController {
	@Autowired
	private MovieCatalogService catalogService;

	@Operation(summary = "View all movies", description = "Get list of currently available movies in theaters")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "All movie retrieve successful", content = @Content(schema = @Schema(implementation = AllMovieResponse.class))) })
	@GetMapping("/all")
	public ResponseEntity<AllMovieResponse> getAllMovies() {
		AllMovieResponse response = catalogService.getAllMovies();
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Search movie by id", description = "Get specific movie details by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Movie details retrieve successful", content = @Content(schema = @Schema(implementation = MovieDto.class))),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
	@GetMapping("/movies/search/id/{movieById}")
	public ResponseEntity<MovieDto> getMovieById(
			@Parameter(description = "Id of the movie", required = true) @PathVariable String movieById) {
		MovieDto movie = catalogService.getMovieById(movieById);
		return ResponseEntity.ok(movie);
	}

	//Add Search BY Name Functionality
	@Operation(summary = "Search movie by title keyword", description = "Get Matching Movies By Searching the Movie Title")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "All movie Related to keyword retrieve successful", content = @Content(schema = @Schema(implementation = AllMovieResponse.class))) })
	@GetMapping("/movies/search/name/{movieName}")
	public ResponseEntity<AllMovieResponse> getAllMatchingMovies(
			@Parameter(description = "The Keyword of title of the movie", required = true)
			@PathVariable("movieName") String movieName){
		AllMovieResponse movies = catalogService.getMovieByName(movieName);
		return ResponseEntity.ok(movies);
	}

}
