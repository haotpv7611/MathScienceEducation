package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.ProgressTest;
import com.example.demo.services.impls.ProgressTestServiceImpl;

@CrossOrigin
@RestController
public class ProgressTestController {
	
	@Autowired
	private ProgressTestServiceImpl progressTestServiceImpl;
	
//	@GetMapping("subject/{subjectId}/progressTest")
//	public ResponseEntity<List<ProgressTest>> findBySubjectId(@PathVariable Long subjectId){
//		return ResponseEntity.status(HttpStatus.OK).body(progressTestServiceImpl.findBySubjectId(subjectId));
//	}
}
