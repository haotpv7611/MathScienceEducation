package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.ClassResponseDTO;
import com.example.demo.services.IClassService;

@CrossOrigin
@RestController
@RequestMapping("/class")
public class ClassController {

	@Autowired
	private IClassService iClassService;

	@GetMapping
	public ResponseEntity<List<ClassResponseDTO>> findBySchoolGradeId(@RequestParam long gradeId,
			@RequestParam long schoolId) {
		return ResponseEntity.ok(iClassService.findBySchoolGradeId(gradeId, schoolId));
	}
}
