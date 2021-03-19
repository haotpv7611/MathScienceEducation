package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.LessonDTO;
import com.example.demo.services.ILessonService;

@CrossOrigin
@RestController
public class LessonController {

	@Autowired
	ILessonService iLessonService;
	
	@GetMapping("/unit/{unitId}/lessons")
	public ResponseEntity<List<LessonDTO>> findByUnitId(@PathVariable long unitId){		
		List<LessonDTO> response = iLessonService.findByUnitIdOrderByLessonNameAsc(unitId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/lesson/{id}")
	public ResponseEntity<LessonDTO> findOneById(@PathVariable long id){
		LessonDTO response = iLessonService.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
