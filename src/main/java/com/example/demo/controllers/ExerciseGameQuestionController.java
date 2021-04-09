package com.example.demo.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
			@Valid @RequestBody ExerciseGameQuestionRequestDTO exerciseGameQuestionRequestDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		
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
			@Valid @RequestBody ExerciseGameQuestionRequestDTO exerciseGameQuestionRequestDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}			

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
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
