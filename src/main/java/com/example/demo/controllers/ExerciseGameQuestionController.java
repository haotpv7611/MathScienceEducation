package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.ExerciseGameQuestionRequestDTO;
import com.example.demo.services.IExerciseGameQuestionService;

@CrossOrigin
@RestController
public class ExerciseGameQuestionController {

	@Autowired
	IExerciseGameQuestionService iExerciseGameQuestionService;

	@PostMapping("/exerciseGameQuestion")
	public ResponseEntity<String> addQuestionToGameQuestionExercise(
			@RequestBody ExerciseGameQuestionRequestDTO exerciseGameQuestionRequestDTO) {
		String response = iExerciseGameQuestionService.addExerciseOrGameQuestion(exerciseGameQuestionRequestDTO);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (response.contains("EXISTED")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/exerciseGameQuestion/delete")
	public ResponseEntity<String> deleteExerciseOrGameQuestion(
			@RequestBody ExerciseGameQuestionRequestDTO exerciseGameQuestionRequestDTO) {
		if (exerciseGameQuestionRequestDTO.getQuestionIds().isEmpty()) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("QUESTION INVALID!");
		} else {
			if (exerciseGameQuestionRequestDTO.getQuestionIds().size() != 1) {
				
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("QUESTION INVALID!");
			}
		}

		String response = iExerciseGameQuestionService.deleteExerciseOrGameQuestion(exerciseGameQuestionRequestDTO);

		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}
}
