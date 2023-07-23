package com.cts.userauthservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordChangeRequest {
	@NotNull
	private Long securityQuestionId;

	@NotBlank(message = "Answer to secret question is required")
	@Size(min = 3, message = "Secret question answer must be at least 3 characters long")
	private String answer;

	@NotBlank(message = "New password is required")
	@Size(min = 8, message = "Password must be at least 8 characters long")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
	private String newPassword;
}
