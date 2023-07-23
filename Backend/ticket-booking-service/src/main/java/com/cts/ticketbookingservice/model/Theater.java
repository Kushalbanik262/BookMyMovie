package com.cts.ticketbookingservice.model;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "theater")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Theater {
	@Id
	private String id;

	@Field
	private String name;

	@Field
	private String location;

	@JsonIgnore
	@DocumentReference
	private List<Showing> shows;
}
