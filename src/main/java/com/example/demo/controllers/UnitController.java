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

import com.example.demo.dtos.UnitDTO;
import com.example.demo.dtos.UnitViewDTO;
import com.example.demo.services.impls.UnitServiceImpl;

@CrossOrigin
@RestController
public class UnitController {
	
	@Autowired
	private UnitServiceImpl unitServiceImpl;
	
//	@GetMapping("subject/{subjectId}/units")
//	public ResponseEntity<List<UnitDTO>> findBySujectIdOrderByUnitName(@PathVariable Long subjectId){
//		List<UnitDTO> response = unitServiceImpl.findBySubjectIdOrderByUnitNameAsc(subjectId);
//		if (response.isEmpty()) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//		}
//		return ResponseEntity.status(HttpStatus.OK).body(response);
//	}
	
	@GetMapping("subject/{subjectId}/unitView")
	public ResponseEntity<List<UnitViewDTO>> showSubjectViewBySubjectId(@PathVariable Long subjectId){
		List<UnitViewDTO> response = unitServiceImpl.showUnitViewBySubjectId(subjectId);
		if (response.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping("/unit")
	public ResponseEntity<?> createUnit(@RequestBody UnitDTO unitDTO){
		
		UnitDTO response = unitServiceImpl.createUnit(unitDTO);
		
		if(response == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Have properties null!");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(response);		
	}

	@PutMapping("/unit")
	public ResponseEntity<UnitDTO> updateUnit(@RequestBody UnitDTO unitDTO){
		
		return ResponseEntity.status(HttpStatus.OK).body(unitServiceImpl.updateUnit(unitDTO));
		
	}
	
	@DeleteMapping("/unit/{id}")
	public ResponseEntity<String> deleteUnit(@PathVariable long id){
		return ResponseEntity.status(HttpStatus.OK).body(unitServiceImpl.deleteUnit(id));
	}
	
//	@PostMapping
//	public ResponseEntity<Unit> createUnit(@RequestBody Unit unit){
//		return ResponseEntity.ok(unitService.create(unit));
//	}
//

}
