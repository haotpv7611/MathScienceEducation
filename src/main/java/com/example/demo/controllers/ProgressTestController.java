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

import com.example.demo.dtos.ProgressTestDTO;
import com.example.demo.services.IProgressTestService;

@CrossOrigin
@RestController
public class ProgressTestController {

	@Autowired
	private IProgressTestService iProgressTestService;

	@GetMapping("subject/{subjectId}/progressTest")
	public ResponseEntity<List<ProgressTestDTO>> findBySubjectId(@PathVariable Long subjectId) {
		List<ProgressTestDTO> response = iProgressTestService.findBySubjectId(subjectId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("progressTest/{id}")
	public ResponseEntity<ProgressTestDTO> findById(@PathVariable long id) {
		ProgressTestDTO response = iProgressTestService.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("progressTest")
	public ResponseEntity<String> createProgressTest(@Valid @RequestBody ProgressTestDTO progressTestDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "/n" + object.getDefaultMessage();
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		String response = iProgressTestService.createProgressTest(progressTestDTO);
		if (!response.contains("SUCCESS")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("progressTest/{id}")
	public ResponseEntity<String> updateProgressTest(@PathVariable long id,
			@Valid @RequestBody ProgressTestDTO progressTestDTO, BindingResult bindingResult) {
		System.out.println("start");
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "/n" + object.getDefaultMessage();
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		String response = iProgressTestService.updateProgressTest(progressTestDTO);
		if (!response.contains("SUCCESS")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PutMapping("progressTest/delete")
	public ResponseEntity<String> deleteProgressTest(@RequestParam long id) {
		String response = iProgressTestService.deleteProgressTest(id);
		if (!response.contains("SUCCESS")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
