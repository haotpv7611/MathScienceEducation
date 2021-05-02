package com.example.demo.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.IClassService;
import com.example.demo.services.IExerciseService;
import com.example.demo.services.IGameService;
import com.example.demo.services.ILessonService;
import com.example.demo.services.IProgressTestService;
import com.example.demo.services.ISubjectService;
import com.example.demo.services.IUnitService;

@CrossOrigin
@RestController
@RequestMapping("/breadcrumb")
public class BreadcrumbController {
	@Autowired
	private IClassService iClassService;

	@Autowired
	private ISubjectService iSubjectService;
	
	@Autowired
	private IProgressTestService iProgressTestService;

	@Autowired
	private IUnitService iUnitService;

	@Autowired
	private ILessonService iLessonService;

	@Autowired
	private IExerciseService iExerciseService;

	@Autowired
	private IGameService iGameService;

	@GetMapping("/classes")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<Map<Long, String>> findAllClassesBreadcrumb() {
		Map<Long, String> response = iClassService.findAllClass();

		return ResponseEntity.ok(response);
	}

	@GetMapping("/subject")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<Map<Long, String>> findAllSubjectBreadcrumb() {
		Map<Long, String> response = iSubjectService.findAllSubject();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/progressTest")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<Map<Long, String>> findAllProgressTestBreadcrumb() {
		Map<Long, String> response = iProgressTestService.findAllProgressTest();

		return ResponseEntity.ok(response);
	}

	@GetMapping("/unit")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<Map<Long, Integer>> findAllUnitBreadcrumb() {
		Map<Long, Integer> response = iUnitService.findAllUnit();

		return ResponseEntity.ok(response);
	}

	@GetMapping("/lesson")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<Map<Long, Integer>> findAllLessonBreadcrumb() {
		Map<Long, Integer> response = iLessonService.findAllLesson();

		return ResponseEntity.ok(response);
	}

	@GetMapping("/exercise")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<Map<Long, Integer>> findAllExerciseBreadcrumb() {
		Map<Long, Integer> response = iExerciseService.findAllExercise();

		return ResponseEntity.ok(response);
	}

	@GetMapping("/game")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<Map<Long, Integer>> findAllGameBreadcrumb() {
		Map<Long, Integer> response = iGameService.findAllGame();

		return ResponseEntity.ok(response);
	}
}
