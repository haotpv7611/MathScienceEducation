package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.ISchoolGradeService;

@CrossOrigin
@RestController
@RequestMapping("/schoolGrade")
public class SchoolGradeController {

	@Autowired
	ISchoolGradeService iSchoolGradeService;

	@PostMapping
	public ResponseEntity<String> linkGradeAndSchool(@RequestParam long gradeId, @RequestParam long schoolId) {

		return ResponseEntity.ok(iSchoolGradeService.linkGradeAndSchool(gradeId, schoolId));
	}
	
//	@PutMapping
//	public ResponseEntity<String> removeLinkGradeAndSchool(@RequestParam long gradeId, @RequestParam long schoolId) {
//
//		return ResponseEntity.ok(iSchoolGradeService.removeLinkGradeAndSchool(gradeId, schoolId));
//	}
}
