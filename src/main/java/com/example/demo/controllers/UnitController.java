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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.UnitDTO;
import com.example.demo.dtos.UnitRequestDTO;
import com.example.demo.dtos.UnitViewDTO;
import com.example.demo.services.IUnitService;

@CrossOrigin
@RestController
public class UnitController {

	@Autowired
	private IUnitService iUnitService;

	@GetMapping("subject/{subjectId}/units")
	public ResponseEntity<List<UnitDTO>> findBySubjectIdOrderByUnitNameAsc(@PathVariable long subjectId) {
		List<UnitDTO> response = iUnitService.findBySubjectIdOrderByUnitNameAsc(subjectId);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("subject/{subjectId}/unitView")
	public ResponseEntity<List<UnitViewDTO>> showUnitViewBySubjectId(@PathVariable long subjectId) {
		List<UnitViewDTO> response = iUnitService.showUnitViewBySubjectId(subjectId);
		if (response.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("/unit")
	public ResponseEntity<String> createUnit(@Valid @RequestBody UnitRequestDTO unitRequestDTO,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "/n" + object.getDefaultMessage();
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		String response = iUnitService.createUnit(unitRequestDTO);

//		if (!response.contains("SUCCESS")) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//		}
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/unit/{id}")
	public ResponseEntity<String> updateUnit(@PathVariable long id, @Valid @RequestBody UnitRequestDTO unitRequestDTO,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "/n" + object.getDefaultMessage();
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}

		return ResponseEntity.ok(iUnitService.updateUnit(id, unitRequestDTO));
	}

	@PutMapping("unit/delete")
	public ResponseEntity<String> deleteUnit(@RequestParam long id) {
		String response = iUnitService.deleteUnit(id);
		if (!response.contains("SUCCESS")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

}
