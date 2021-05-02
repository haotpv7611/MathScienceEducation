package com.example.demo.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.ExerciseTakenRequestDTO;
import com.example.demo.dtos.ExerciseTakenResponseDTO;
import com.example.demo.dtos.ScoreResponseDTO;
import com.example.demo.services.IExerciseTakenService;

@CrossOrigin
@RestController
public class ExerciseTakenController {
	@Autowired
	private IExerciseTakenService iExerciseTakenService;

	@GetMapping("/exerciseTaken/{id}")
	@PreAuthorize("hasRole('admin') or hasRole('staff') or hasRole('student')")
	public ResponseEntity<Map<String, String>> findTakenObjectById(@PathVariable long id) {
		String response = iExerciseTakenService.findTakenObjectById(id);
		HashMap<String, String> map = new HashMap<>();
		map.put("takenObject", response);

		return ResponseEntity.ok(map);
	}

	@PostMapping("/exerciseTaken/all")
	@PreAuthorize("hasRole('student')")
	public ResponseEntity<List<ExerciseTakenResponseDTO>> findAllByExerciseId(@RequestParam long exerciseId,
			@RequestParam long accountId) {
		List<ExerciseTakenResponseDTO> response = iExerciseTakenService.findAllByExerciseId(exerciseId, accountId);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/exericseTaken")
	@PreAuthorize("hasRole('student')")
	public ResponseEntity<String> doExericse(@RequestBody ExerciseTakenRequestDTO exerciseTakenRequestDTO) {
		String response = iExerciseTakenService.doExercise(exerciseTakenRequestDTO);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PostMapping("subject/{subjectId}/score")
	@PreAuthorize("hasRole('student')")
	public ResponseEntity<List<ScoreResponseDTO>> findAllScoreBySubjectId(@PathVariable long subjectId,
			@RequestParam long accountId) {
		List<ScoreResponseDTO> response = iExerciseTakenService.findAllExerciseScoreBySubjectId(subjectId, accountId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
		return ResponseEntity.ok(response);
	}
}
