package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.UnitViewDTO;
import com.example.demo.services.IUnitService;

@CrossOrigin
@RestController
public class UnitController {
	
	@Autowired
	private IUnitService iUnitService;
	

	
	@GetMapping("subject/{subjectId}/unitView")
	public ResponseEntity<List<UnitViewDTO>> showSubjectViewBySubjectId(@PathVariable long subjectId){
		List<UnitViewDTO> response = iUnitService.showUnitViewBySubjectId(subjectId);
		if (response.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping("/unit")
	public ResponseEntity<String> createUnit(@RequestParam long subjectId, @RequestParam int unitName, @RequestParam String description){
		
		String response = iUnitService.createUnit(subjectId, unitName, description);
		
		if(!response.contains("SUCCESS")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(response);		
	}

	@PutMapping("/unit")
	public ResponseEntity<String> updateUnit(@RequestParam long id, @RequestParam int unitName, @RequestParam String description){
		
		String response = iUnitService.updateUnit(id, unitName, description);
		if(!response.contains("SUCCESS")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(response);		
		
	}
	
//	@PutMapping("/unit/{id}")
//	public ResponseEntity<String> deleteUnit(@PathVariable long id){
//		iUnitService.deleteUnit(id);
//		return ResponseEntity.status(HttpStatus.OK).body("DELETE SUCCESS!");
//	}
	


}
