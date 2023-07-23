package com.cts.userauthservice.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "tb_secret_questions")
@Data
@Builder
public class SecretQuestion {
	@Id
	private Long id;

	@Field
	private String question;
}
