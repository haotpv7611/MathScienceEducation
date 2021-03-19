package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.SubjectDTO;
import com.example.demo.services.ISubjectService;

@CrossOrigin
@RestController
public class SubjectController {

	@Autowired
	ISubjectService iSubjectService;
	

	@GetMapping("/grade/{gradeId}/subjects")
	public ResponseEntity<List<SubjectDTO>> findSubjectByGradeId(@PathVariable long gradeId){
		
		List<SubjectDTO> response = iSubjectService.findSubjectByGradeId(gradeId);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
