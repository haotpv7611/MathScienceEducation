package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.GradeResponseDTO;
import com.example.demo.services.IGradeService;

@CrossOrigin
@RestController
@RequestMapping("/grade")
public class GradeController {
	
	@Autowired
	private IGradeService iGradeService;

	@GetMapping("/all")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<List<GradeResponseDTO>> findAllGrades() {
		List<GradeResponseDTO> response = iGradeService.findAllGrades();
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
		return ResponseEntity.ok(response);
	}

}
