package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.ExerciseTakenRequestDTO;
import com.example.demo.services.impls.ExerciseTakenServiceImpl;

@RestController
public class ExerciseTakenController {
	@Autowired
	ExerciseTakenServiceImpl ExerciseTakenServiceImpl;
	
	@PostMapping("/exericseTaken")
	public ResponseEntity<String> doExericse(@RequestBody ExerciseTakenRequestDTO exerciseTakenRequestDTO){
		String response = ExerciseTakenServiceImpl.doExercise(exerciseTakenRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
