package com.cts.moviecatalogservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowingDto {
	private String id;
	private String name;
	private String location;
	private String showTime;
	private int totalSeats;
	private int bookedSeats;
}
