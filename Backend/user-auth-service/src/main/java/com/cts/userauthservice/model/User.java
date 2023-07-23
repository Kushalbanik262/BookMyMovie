package com.cts.userauthservice.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "tb_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
	@Id
	private String userId;

	@Field
	private String firstName;

	@Field
	private String lastName;

	@Field
	private String email;

	@Field
	private String password;

	@Field
	private Role role;

	@DocumentReference
	private SecretQuestion secretQuestion;

	@Field
	private String answerToSecretQuestion;

}