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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.IdAndStatusDTO;
import com.example.demo.dtos.SchoolRequestDTO;
import com.example.demo.dtos.SchoolResponseDTO;
import com.example.demo.services.ISchoolService;

@CrossOrigin
@RestController
@RequestMapping("/school")
public class SchoolController {

	@Autowired
	private ISchoolService iSchoolService;

	@GetMapping("/{id}")
	public ResponseEntity<SchoolResponseDTO> findSchoolById(@PathVariable long id) {

		return ResponseEntity.ok(iSchoolService.findSchoolById(id));
	}

	@PostMapping("/check")
	public ResponseEntity<String> checkSchoolIsExisted(@Valid @RequestBody SchoolRequestDTO schoolRequestDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		String response = iSchoolService.checkSchoolExisted(schoolRequestDTO);

		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<String> createSchool(@Valid @RequestBody SchoolRequestDTO schoolRequestDTO,
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
		String response = iSchoolService.updateSchool(id, schoolRequestDTO);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/all")
	public ResponseEntity<List<SchoolResponseDTO>> findAllSchools() {
		List<SchoolResponseDTO> response = iSchoolService.findAllSchool();

		return ResponseEntity.ok(response);

	}

	@GetMapping("/all/active")
	public ResponseEntity<List<SchoolResponseDTO>> findSchoolByStatusActive() {
		List<SchoolResponseDTO> response = iSchoolService.findSchoolByStatusActive();

		return ResponseEntity.ok(response);
	}

	@PutMapping("/changeStatus")
	public ResponseEntity<String> changeStatusSchool(@Valid @RequestBody IdAndStatusDTO idAndStatusDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		String status = idAndStatusDTO.getStatus();
		if (!status.equals("ACTIVE") && !status.equals("INACTIVE") && !status.equals("DELETED")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("STATUS INVALID!");
		}
		String response = iSchoolService.changeStatusSchool(idAndStatusDTO);

		return ResponseEntity.ok(response);
	}

}
