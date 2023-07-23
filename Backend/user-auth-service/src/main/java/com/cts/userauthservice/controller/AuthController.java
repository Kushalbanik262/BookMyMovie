package com.cts.userauthservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cts.userauthservice.dto.LoginRequest;
import com.cts.userauthservice.dto.LoginResponse;
import com.cts.userauthservice.dto.PasswordChangeRequest;
import com.cts.userauthservice.dto.RegistrationRequest;
import com.cts.userauthservice.dto.Response;
import com.cts.userauthservice.dto.ValidationDto;
import com.cts.userauthservice.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication", description = "the user auth API")
@RestController
@CrossOrigin
public class AuthController {
	@Autowired
	private AuthService authService;

	@Operation(summary = "Register new user", description = "Create a new user account with provided credentials.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Successful registration", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "409", description = "Email already exists", content = @Content(schema = @Schema(implementation = Response.class))) })
	@PostMapping("/register")
	public ResponseEntity<Response> register(
			@Parameter(description = "Registration request containing firstname, lastname, email, password and secret-question", required = true) @RequestBody @Valid RegistrationRequest registrationRequest,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return errorResponse(bindingResult.getAllErrors());
		}
		authService.register(registrationRequest);
		return new ResponseEntity<Response>(
				Response.builder().status("success").message("User resgistration successfull").build(),
				HttpStatus.CREATED);
	}

	@Operation(summary = "Login user", description = "Authenticate user and generate a JWT token for accessing protected resources.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful login", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad credentials", content = @Content(schema = @Schema(implementation = Response.class))) })
	@PostMapping("/login")
	public ResponseEntity<?> login(
			@Parameter(description = "Login request containing email and password", required = true) @RequestBody @Valid LoginRequest loginRequest,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return errorResponse(bindingResult.getAllErrors());
		}
		LoginResponse response = authService.login(loginRequest);
		return new ResponseEntity<LoginResponse>(response, HttpStatus.OK);
	}

	@Operation(summary = "Forgot Password", description = "Reset user password after checking the answer to the security question.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Password reset successful", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Response.class))) })
	@PutMapping("/{userid}/forgot")
	public ResponseEntity<Response> forgotPassword(
			@Parameter(description = "Id of the user", required = true) @PathVariable String userid,
			@Parameter(description = "Forgot password request containing new password, security question and answer") @RequestBody @Valid PasswordChangeRequest passwordChangeRequest,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return errorResponse(bindingResult.getAllErrors());
		}
		authService.forgotPassword(userid, passwordChangeRequest);
		return new ResponseEntity<Response>(
				Response.builder().status("success").message("Your password recovered successfully").build(),
				HttpStatus.OK);
	}

	@Operation(summary = "Update Password", description = "Update the current password of the logged in user.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Password update successful", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Response.class))) })
	@PutMapping("/{userid}/updatepassword")
	public ResponseEntity<Response> updatePassword(
			@Parameter(description = "Id of the user", required = true) @PathVariable String userid,
			@Parameter(description = "Update password request containing new password, security question and answer") @RequestBody @Valid PasswordChangeRequest passwordChangeRequest,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return errorResponse(bindingResult.getAllErrors());
		}
		authService.updatePassword(userid, passwordChangeRequest);
		return new ResponseEntity<Response>(
				Response.builder().status("success").message("Your password updated successfully").build(),
				HttpStatus.OK);
	}

	@Operation(summary = "Validate JWT Token", description = "Validate the provided JWT token and check if it is still valid or expired.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Valid JWT token", content = @Content(schema = @Schema(implementation = ValidationDto.class))),
			@ApiResponse(responseCode = "401", description = "Invalid token", content = @Content(schema = @Schema(implementation = ValidationDto.class))),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = Response.class))) })
	@GetMapping("/validate")
	public ResponseEntity<ValidationDto> validateAuthToken(
			@Parameter(description = "JWT token to validate", required = true) @RequestHeader(name = "Authorization") String jwtToken) {
		ValidationDto response = authService.validateAuthToken(jwtToken);
		if (response.isStatus())
			return ResponseEntity.ok(authService.validateAuthToken(jwtToken));
		else
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	private ResponseEntity<Response> errorResponse(List<ObjectError> errors) {
		String errorMsg = errors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(". "));
		return new ResponseEntity<>(Response.builder().status("error").message(errorMsg).build(),
				HttpStatus.BAD_REQUEST);
	}
}
