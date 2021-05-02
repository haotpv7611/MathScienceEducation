package com.example.demo.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.NewsRequestDTO;
import com.example.demo.dtos.NewsResponseDTO;
import com.example.demo.services.INewsService;

@CrossOrigin
@RestController
@RequestMapping("/news")
public class NewsController {

	@Autowired
	private INewsService inewsService;

	@PostMapping
	@PreAuthorize("hasRole('admin')")
	public ResponseEntity<String> createNews(@Valid @RequestBody NewsRequestDTO newsRequestDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}

		String response = inewsService.createNews(newsRequestDTO);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// both
	@GetMapping("/all")
	@PreAuthorize("hasRole('admin') or hasRole('student')")
	public ResponseEntity<List<NewsResponseDTO>> findAllOrderByCreateDateDesc(@RequestParam boolean isStudent) {
		List<NewsResponseDTO> response = inewsService.findAllNewsOrderByCreatedDateDesc(isStudent);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	// both
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('admin') or hasRole('student')")
	public ResponseEntity<?> findNewsById(@PathVariable long id) {
		Object response = inewsService.findNewsById(id);
		if (response.equals("NOT FOUND!")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.equals("FIND FAIL!")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PutMapping
	@PreAuthorize("hasRole('admin')")
	public ResponseEntity<String> deleteNews(@RequestBody List<Long> ids) {
		if (ids == null) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID ID");
		} else {
			if (ids.isEmpty()) {

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID ID");
			}
		}
		String response = inewsService.deleteNews(ids);
		if (response.equals("NOT FOUND!")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.equals("FIND FAIL!")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	// student role
	@GetMapping("/3newest")
	@PreAuthorize("hasRole('student')")
	public ResponseEntity<List<NewsResponseDTO>> findThreeOrderByCreateDateDesc() {
		List<NewsResponseDTO> response = inewsService.findThreeNewsOrderByCreatedDateDesc();
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

}
