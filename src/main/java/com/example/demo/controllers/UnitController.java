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

import com.example.demo.dtos.UnitResponseDTO;
import com.example.demo.dtos.UnitRequestDTO;
import com.example.demo.dtos.UnitViewDTO;
import com.example.demo.services.IUnitService;

@CrossOrigin
@RestController
public class UnitController {

	@Autowired
	private IUnitService iUnitService;

	// done
	@GetMapping("/subject/{subjectId}/units")
	public ResponseEntity<List<UnitResponseDTO>> findBySubjectIdOrderByUnitNameAsc(@PathVariable long subjectId) {
		List<UnitResponseDTO> response = iUnitService.findBySubjectIdOrderByUnitNameAsc(subjectId);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/subject/{subjectId}/unitView")
	public ResponseEntity<List<UnitViewDTO>> showUnitViewBySubjectId(@PathVariable long subjectId) {
		List<UnitViewDTO> response = iUnitService.showUnitViewBySubjectId(subjectId);
		if (response.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	// done
	@GetMapping("/unit/{id}")
	public ResponseEntity<UnitResponseDTO> findUnitById(@PathVariable long id) {
		UnitResponseDTO response = iUnitService.findById(id);

		return ResponseEntity.ok(response);
	}

	// done
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
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// done
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
		String response = iUnitService.updateUnit(id, unitRequestDTO);
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PutMapping("unit/delete")
	public ResponseEntity<String> deleteUnit(@RequestParam long id) {
		String response = iUnitService.deleteUnit(id);
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);

	}
}
