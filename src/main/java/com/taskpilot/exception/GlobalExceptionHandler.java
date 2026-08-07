package com.taskpilot.exception;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(TaskNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleTaskNotFoundException(TaskNotFoundException ex) {

	    ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.NOT_FOUND.value(),
	            ex.getMessage()
	    );

	    return ResponseEntity
	            .status(HttpStatus.NOT_FOUND)
	            .body(errorResponse);
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(
	        MethodArgumentNotValidException ex) {

	    Map<String, String> errors = new HashMap<>();

	    ex.getBindingResult().getFieldErrors().forEach(error ->
	            errors.put(error.getField(), error.getDefaultMessage()));

	    return ResponseEntity.badRequest().body(errors);
	}
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String, String>> handleRuntimeException(
	        RuntimeException ex) {

	    Map<String, String> error = new HashMap<>();
	    error.put("message", ex.getMessage());

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(error);
	}

}
