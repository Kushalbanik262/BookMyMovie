package com.cts.moviecatalogservice.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {
	private String id;
	private String title;
	private String description;
	private LocalDate releaseDate;
	private int runtime;
	private String genre;
	private String language;
	private String country;
	private String director;
	private String cast;
	private String rating;
	private String posterUrl;
	private String trailerUrl;
	private List<ShowingDto> shows;
}
