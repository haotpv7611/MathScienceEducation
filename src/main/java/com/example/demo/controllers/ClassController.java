package com.example.demo.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.ClassRequestDTO;
import com.example.demo.dtos.ClassResponseDTO;
import com.example.demo.dtos.GradeClassDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.SchoolGradeDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.services.IClassService;

@CrossOrigin
@RestController
@RequestMapping("/class")
public class ClassController {

	@Autowired
	private IClassService iClassService;

	@PostMapping("/schoolGradeId")
	// @PreAuthorize("hasRole('admin')")
	public ResponseEntity<List<ClassResponseDTO>> findBySchoolGradeId(@RequestBody SchoolGradeDTO schoolGradeDTO) {
		List<ClassResponseDTO> response = iClassService.findBySchoolGradeId(schoolGradeDTO);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{schoolId}")
	// @PreAuthorize("hasRole('admin')")
	public ResponseEntity<List<GradeClassDTO>> findGradeClassBySchoolId(@RequestParam long schoolId,
			@RequestParam int gradeId, @RequestParam long classesId) {
		List<GradeClassDTO> response = iClassService.findGradeClassBySchoolId(schoolId, gradeId, classesId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping
	// @PreAuthorize("hasRole('admin')")
	public ResponseEntity<String> createClass(@Valid @RequestBody ClassRequestDTO classRequestDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}

		String response = iClassService.createClass(classRequestDTO);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (response.contains("EXISTED")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{id}")
	// @PreAuthorize("hasRole('admin')")
	public ResponseEntity<String> updateClass(@PathVariable long id,
			@Valid @RequestBody ClassRequestDTO classRequestDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		String response = iClassService.updateClass(id, classRequestDTO);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (response.contains("EXISTED")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PutMapping("/changeStatus")
	// @PreAuthorize("hasRole('admin')")
	public ResponseEntity<String> changeStatusClass(@Valid @RequestBody ListIdAndStatusDTO idAndStatusDTOList,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		String status = idAndStatusDTOList.getStatus();
		if (!status.equals("ACTIVE") && !status.equals("INACTIVE") && !status.equals("DELETED")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("STATUS INVALID!");
		}
		try {
			String response = iClassService.changeStatusClass(idAndStatusDTOList);
			if (response.contains("CANNOT")) {

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			if (e instanceof ResourceNotFoundException) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND");
			}

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CHANGE FAIL!");
		}
	}
}
