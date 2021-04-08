package com.example.demo.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.ExerciseDTO;
import com.example.demo.dtos.ExerciseRequestDTO;
import com.example.demo.services.IExerciseService;

@CrossOrigin
@RestController
public class ExerciseController {

	@Autowired
	IExerciseService iExerciseService;

	@GetMapping("/lesson/{lessonId}/exercises")
	public ResponseEntity<List<ExerciseDTO>> findByLessonIdOrderByExerciseNameAsc(@PathVariable long lessonId) {
		List<ExerciseDTO> response = iExerciseService.findByLessonIdOrderByExerciseNameAsc(lessonId);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/progressTest/{progressTestId}/exercises")
	public ResponseEntity<List<ExerciseDTO>> findByProgressTestIdOrderByExerciseNameAsc(
			@PathVariable long progressTestId) {
		List<ExerciseDTO> response = iExerciseService.findByProgressTestIdOrderByExerciseNameAsc(progressTestId);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/exercise")
	public ResponseEntity<String> createExercise(@Valid @RequestBody ExerciseRequestDTO exerciseRequestDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "/n" + object.getDefaultMessage();
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		String response = iExerciseService.createExercise(exerciseRequestDTO);
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/exercise/{id}")
	public ResponseEntity<String> updateExercise(@PathVariable long id,
			@Valid @RequestBody ExerciseRequestDTO exerciseRequestDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "/n" + object.getDefaultMessage();
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		String response = iExerciseService.updateExercise(id, exerciseRequestDTO);
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PutMapping("/exercise/delete")
	public ResponseEntity<String> deleteExercise(@RequestParam long id) {
		String response = iExerciseService.deleteExercise(id);
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}
}
