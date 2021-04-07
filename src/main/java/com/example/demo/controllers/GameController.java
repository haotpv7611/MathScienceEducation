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
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.GameRequestDTO;
import com.example.demo.dtos.GameResponseDTO;
import com.example.demo.services.IGameService;

@CrossOrigin
@RestController
public class GameController {
	@Autowired
	IGameService iGameService;

	@GetMapping("/lesson/{lessonId}/game/all")
	public ResponseEntity<List<GameResponseDTO>> findAllByLessonId(@PathVariable long lessonId) {
		List<GameResponseDTO> response = iGameService.findAllByLessonId(lessonId);
		
		return ResponseEntity.ok(response);
	}

	@GetMapping("/game/{id}")
	public ResponseEntity<GameResponseDTO> findOneById(@PathVariable long id) {
		GameResponseDTO response = iGameService.findGameById(id);
		
		return ResponseEntity.ok(response);
	}

	@PutMapping("/game/{id}")
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

		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/game")
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

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	

	@PutMapping("/game")
	public ResponseEntity<String> deleteGame(@RequestBody List<Long> ids) {		
		String response = iGameService.deleteGame(ids);

		return ResponseEntity.ok(response);
	}
}
