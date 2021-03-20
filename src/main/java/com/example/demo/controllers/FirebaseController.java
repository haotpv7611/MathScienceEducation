package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.services.impls.FirebaseService;

@CrossOrigin
@RestController
public class FirebaseController {

	@Autowired
    private FirebaseService firebaseConfigService;
    
    @PostMapping("/api/v1/test")
    public ResponseEntity<String> create(@RequestParam(name = "file") MultipartFile file) {
    	String fileName = "";
        try {
            fileName = firebaseConfigService.saveFile(file);
            // do whatever you want with that
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	return ResponseEntity.status(HttpStatus.OK).body("Error here");
        }
        return ResponseEntity.status(HttpStatus.OK).body(fileName);
    }
  
}
