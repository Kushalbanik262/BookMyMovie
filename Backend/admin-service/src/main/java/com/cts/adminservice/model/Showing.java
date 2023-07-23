package com.cts.adminservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "showing")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Showing {
	@Id
	private String id;

	@DocumentReference
	private Theater theater;

	@DocumentReference
	private Movie movie;

	@Field
	private String showTime;

	@Field
	private int totalSeats;

	@Field
	private int bookedSeats;
}
