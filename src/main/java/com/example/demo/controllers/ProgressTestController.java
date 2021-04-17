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

import com.example.demo.dtos.ProgressTestResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.dtos.ProgressTestRequestDTO;
import com.example.demo.services.IProgressTestService;

@CrossOrigin
@RestController
public class ProgressTestController {

	@Autowired
	private IProgressTestService iProgressTestService;

	@GetMapping("/progressTest/{id}")
	public ResponseEntity<?> findProgressTestById(@PathVariable long id) {
		Object response = iProgressTestService.findById(id);
		if (response.equals("NOT FOUND!")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.equals("FIND FAIL!")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/subject/{subjectId}/progressTest")
	public ResponseEntity<List<ProgressTestResponseDTO>> findBySubjectId(@PathVariable long subjectId) {
		List<ProgressTestResponseDTO> response = iProgressTestService.findBySubjectId(subjectId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/progressTest")
	public ResponseEntity<String> createProgressTest(@Valid @RequestBody ProgressTestRequestDTO progressTestRequestDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "/n" + object.getDefaultMessage();
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}

		String response = iProgressTestService.createProgressTest(progressTestRequestDTO);
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

	@PutMapping("progressTest/{id}")
	public ResponseEntity<String> updateProgressTest(@PathVariable long id,
			@Valid @RequestBody ProgressTestRequestDTO progressTestRequestDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "/n" + object.getDefaultMessage();
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}

		String response = iProgressTestService.updateProgressTest(id, progressTestRequestDTO);
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

	@PutMapping("progressTest/delete")
	public ResponseEntity<String> deleteProgressTest(@RequestParam long id) {
		try {
			iProgressTestService.deleteOneProgressTest(id);

			return ResponseEntity.ok("DELETE SUCCESS!");
		} catch (Exception e) {
			if (e instanceof ResourceNotFoundException) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND!");
			}

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DELETE FAIL!");
		}

//		if (response.contains("NOT FOUND")) {
//
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//		}
//		if (response.contains("FAIL")) {
//
//			return ;
//		}

	}
}
