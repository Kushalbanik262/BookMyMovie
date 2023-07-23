package com.cts.moviecatalogservice.dto;

import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AllMovieResponse {
	private List<MovieDto> movies;
}
