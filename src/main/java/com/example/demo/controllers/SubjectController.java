package com.example.demo.controllers;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.example.demo.utils.Util;

@CrossOrigin
@RestController
public class SubjectController {
	private final int SUBJECT_NAME_MAX_LENGTH = 20;
	private final int DESCRIPTION_MAX_LENGTH = 50;

	@Autowired
	private ISubjectService iSubjectService;

	@GetMapping("/subject/{id}")
	@PreAuthorize("hasRole('admin') or hasRole('staff')")
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
	@PreAuthorize("hasRole('student')")
	public ResponseEntity<List<SubjectResponseDTO>> findSubjectsByGradeId(@PathVariable int gradeId) {
		List<SubjectResponseDTO> response = iSubjectService.findSubjectsByGradeId(gradeId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/subject")
	@PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<String> createSubject(@RequestParam String subjectName,
			@RequestParam MultipartFile multipartFile, @RequestParam(required = false) String description,
			@RequestParam int gradeId) throws SizeLimitExceededException, IOException {
		// validate data input
		String error = Util.validateRequiredString(subjectName, SUBJECT_NAME_MAX_LENGTH, "\nSubjectName is invalid!");
		error += Util.validateRequiredFile(multipartFile, "image", "\nFile is invalid!",
				"\nNot supported this file type for image!");
		error += Util.validateString(description, DESCRIPTION_MAX_LENGTH, "\nDescription is invalid!");
		if (!error.isEmpty()) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}

		String response = iSubjectService.createSubject(subjectName, multipartFile, description, gradeId);
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

	@PutMapping("/subject/{id}")
	@PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<String> updateSubject(@PathVariable long id, @RequestParam String subjectName,
			@RequestParam(required = false) MultipartFile multipartFile,
			@RequestParam(required = false) String description, @RequestParam int gradeId)
			throws SizeLimitExceededException, IOException {
		// validate data input
		String error = Util.validateRequiredString(subjectName, SUBJECT_NAME_MAX_LENGTH, "\nSubjectName is invalid!");
		error += Util.validateFile(multipartFile, "image", "\nNot supported this file type for image!");
		error += Util.validateString(description, DESCRIPTION_MAX_LENGTH, "\nDescription is invalid!");
		if (!error.isEmpty()) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		
		String response = iSubjectService.updateSubject(id, subjectName, multipartFile, description, gradeId);
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

	@PutMapping("subject/delete/{id}")
	@PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<String> deleteSubject(@PathVariable long id) {
		String response = iSubjectService.deleteSubject(id);
		if (response.contains("CANNOT")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}
}
