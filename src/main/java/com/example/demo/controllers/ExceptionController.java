package com.example.demo.controllers;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
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
	
//	@ExceptionHandler(value = IllegalArgumentException.class)
//	public ResponseEntity<?> illegalArgumentException(){
//		System.out.println("run");
//		return new ResponseEntity<>("Illegal Id!", HttpStatus.BAD_REQUEST);
//	}
	
	@ExceptionHandler(value = SizeLimitExceededException.class)
	public ResponseEntity<?> sizeLimitExceededException(){
		
		return new ResponseEntity<>("Too Large!", HttpStatus.BAD_REQUEST);
	}
}
