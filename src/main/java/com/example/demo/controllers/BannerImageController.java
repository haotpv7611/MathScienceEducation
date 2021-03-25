package com.example.demo.controllers;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
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
import org.springframework.web.multipart.MultipartFile;

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
	public ResponseEntity<BannerImageDTO> findBannerImageById(@PathVariable long id) {

		return ResponseEntity.status(HttpStatus.OK).body(iBannerImageService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<String> createBannerImage(@RequestParam String description,
			@RequestParam MultipartFile file, @RequestParam long accountId) throws SizeLimitExceededException, IOException {
		String response = iBannerImageService.createBannerImage(description, file, accountId);
		if (response.contains("permission")) {

			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
		if (!response.contains("SUCCESS")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> updateBannerImage(@PathVariable long id, @RequestParam String description,
			@RequestParam MultipartFile file) throws SizeLimitExceededException, IOException {
		String response = iBannerImageService.updateBannerImage(id, description, file);
		if (!response.contains("SUCCESS")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PutMapping
	public ResponseEntity<String> deleteBannerImage(@RequestParam long id){
		String response = iBannerImageService.deleteBannerImage(id);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
