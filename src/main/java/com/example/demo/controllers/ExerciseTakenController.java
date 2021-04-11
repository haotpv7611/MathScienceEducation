package com.example.demo.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.ExerciseTakenRequestDTO;
import com.example.demo.dtos.ExerciseTakenResponseDTO;
import com.example.demo.services.IExerciseTakenService;

@CrossOrigin
@RestController
public class ExerciseTakenController {
	@Autowired
	IExerciseTakenService iExerciseTakenService;

	@GetMapping("/exerciseTaken/{id}")
	public ResponseEntity<Map<String, String>> findTakenObjectById(@PathVariable long id) {
		String response = iExerciseTakenService.findTakenObjectById(id);
		HashMap<String, String> map = new HashMap<>();
		map.put("takenObject", response);

		return ResponseEntity.ok(map);
	}

	@PostMapping("/exerciseTaken/all")
	public ResponseEntity<List<ExerciseTakenResponseDTO>> findAllByExerciseId(@RequestParam long exerciseId,
			@RequestParam long accountId) {
		List<ExerciseTakenResponseDTO> response = iExerciseTakenService.findAllByExerciseId(exerciseId, accountId);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/exericseTaken")
	public ResponseEntity<String> doExericse(@RequestBody ExerciseTakenRequestDTO exerciseTakenRequestDTO) {
		String response = iExerciseTakenService.doExercise(exerciseTakenRequestDTO);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
