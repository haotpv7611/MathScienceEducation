package com.example.demo.controllers;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.SubjectResponseDTO;
import com.example.demo.services.ISubjectService;

@CrossOrigin
@RestController
public class SubjectController {

	@Autowired
	ISubjectService iSubjectService;

	@GetMapping("/subject/{id}")
	public ResponseEntity<?> findSubjectById(@PathVariable long id) {
		Object response = iSubjectService.findById(id);
		if (response.equals("NOT FOUND!")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.equals("FIND FAIL!")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/grade/{gradeId}/subjects")
	public ResponseEntity<List<SubjectResponseDTO>> findSubjectByGradeId(@PathVariable int gradeId) {
		List<SubjectResponseDTO> response = iSubjectService.findSubjectByGradeId(gradeId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/subject")
	public ResponseEntity<String> createSubject(@RequestParam String subjectName,
			@RequestParam MultipartFile multipartFile, @RequestParam(required = false) String description,
			@RequestParam int gradeId) throws SizeLimitExceededException, IOException {
		String response = iSubjectService.createSubject(subjectName, multipartFile, description, gradeId);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (response.contains("SUCCESS")) {

			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PutMapping("/subject/{id}")
	public ResponseEntity<String> updateSubject(@PathVariable long id, @RequestParam String subjectName,
			@RequestParam(required = false) MultipartFile multipartFile,
			@RequestParam(required = false) String description, @RequestParam int gradeId)
			throws SizeLimitExceededException, IOException {
		String response = iSubjectService.updateSubject(id, subjectName, multipartFile, description, gradeId);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (response.contains("SUCCESS")) {

			return ResponseEntity.ok(response);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PutMapping("subject/delete/{id}")
	public ResponseEntity<String> deleteSubject(@PathVariable long id) {
		String response = iSubjectService.deleteSubject(id);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}
}
