package com.example.demo.controllers;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
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
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.BannerImageDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.services.IBannerImageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin
@RestController
@RequestMapping("/bannerImage")
public class BannerImageController {
	Logger logger = LoggerFactory.getLogger(BannerImageController.class);

	@Autowired
	IBannerImageService iBannerImageService;

	@PostMapping
	public ResponseEntity<String> createBannerImage(@RequestParam String description, @RequestParam MultipartFile file,
			@RequestParam long accountId) throws SizeLimitExceededException, IOException {
		String response = iBannerImageService.createBannerImage(description, file, accountId);
		if (response.contains("permission")) {

			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
		if (!response.contains("SUCCESS")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/all")
	public ResponseEntity<List<BannerImageDTO>> findAllBannerImage() {
		List<BannerImageDTO> response = iBannerImageService.findAll();

		return ResponseEntity.ok(response);
	}

	@PutMapping
	public ResponseEntity<String> changeStatusBannerImage(@Valid @RequestBody ListIdAndStatusDTO listIdAndStatusDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		String status = listIdAndStatusDTO.getStatus();
		if (!status.equals("ACTIVE") && !status.equals("INACTIVE") && !status.equals("DELETED")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("STATUS INVALID!");
		}
		String response = iBannerImageService.changeStatusBannerImage(listIdAndStatusDTO);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BannerImageDTO> findBannerImageById(@PathVariable long id) {
		BannerImageDTO response = iBannerImageService.findById(id);
		
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateBannerImage(@PathVariable long id,
			@RequestParam(required = false) String description, @RequestParam(required = false) MultipartFile file)
			throws SizeLimitExceededException, IOException {
		String response = iBannerImageService.updateBannerImage(id, description, file);
		if (!response.contains("SUCCESS")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		
		return ResponseEntity.ok(response);
	}

	//student role
	@GetMapping("/url")
	public ResponseEntity<List<String>> showBannerImage() {
		List<String> response = iBannerImageService.showBannerImage();

		return ResponseEntity.ok(response);
	}
}
