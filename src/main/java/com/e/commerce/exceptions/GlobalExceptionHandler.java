package com.e.commerce.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
		return ResponseEntity.badRequest()
				.body(errors);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException (DataNotFoundException e) {
		e.printStackTrace();
		return ResponseEntity.badRequest().body("something went wrong");
	}

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<Object> dataNotFoundException (DataNotFoundException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(GenericException.class)
	public ResponseEntity<?> handleException(GenericException e) {
		return ResponseEntity
				.status(e.getHttpStatus() != null ?  e.getHttpStatus() : HttpStatus.BAD_REQUEST)
				.body(e.getErrorMessage());
	}

}