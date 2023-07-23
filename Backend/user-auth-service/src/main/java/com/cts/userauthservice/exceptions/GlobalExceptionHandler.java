package com.cts.userauthservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.cts.userauthservice.dto.Response;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(value = { ResourceNotFoundException.class, EmailAlreadyExistsException.class,
			RuntimeException.class })
	public ResponseEntity<Response> handleException(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		if (ex instanceof ResourceNotFoundException) {
			status = HttpStatus.NOT_FOUND;
		} else if (ex instanceof EmailAlreadyExistsException) {
			status = HttpStatus.CONFLICT;
		}
		return new ResponseEntity<>(Response.builder().status("error").message(ex.getMessage()).build(), status);
	}
}
