package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.GradeResponseDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.SchoolGradeDTO;
import com.example.demo.dtos.SchoolResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.services.ISchoolGradeService;

@CrossOrigin
@RestController
public class SchoolGradeController {

	@Autowired
	ISchoolGradeService iSchoolGradeService;

	@GetMapping("/grade/{gradeId}/school")
	public ResponseEntity<List<SchoolResponseDTO>> findSchoolByGradeId(@PathVariable int gradeId) {
		List<SchoolResponseDTO> response = iSchoolGradeService.findSchoolLinkedByGradeId(gradeId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/schoolGrade")
	public ResponseEntity<String> linkGradeAndSchool(@RequestBody SchoolGradeDTO schoolGradeDTO) {
		String response = iSchoolGradeService.linkGradeAndSchool(schoolGradeDTO);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (response.contains("EXISTED")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		if (response.contains("INACTIVE")) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/schoolGrade")
	public ResponseEntity<String> changeStatusGradeAndSchool(@RequestBody ListIdAndStatusDTO listIdAndStatusDTO) {
		try {
			iSchoolGradeService.changeStatusGradeAndSchool(listIdAndStatusDTO);

			return ResponseEntity.ok("CHANGE SUCCESS!");
		} catch (Exception e) {
			if (e instanceof ResourceNotFoundException) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND");
			}

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CHANGE FAIL!");
		}
	}

	@GetMapping("/grade/{schoolId}")
	public ResponseEntity<List<GradeResponseDTO>> findGradeBySchoolId(@PathVariable long schoolId) {
		List<GradeResponseDTO> response = iSchoolGradeService.findGradeLinkedBySchoolId(schoolId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}
}
