package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.ExerciseDTO;
import com.example.demo.services.IExerciseService;

@CrossOrigin
@RestController
public class ExerciseController  {

	@Autowired
	IExerciseService iExerciseService;
	
	@GetMapping("/lesson/{lessonId}/exercise")
	public ResponseEntity<List<ExerciseDTO>> findByLessonIdAndIsDisableOrderByExerciseNameAsc(@PathVariable Long lessonId){
		List<ExerciseDTO> response = iExerciseService.findByLessonIdAndIsDisableOrderByExerciseNameAsc(lessonId);
		if(response.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/progressTest/{progressTestId}/exercise")
	public ResponseEntity<List<ExerciseDTO>> findByProgressTestIdAndIsDisableOrderByExerciseNameAsc(@PathVariable Long progressTestId){
		List<ExerciseDTO> response = iExerciseService.findByProgressTestIdAndIsDisableOrderByExerciseNameAsc(progressTestId);
		if(response.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
