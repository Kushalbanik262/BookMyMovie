package com.cts.adminservice.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddMovieRequest {
	@NotBlank(message = "Title is required")
	private String title;

	@NotBlank(message = "Description is required")
	private String description;

	@NotNull(message = "Release date is required")
	private LocalDate releaseDate;

	@Min(value = 30)
	private int runtime;

	@NotBlank(message = "Genre is required")
	private String genre;

	@NotBlank(message = "Language is required")
	private String language;

	@NotBlank(message = "Country is required")
	private String country;

	@NotBlank(message = "Director is required")
	private String director;

	@NotBlank(message = "Cast is required")
	private String cast;

	@NotBlank(message = "Rating is required")
	private String rating;

	@NotBlank(message = "Poster url is required")
	private String posterUrl;

	@NotBlank(message = "Trailer url is required")
	private String trailerUrl;

	@NotNull(message = "Show cannot be null. Atleat add one show")
	private List<ShowingDto> shows;
}
