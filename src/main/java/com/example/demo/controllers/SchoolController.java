package com.example.demo.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.SchoolRequestDTO;
import com.example.demo.dtos.SchoolResponseDTO;
import com.example.demo.services.impls.SchoolServiceImpl;

//@CrossOrigin(origins = { "http://localhost:3000/", "http://major-edu-admin.herokuapp.com/",
//		"http://major-edu-student.herokuapp.com/"})
@CrossOrigin
@RestController
@RequestMapping("/schools")
public class SchoolController {

	@Autowired
	private SchoolServiceImpl schoolServiceImpl;

//	@PostMapping("/school")
	@PostMapping
	public ResponseEntity<?> createSchool(@Valid @RequestBody SchoolRequestDTO schoolRequestDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}

		String response = schoolServiceImpl.createSchool(schoolRequestDTO);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/school/{id}")
	public ResponseEntity<String> updateSchool(@PathVariable long id,
			@Valid @RequestBody SchoolRequestDTO schoolRequestDTO, BindingResult bindingResult) {

		return ResponseEntity.status(HttpStatus.OK).body(schoolServiceImpl.updateSchool(id, schoolRequestDTO));
	}

	@DeleteMapping("/school/{id}")
	public ResponseEntity<String> deleteSchool(@PathVariable long id) {

		return ResponseEntity.status(HttpStatus.OK).body(schoolServiceImpl.deleteSchool(id));
	}

//	@GetMapping("/schools")
	@GetMapping
	public ResponseEntity<List<SchoolResponseDTO>> findAllSchool() {

		List<SchoolResponseDTO> response = schoolServiceImpl.findAllSchool();

		if (response == null) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/school/{id}")
	public ResponseEntity<SchoolResponseDTO> findById(@PathVariable long id) {

		return ResponseEntity.status(HttpStatus.OK).body(schoolServiceImpl.findBySchoolId(id));
	}

}
