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

	// done
	@GetMapping("/grade/{gradeId}/subjects")
	public ResponseEntity<List<SubjectResponseDTO>> findSubjectByGradeId(@PathVariable long gradeId) {
		List<SubjectResponseDTO> response = iSubjectService.findSubjectByGradeId(gradeId);

		return ResponseEntity.ok(response);
	}

	// done
	@GetMapping("/subject/{id}")
	public ResponseEntity<SubjectResponseDTO> findSubjectById(@PathVariable long id) {
		SubjectResponseDTO response = iSubjectService.findById(id);

		return ResponseEntity.ok(response);
	}

	// done
	@PostMapping("/subject")
	public ResponseEntity<String> createSubject(@RequestParam String subjectName,
			@RequestParam MultipartFile multipartFile, @RequestParam(required = false) String description,
			@RequestParam long gradeId) throws SizeLimitExceededException, IOException {
		String response = iSubjectService.createSubject(subjectName, multipartFile, description, gradeId);
		if (!response.contains("SUCCESS")) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// done
	@PutMapping("/subject/{id}")
	public ResponseEntity<String> updateSubject(@PathVariable long id,
			@RequestParam String subjectName,
			@RequestParam(required = false) MultipartFile multipartFile,
			@RequestParam(required = false) String description, @RequestParam long gradeId)
			throws SizeLimitExceededException, IOException {
		String response = iSubjectService.updateSubject(id, subjectName, multipartFile, description, gradeId);
		if (!response.contains("SUCCESS")) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		
		return ResponseEntity.ok(response);
	}

	@PutMapping("subject/delete/{id}")
	public ResponseEntity<String> deleteSubject(@PathVariable long id) {
		String response = iSubjectService.deleteSubject(id);

		return ResponseEntity.ok(response);
	}
}
