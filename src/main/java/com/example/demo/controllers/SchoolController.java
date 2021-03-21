package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.SchoolDTO;
import com.example.demo.services.impls.SchoolServiceImpl;

@CrossOrigin
@RestController
public class SchoolController {

	@Autowired
	private SchoolServiceImpl schoolServiceImpl;

	@PostMapping("/school")
	public ResponseEntity<?> createSchool(@RequestBody SchoolDTO schoolDTO) {

		SchoolDTO response = schoolServiceImpl.createSchool(schoolDTO);

		if (response == null) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Have properties null!");
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/school")
	public ResponseEntity<SchoolDTO> updateSchool(@RequestBody SchoolDTO schoolDTO) {

		return ResponseEntity.status(HttpStatus.OK).body(schoolServiceImpl.updateSchool(schoolDTO));
	}

	@DeleteMapping("/school/{id}")
	public ResponseEntity<String> deleteSchool(@PathVariable long id) {

		return ResponseEntity.status(HttpStatus.OK).body(schoolServiceImpl.deleteSchool(id));
	}

	@GetMapping("/schools")
	public ResponseEntity<List<SchoolDTO>> findAllSchool() {

		List<SchoolDTO> response = schoolServiceImpl.findAllSchool();

		if (response == null) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/school/{id}")
	public ResponseEntity<SchoolDTO> findById(@PathVariable long id) {
		
		return ResponseEntity.status(HttpStatus.OK).body(schoolServiceImpl.findBySchoolId(id));
	}

}
