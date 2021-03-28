package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.SchoolLevel;
import com.example.demo.services.ISchoolLevelService;

@CrossOrigin
@RestController
@RequestMapping("/schoolLevel")
public class SchoolLevelController {
	@Autowired
	ISchoolLevelService iSchoolLevelService;
	
	@GetMapping
	public ResponseEntity<List<SchoolLevel>> findAll(){
		return ResponseEntity.ok(iSchoolLevelService.findAll());
	}
}
