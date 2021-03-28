package com.example.demo.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.SchoolRequestDTO;
import com.example.demo.dtos.SchoolResponseDTO;
import com.example.demo.services.ISchoolService;

@CrossOrigin
@RestController
//@RequestMapping("/school")
public class SchoolController {

	@Autowired
	private ISchoolService iSchoolService;

	@GetMapping("/grade/{gradeId}/school")
	public ResponseEntity<List<SchoolResponseDTO>> findSchoolByGradeId(@PathVariable long gradeId) {

		return ResponseEntity.ok(iSchoolService.findByGradeId(gradeId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<SchoolResponseDTO> findSchoolById(@PathVariable long id) {

		return ResponseEntity.ok(iSchoolService.findSchoolById(id));
	}

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

		String response = iSchoolService.createSchool(schoolRequestDTO);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateSchool(@PathVariable long id,
			@Valid @RequestBody SchoolRequestDTO schoolRequestDTO, BindingResult bindingResult) {

		return ResponseEntity.status(HttpStatus.OK).body(iSchoolService.updateSchool(id, schoolRequestDTO));
	}

//	@DeleteMapping("/{id}")
//	public ResponseEntity<String> deleteSchool(@PathVariable long id) {
//
//		return ResponseEntity.status(HttpStatus.OK).body(schoolServiceImpl.deleteSchool(id));
//	}

	@GetMapping("/all")
	public ResponseEntity<List<SchoolResponseDTO>> findAllSchool() {

		List<SchoolResponseDTO> response = iSchoolService.findAllSchool();

		if (response == null) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
