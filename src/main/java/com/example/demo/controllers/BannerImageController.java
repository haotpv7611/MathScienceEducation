package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.services.IBannerImageService;
import com.example.demo.services.impls.BannerImageServiceImpl;

@Controller
public class BannerImageController {
	
	@Autowired
	IBannerImageService iBannerImageService;
	
	@GetMapping("/bannerImages")
	public ResponseEntity<List<String>> findAll(){
		List<String> response = iBannerImageService.findAll();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
