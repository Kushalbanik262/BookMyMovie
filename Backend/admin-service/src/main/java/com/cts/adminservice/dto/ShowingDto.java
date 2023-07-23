package com.cts.adminservice.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ShowingDto {
	@NotBlank(message = "Theater id is required")
	private String theaterId;
	@NotBlank(message = "showTime is required")
	private String showTime;
	@Min(value = 50)
	private int totalSeats;
}
