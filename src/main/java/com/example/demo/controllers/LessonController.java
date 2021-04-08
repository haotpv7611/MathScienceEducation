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

import com.example.demo.dtos.LessonResponseDTO;
import com.example.demo.dtos.LessonRequestDTO;
import com.example.demo.services.ILessonService;

@CrossOrigin
@RestController
public class LessonController {

	@Autowired
	ILessonService iLessonService;

	@GetMapping("/unit/{unitId}/lessons")
	public ResponseEntity<List<LessonResponseDTO>> findLessonByUnitId(@PathVariable long unitId) {
		List<LessonResponseDTO> response = iLessonService.findByUnitIdOrderByLessonNameAsc(unitId);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/lesson/{id}")
	public ResponseEntity<LessonResponseDTO> findLessonById(@PathVariable long id) {
		LessonResponseDTO response = iLessonService.findById(id);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/lesson")
	public ResponseEntity<String> createLesson(@Valid @RequestBody LessonRequestDTO lessonRequestDTO,
			BindingResult bingdingResult) {
		if (bingdingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bingdingResult.getAllErrors()) {
				error += "/n" + object.getDefaultMessage();
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		String response = iLessonService.createLesson(lessonRequestDTO);
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/lesson/{id}")
	public ResponseEntity<String> updateLesson(@PathVariable long id,
			@Valid @RequestBody LessonRequestDTO lessonRequestDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "/n" + object.getDefaultMessage();
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		String response = iLessonService.updateLesson(id, lessonRequestDTO);
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);

	}

	@PutMapping("/lesson")
	public ResponseEntity<String> deleteLesson(@RequestParam long id) {
		String response = iLessonService.deleteLesson(id);
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);

	}
}
