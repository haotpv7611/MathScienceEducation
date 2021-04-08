package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/exerciseGameQuestion/delete")
	public ResponseEntity<String> deleteExerciseOrGameQuestion(@RequestParam long id) {
		String response = iExerciseGameQuestionService.deleteExerciseOrGameQuestion(id);
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}
}
