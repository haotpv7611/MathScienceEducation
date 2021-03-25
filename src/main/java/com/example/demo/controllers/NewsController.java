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

	@GetMapping
	public ResponseEntity<List<NewsResponseDTO>> findAllOrderByCreateDateDesc(@RequestParam boolean isStudent) {
		List<NewsResponseDTO> response = inewsService.findAllNewsOrderByCreatedDateDesc(isStudent);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/3newest")
	public ResponseEntity<List<NewsResponseDTO>> findThreeOrderByCreateDateDesc() {
		List<NewsResponseDTO> response = inewsService.findThreeNewsOrderByCreatedDateDesc();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<NewsResponseDTO> findNewsById(@PathVariable long id) {

		return ResponseEntity.status(HttpStatus.OK).body(inewsService.findNewsById(id));
	}

	@PostMapping
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
		if (response.contains("permission")) {

			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

//	@PutMapping("/{id}")
//	public ResponseEntity<String> updateNews(@PathVariable long id, @Valid @RequestBody NewsRequestDTO newsRequestDTO,
//			BindingResult bindingResult) {
//		if (bindingResult.hasErrors()) {
//			String error = "";
//			for (ObjectError object : bindingResult.getAllErrors()) {
//				error += "\n" + object.getDefaultMessage();
//			}
//
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
//		}
//
//		String response = inewsService.updateNews(newsRequestDTO);
//		if (!response.contains("SUCCESS")) {
//
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//		}
//
//		return ResponseEntity.status(HttpStatus.OK).body(response);
//	}

	@PutMapping
	public ResponseEntity<String> deleteNews(@RequestParam long id) {
		String response = inewsService.deleteNews(id);
		if (!response.contains("SUCCESS")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
