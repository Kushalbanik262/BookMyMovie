package com.cts.moviecatalogservice.model;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "theater")
public class Theater {
	@Id
	private String id;

	@Field
	private String name;

	@Field
	private String location;

	@DocumentReference
	private List<Showing> shows;
}
