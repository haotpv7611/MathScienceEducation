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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.QuestionDTO;
import com.example.demo.dtos.QuestionViewDTO;
import com.example.demo.services.IQuestionService;

@CrossOrigin
@RestController
public class QuestionController {

	@Autowired
	private IQuestionService iQuestionService;

//	@GetMapping("/exersise/{exerciseId}/questions")
//	public ResponseEntity<List<QuestionViewDTO>> getListQuestionByExerciseId(@PathVariable long exerciseId){
//		List<QuestionViewDTO> response = iQuestionService.showQuestionByExerciseId(exerciseId);
//		return ResponseEntity.status(HttpStatus.OK).body(response);
//	}
//	
//	@GetMapping("/game/{gameId}/questions")
//	public ResponseEntity<List<QuestionViewDTO>> getListQuestionByGameId(@PathVariable long gameId){
//		List<QuestionViewDTO> response = iQuestionService.showQuestionByGameId(gameId);
//		return ResponseEntity.status(HttpStatus.OK).body(response);
//	}
	
	@PostMapping("/question")
	public ResponseEntity<String> createQuestion(@RequestParam String questionTitle, @RequestParam String description,
			@RequestParam MultipartFile multipartImage, @RequestParam MultipartFile multipartAudio,
			@RequestParam float score, @RequestParam long questionTypeId, @RequestParam long unitId)
			throws SizeLimitExceededException, IOException {
		String respone = iQuestionService.createQuestion(questionTitle, description, multipartImage, multipartAudio,
				score, questionTypeId, unitId);
		if (!respone.contains("SUCCESS")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respone);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(respone);
	}

	@PutMapping("/question/{id}")
	public ResponseEntity<String> updateQuestion(@PathVariable long id,
			@RequestParam(required = false) String questionTitle, @RequestParam(required = false) String description,
			@RequestParam(required = false) MultipartFile multipartImage,
			@RequestParam(required = false) MultipartFile multipartAudio, @RequestParam(required = false) float score,
			@RequestParam(required = false) long unitId) throws SizeLimitExceededException, IOException {

		String response = iQuestionService.updateQuestion(id, questionTitle, description, multipartImage,
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
	
	@GetMapping("/question/{unitId}/questions")
	public ResponseEntity<List<QuestionDTO>> getAllListQuestionByUnitId(@PathVariable long unitId){
		List<QuestionDTO> responses = iQuestionService.findByUnitIdAndIsDisable(unitId, false);
		return ResponseEntity.status(HttpStatus.OK).body(responses);
	}
}
