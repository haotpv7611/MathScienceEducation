package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.SchoolGradeDTO;
import com.example.demo.dtos.SchoolResponseDTO;
import com.example.demo.services.ISchoolGradeService;

@CrossOrigin
@RestController
public class SchoolGradeController {

	@Autowired
	ISchoolGradeService iSchoolGradeService;
	
	@GetMapping("/grade/{gradeId}/school")
	public ResponseEntity<List<SchoolResponseDTO>> findSchoolByGradeId(@PathVariable long gradeId) {

		return ResponseEntity.ok(iSchoolGradeService.findSchoolLinkedByGradeId(gradeId));
	}

	@PostMapping("/schoolGrade")
	public ResponseEntity<String> linkGradeAndSchool(@RequestBody SchoolGradeDTO schoolGradeDTO) {

		return ResponseEntity.ok(iSchoolGradeService.linkGradeAndSchool(schoolGradeDTO));
	}
	
	@PutMapping("/schoolGrade")
	public ResponseEntity<String> removeLinkGradeAndSchool(@RequestBody ListIdAndStatusDTO listIdAndStatusDTO) {

		return ResponseEntity.ok(iSchoolGradeService.changeStatusGradeAndSchool(listIdAndStatusDTO));
	}
	
	@GetMapping("/grade/{schoolId}")
	public ResponseEntity<List<SchoolResponseDTO>> findGradeBySchoolId(@PathVariable long schoolId) {

		return ResponseEntity.ok(iSchoolGradeService.findSchoolLinkedByGradeId(schoolId));
	}
}
