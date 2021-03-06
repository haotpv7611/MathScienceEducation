package com.example.demo.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.example.demo.dtos.ExerciseRequestDTO;
import com.example.demo.dtos.ExerciseResponseDTO;
import com.example.demo.dtos.IdAndStatusDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.services.IExerciseService;

@CrossOrigin
@RestController
public class ExerciseController {

	@Autowired
	private IExerciseService iExerciseService;
	
	//sử dụng để thay đổi nội dung nút open và close game
	@GetMapping("/exercise/{id}/status")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<String> findExerciseStatusById(@PathVariable long id){
		String response = iExerciseService.findExerciseStatusById(id);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
		return ResponseEntity.ok(response);
	}

	@GetMapping("/lesson/{lessonId}/exercises")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<List<ExerciseResponseDTO>> findByLessonIdOrderByExerciseNameAsc(@PathVariable long lessonId) {
		List<ExerciseResponseDTO> response = iExerciseService.findByLessonIdOrderByExerciseNameAsc(lessonId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/lesson/{lessonId}/exercises/student")
	// @PreAuthorize("hasRole('student')")
	public ResponseEntity<List<ExerciseResponseDTO>> findByLessonIdStudentView(@PathVariable long lessonId,
			@RequestParam long accountId) {
		List<ExerciseResponseDTO> response = iExerciseService.findByLessonIdStudentView(lessonId, accountId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/progressTest/{progressTestId}/exercises")
	// @PreAuthorize("hasRole('admin') or hasRole('staff') or hasRole('student')")
	public ResponseEntity<List<ExerciseResponseDTO>> findByProgressTestIdOrderByExerciseNameAsc(
			@PathVariable long progressTestId) {
		List<ExerciseResponseDTO> response = iExerciseService
				.findByProgressTestIdOrderByExerciseNameAsc(progressTestId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/progressTest/{progressTestId}/exercises/student")
	// @PreAuthorize("hasRole('student')")
	public ResponseEntity<List<ExerciseResponseDTO>> findByProgressTestIdStudentView(@PathVariable long progressTestId,
			@RequestParam long accountId) {
		List<ExerciseResponseDTO> response = iExerciseService.findByProgressTestIdStudentView(progressTestId,
				accountId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/exercise")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
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

	@PutMapping("/exercise/{id}")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
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
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (response.contains("EXISTED")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PutMapping("/exercise/delete")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<String> changeExerciseStatus(@RequestBody IdAndStatusDTO idAndStatusDTO) {
		try {
			String response = iExerciseService.changeOneExerciseStatus(idAndStatusDTO);
			if (response.contains("CANNOT")) {

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
			
			return ResponseEntity.ok("DELETE SUCCESS!");
		} catch (Exception e) {
			if (e instanceof ResourceNotFoundException) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND!");
			}

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DELETE FAIL!");
		}
	}
}
