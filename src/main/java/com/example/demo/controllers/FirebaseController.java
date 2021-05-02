package com.example.demo.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.services.IFirebaseService;

@CrossOrigin
@RestController
public class FirebaseController {

	@Autowired
	private IFirebaseService iFirebaseService;

	@PostMapping("/file")
	// @PreAuthorize("hasRole('admin')")
	public ResponseEntity<String> uploadImage(@RequestParam MultipartFile multipartFile) throws IOException {
		String error = "";

		// 1. validate file is empty and file type only image and audio
		if (multipartFile == null) {
			error = "File is invalid!";
		} else {
			if (multipartFile.isEmpty()) {
				error = "File is invalid!";
			} else {
				if (!multipartFile.getContentType().contains("image")
						&& !multipartFile.getContentType().contains("audio")) {
					error = "Not supported this file type!";
				}
			}
		}
		if (!error.isEmpty()) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}

		String response = iFirebaseService.uploadFile(multipartFile);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/file")
	// @PreAuthorize("hasRole('admin')")
	public ResponseEntity<String> deleteImage(@RequestParam String fileUrl) {
		try {
			iFirebaseService.deleteFile(fileUrl);
		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID URL");
		}

		return ResponseEntity.ok("SUCCESS!");
	}
}
