package com.e.commerce.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
		return ResponseEntity.badRequest()
				.body(errors);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException (Exception e) {
		log.error("Exception :::{}" , e.getMessage());
		e.printStackTrace();
		return ResponseEntity.badRequest().body("something went wrong");
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> AccessDeniedException(AccessDeniedException e) {
//		log.error("AccessDeniedException :::{}" , e.getMessage());
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Object> AuthenticationException(AuthenticationException e) {
//		log.error("AuthenticationException :::{}" , e.getMessage());
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<Object> dataNotFoundException (DataNotFoundException e) {
//		log.error("dataNotFoundException :::{}" , e.getMessage());
		return ResponseEntity.badRequest().body(e.getMessage());
	}


	@ExceptionHandler(GenericException.class)
	public ResponseEntity<Object> handleException(GenericException e) {
		return ResponseEntity
				.status(e.getHttpStatus() != null ?  e.getHttpStatus() : HttpStatus.BAD_REQUEST)
				.body(e.getErrorMessage());
	}

}