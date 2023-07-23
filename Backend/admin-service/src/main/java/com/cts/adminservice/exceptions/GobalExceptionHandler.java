package com.cts.adminservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.cts.adminservice.dto.Response;

@RestControllerAdvice
public class GobalExceptionHandler {
	@ExceptionHandler(value = { ResourceNotFoundException.class, InvalidTokenException.class, RuntimeException.class })
	public ResponseEntity<Response> handleException(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		if (ex instanceof ResourceNotFoundException) {
			status = HttpStatus.NOT_FOUND;
		} else if (ex instanceof InvalidTokenException) {
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<>(Response.builder().status("error").message(ex.getMessage()).build(), status);
	}
}
