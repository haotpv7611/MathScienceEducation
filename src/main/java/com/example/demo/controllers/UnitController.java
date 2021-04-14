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
	@GetMapping("/unit/{id}")
	public ResponseEntity<?> findUnitById(@PathVariable long id) {
		Object response = iUnitService.findById(id);
		if (response.equals("NOT FOUND!")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.equals("FIND FAIL!")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	// done
	@GetMapping("/subject/{subjectId}/units")
	public ResponseEntity<List<UnitResponseDTO>> findBySubjectId(@PathVariable long subjectId) {
		List<UnitResponseDTO> response = iUnitService.findBySubjectId(subjectId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/subject/{subjectId}/unitAterIds")
	public ResponseEntity<List<UnitResponseDTO>> findAllUnitAfterIdsBySubjectId(@PathVariable long subjectId) {
		List<UnitResponseDTO> response = iUnitService.findAllUnitAfterIdsBySubjectId(subjectId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/subject/{subjectId}/unitView")
	public ResponseEntity<List<UnitViewDTO>> showUnitViewBySubjectId(@PathVariable long subjectId, @RequestParam long accountId) {
		List<UnitViewDTO> response = iUnitService.showUnitViewBySubjectId(subjectId, accountId);
		if (response.isEmpty()) {
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
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

	@PutMapping("unit/delete")
	public ResponseEntity<String> deleteUnit(@RequestParam long id) {
		String response = iUnitService.deleteUnit(id);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);

	}
}
