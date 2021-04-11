package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.ScoreResponseDTO;
import com.example.demo.services.IScoreService;

@RestController
public class ScoreController {
	@Autowired
	private IScoreService iScoreService;

	@GetMapping("subject/{subjectId}/score")
	public ResponseEntity<List<ScoreResponseDTO>> findAllScoreBySubjectId(@PathVariable long subjectId,
			@RequestParam long accountId) {
		List<ScoreResponseDTO> response = iScoreService.findAllExerciseScoreBySubjectId(subjectId, accountId);

		return ResponseEntity.ok(response);

	}

}
