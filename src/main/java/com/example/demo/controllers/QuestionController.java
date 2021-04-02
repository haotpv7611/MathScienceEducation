package com.example.demo.controllers;

import java.io.IOException;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.services.IQuestionService;

@CrossOrigin
@RestController
public class QuestionController {

	@Autowired
	private IQuestionService iQuestionService;

	@PostMapping("/question")
	public ResponseEntity<String> createQuestion(@RequestParam String questionTitle, @RequestParam String questionText,
			@RequestParam MultipartFile multipartImage, @RequestParam MultipartFile multipartAudio,
			@RequestParam float score, @RequestParam long questionTypeId, @RequestParam long unitId)
			throws SizeLimitExceededException, IOException {
		String respone = iQuestionService.createQuestion(questionTitle, questionText, multipartImage, multipartAudio,
				score, questionTypeId, unitId);
		if (!respone.contains("SUCCESS")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respone);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(respone);
	}

	@PutMapping("/question/{id}")
	public ResponseEntity<String> updateQuestion(@PathVariable long id,
			@RequestParam(required = false) String questionTitle, @RequestParam(required = false) String questionText,
			@RequestParam(required = false) MultipartFile multipartImage,
			@RequestParam(required = false) MultipartFile multipartAudio, @RequestParam(required = false) float score,
			@RequestParam(required = false) long unitId) throws SizeLimitExceededException, IOException {

		String response = iQuestionService.updateQuestion(id, questionTitle, questionText, multipartImage,
				multipartAudio, score, unitId);
		if (!response.contains("SUCCESS")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PutMapping("/question/delete/{id}")
	public ResponseEntity<String> deleteQuestion(@PathVariable long id) {
		String response = iQuestionService.deleteQuestion(id);
		if (!response.contains("SUCCESS")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
