package com.example.demo.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.services.impls.FirebaseService;

@CrossOrigin
@RestController
public class FirebaseController {

	@Autowired
	private FirebaseService firebaseService;

	@PostMapping("/file")
	public ResponseEntity<String> create(@RequestParam MultipartFile multipartFile) throws IOException {

		String response = firebaseService.saveFile(multipartFile);

		if (response.contains("Not supported")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		if (response.contains("Invalid")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/file")
	public ResponseEntity<String> delete(@RequestParam String fileUrl) {
		String response = firebaseService.deleteFile(fileUrl);
		
		if (response.contains("invalid")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
