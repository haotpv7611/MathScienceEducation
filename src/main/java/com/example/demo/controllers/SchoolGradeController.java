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

import com.example.demo.dtos.GradeDTO;
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
	public ResponseEntity<List<SchoolResponseDTO>> findSchoolByGradeId(@PathVariable int gradeId) {
		List<SchoolResponseDTO> response = iSchoolGradeService.findSchoolLinkedByGradeId(gradeId);
		
		return ResponseEntity.ok(response);
	}

	@PostMapping("/schoolGrade")
	public ResponseEntity<String> linkGradeAndSchool(@RequestBody SchoolGradeDTO schoolGradeDTO) {
		String response = iSchoolGradeService.linkGradeAndSchool(schoolGradeDTO);
		if (response.equals("EXISTED!")) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/schoolGrade")
	public ResponseEntity<String> removeLinkGradeAndSchool(@RequestBody ListIdAndStatusDTO listIdAndStatusDTO) {
		String response = iSchoolGradeService.changeStatusGradeAndSchool(listIdAndStatusDTO);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/grade/{schoolId}")
	public ResponseEntity<List<GradeDTO>> findGradeBySchoolId(@PathVariable long schoolId) {
		List<GradeDTO> response = iSchoolGradeService.findGradeLinkedBySchoolId(schoolId);
		
		return ResponseEntity.ok(response);
	}
}
