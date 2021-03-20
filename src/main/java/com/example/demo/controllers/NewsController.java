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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.NewsDTO;
import com.example.demo.services.INewsService;

@CrossOrigin
@RestController
@RequestMapping("/news")
public class NewsController {

	@Autowired
	private INewsService inewsService;

	@GetMapping
	public ResponseEntity<List<NewsDTO>> findAllOrderByCreateDateDesc(@RequestParam boolean isStudent) {
		List<NewsDTO> response = inewsService.findAllNewsOrderByCreatedDateDesc(isStudent);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/3newest")
	public ResponseEntity<List<NewsDTO>> findThreeOrderByCreateDateDesc() {
		List<NewsDTO> response = inewsService.findThreeNewsOrderByCreatedDateDesc();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<NewsDTO> findNewsById(@PathVariable long id) {

		return ResponseEntity.status(HttpStatus.OK).body(inewsService.findNewsById(id));
	}

	@PostMapping
	public ResponseEntity<String> createNews(@RequestParam String newsTitle, @RequestParam String shortDescription,
			@RequestParam String newsContent, @RequestParam long accountId) {
		String response = inewsService.createNews(newsTitle, shortDescription, newsContent, accountId);
		if (response.contains("permission")) {
			
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
		if (!response.contains("SUCCESS")) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping
	public ResponseEntity<String> updateNews(@RequestParam long id, @RequestParam String newsTitle,
			@RequestParam String shortDescription, @RequestParam String newsContent) {
		String response = inewsService.updateNews(id, newsTitle, shortDescription, newsContent);
		if (!response.contains("SUCCESS")) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> deleteNews(@PathVariable long id) {
		inewsService.deleteNews(id);
		
		return ResponseEntity.status(HttpStatus.OK).body("DELETE SUCCESS!");
	}

}
