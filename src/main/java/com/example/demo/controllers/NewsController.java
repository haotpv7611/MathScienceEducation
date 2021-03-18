package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.NewsDTO;
import com.example.demo.services.INewsService;

@CrossOrigin
@RestController
public class NewsController {
	
	@Autowired
	private INewsService inewsService;
	
	@GetMapping("/news")
	public ResponseEntity<List<NewsDTO>> findAllOrderByCreateDateDesc(){		
		List<NewsDTO> response = inewsService.findAllNewsOrderByCreatedDateDesc();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/news/3newest")
	public ResponseEntity<List<NewsDTO>> findThreeOrderByCreateDateDesc(){
		List<NewsDTO> response = inewsService.findThreeNewsOrderByCreatedDateDesc();

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/news/{id}")
	public ResponseEntity<NewsDTO> findNewsById(@PathVariable long id){
		
		return ResponseEntity.status(HttpStatus.OK).body(inewsService.findNewsById(id));
	}
}
