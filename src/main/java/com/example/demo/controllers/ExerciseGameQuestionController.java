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

import com.example.demo.dtos.ExerciseGameQuestionDTO;
import com.example.demo.services.IExerciseGameQuestionService;

@CrossOrigin
@RestController
public class ExerciseGameQuestionController {

	@Autowired
	IExerciseGameQuestionService iExerciseGameQuestionService;

	@PostMapping("/exerciseGameQuestion")
	public ResponseEntity<String> addQuestionToGameQuestionExercise(
			@RequestBody ExerciseGameQuestionDTO exerciseGameQuestionDTO) {
		String response = iExerciseGameQuestionService.addExerciseOrGameQuestion(exerciseGameQuestionDTO);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/exerciseGameQuestion/delete")
	public ResponseEntity<String> deleteExerciseOrGameQuestion(@RequestParam long id) {
		iExerciseGameQuestionService.deleteExerciseGameQuestion(id);;

		return ResponseEntity.ok("DELETE SUCCESS!");
	}
}
