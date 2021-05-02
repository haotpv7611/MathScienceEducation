package com.example.demo.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.GameRequestDTO;
import com.example.demo.dtos.GameResponseDTO;
import com.example.demo.dtos.IdAndStatusDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.services.IGameService;

@CrossOrigin
@RestController
public class GameController {
	@Autowired
	private IGameService iGameService;

	@GetMapping("/game/{id}")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<Object> findOneById(@PathVariable long id) {
		Object response = iGameService.findGameById(id);
		if (response.equals("NOT FOUND!")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.equals("FIND FAIL!")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/lesson/{lessonId}/game/all")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<List<GameResponseDTO>> findAllByLessonId(@PathVariable long lessonId) {
		List<GameResponseDTO> response = iGameService.findAllByLessonId(lessonId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/lesson/{lessonId}/game/student")
	// @PreAuthorize("hasRole('student')")
	public ResponseEntity<List<GameResponseDTO>> findAllByLessonIdStudentView(@PathVariable long lessonId) {
		List<GameResponseDTO> response = iGameService.findAllByLessonIdStudentView(lessonId);

		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/game")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<String> createGame(@Valid @RequestBody GameRequestDTO gameRequestDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}

		String response = iGameService.createGame(gameRequestDTO);
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

	@PutMapping("/game/{id}")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<String> updateGame(@PathVariable long id, @Valid @RequestBody GameRequestDTO gameRequestDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}

		String response = iGameService.updateGame(id, gameRequestDTO);
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

	@PutMapping("/game")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	@Transactional
	public ResponseEntity<String> changeGameStatus(@RequestBody IdAndStatusDTO idAndStatusDTO) {
		try {
			String response = iGameService.changeOneGameStatus(idAndStatusDTO);
			if (response.contains("CANNOT")) {

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			return ResponseEntity.ok("DELETE SUCESS!");
		} catch (Exception e) {
			if (e instanceof ResourceNotFoundException) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND");
			}

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DELETE FAIL!");
		}
	}
}
