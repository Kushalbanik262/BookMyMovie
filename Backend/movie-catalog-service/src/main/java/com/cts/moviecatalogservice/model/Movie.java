package com.cts.moviecatalogservice.model;

import java.time.LocalDate;
import java.util.List;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "tb_movie")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {
	@Id
	private String id;

	@Field
	private String title;

	@Field
	private String description;

	@Field
	private LocalDate releaseDate;

	@Field
	private int runtime;

	@Field
	private String genre;

	@Field
	private String language;

	@Field
	private String country;

	@Field
	private String director;

	@Field
	private String cast;

	@Field
	private String rating;

	@Field
	private String posterUrl;

	@Field
	private String trailerUrl;

	@DocumentReference(lazy = true)
    private List<Showing> shows;
}
