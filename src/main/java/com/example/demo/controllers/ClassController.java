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

import com.example.demo.dtos.ClassRequestDTO;
import com.example.demo.dtos.ClassResponseDTO;
import com.example.demo.dtos.GradeClassDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.SchoolGradeDTO;
import com.example.demo.services.IClassService;

@CrossOrigin
@RestController
@RequestMapping("/class")
public class ClassController {

	@Autowired
	private IClassService iClassService;

	@PostMapping("/schoolGradeId")
	public ResponseEntity<List<ClassResponseDTO>> findBySchoolGradeId(@RequestBody SchoolGradeDTO schoolGradeDTO) {

		return ResponseEntity.ok(iClassService.findBySchoolGradeId(schoolGradeDTO));
	}
	
	@GetMapping("/{schoolId}")
	public ResponseEntity<List<GradeClassDTO>> findGradeClassBySchoolId(@PathVariable long schoolId) {

		return ResponseEntity.ok(iClassService.findGradeClassBySchoolId(schoolId));
	}

	@PostMapping
	public ResponseEntity<String> createClass(@Valid @RequestBody ClassRequestDTO classRequestDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}

		return ResponseEntity.ok(iClassService.createClass(classRequestDTO));
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<String> updateClass(@PathVariable long id, @Valid @RequestBody ClassRequestDTO classRequestDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}

		return ResponseEntity.ok(iClassService.updateClass(id, classRequestDTO));
	}
	
	@PutMapping("/changeStatus")
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
		String response = iClassService.changeStatusClass(idAndStatusDTOList);

		return ResponseEntity.ok(response);
	}
}
