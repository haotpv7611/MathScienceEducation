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

import com.example.demo.dtos.BannerImageDTO;
import com.example.demo.services.IBannerImageService;

@CrossOrigin
@RestController
@RequestMapping("/bannerImages")
public class BannerImageController {
	
	@Autowired
	IBannerImageService iBannerImageService;
	
	@GetMapping("/url")
	public ResponseEntity<List<String>> showBannerImage(){
		List<String> response = iBannerImageService.showBannerImage();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping
	public ResponseEntity<List<BannerImageDTO>> findAll(){
		List<BannerImageDTO> response = iBannerImageService.findAll();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BannerImageDTO> findNewsById(@PathVariable long id) {

		return ResponseEntity.status(HttpStatus.OK).body(iBannerImageService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<String> createNews(@RequestParam String description,
			@RequestParam String imageUrl, @RequestParam long accountId) {
		String response = iBannerImageService.createBannerImage(description, imageUrl, accountId);
		if (response.contains("permission")) {

			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
		if (!response.contains("SUCCESS")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PutMapping
	public ResponseEntity<String> updateNews(@RequestParam long id, @RequestParam String description,
			@RequestParam String imageUrl) {
		String response = iBannerImageService.updateBannerImage(id, description, imageUrl);
		if (!response.contains("SUCCESS")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
