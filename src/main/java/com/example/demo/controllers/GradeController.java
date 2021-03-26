package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.GradeDTO;
import com.example.demo.services.IGradeService;

@CrossOrigin
@RestController
public class GradeController {

	@Autowired
	IGradeService iGradeService;
	
	@GetMapping("/grades")
	public ResponseEntity<List<GradeDTO>> getAllGrade(){		
		List<GradeDTO> response = iGradeService.findByIsDisable(false);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
}
