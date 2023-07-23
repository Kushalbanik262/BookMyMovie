package com.cts.moviecatalogservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.cts.moviecatalogservice.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(value = { ResourceNotFoundException.class, RuntimeException.class })
	public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		if (ex instanceof ResourceNotFoundException) {
			status = HttpStatus.NOT_FOUND;
		}
		return new ResponseEntity<>(ErrorResponse.builder().message(ex.getMessage()).build(), status);
	}
}
