package com.example.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<?> notFoundException(){
		return new ResponseEntity<>("Resource Not Found!", HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseEntity<?> illegalArgumentException(){
		return new ResponseEntity<>("Illegal Id!", HttpStatus.BAD_REQUEST);
	}
}
