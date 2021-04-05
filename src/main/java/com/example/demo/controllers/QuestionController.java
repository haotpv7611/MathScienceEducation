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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.OptionQuestionDTO;
import com.example.demo.dtos.QuestionDTO;
import com.example.demo.dtos.QuestionRequestDTO;
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
	public ResponseEntity<String> createQuestion(
			@RequestParam(required = false) MultipartFile imageFile,
			@RequestParam(required = false) MultipartFile audioFile, @RequestParam String questionTitle,
			@RequestParam(required = false) String description, @RequestParam float score,
			@RequestParam String questionType, @RequestParam long unitId,
//			@RequestParam List<OptionQuestionDTO> optionQuestionList
			@RequestParam List<String> optionTextList, @RequestParam List<Boolean> isCorrectList
//			@Valid QuestionRequestDTO questionRequestDTO, BindingResult bindingResult
			)
			throws SizeLimitExceededException, IOException {
		System.out.println("Start");
//		String respone = iQuestionService.createQuestion(imageFile, audioFile, questionTitle, description, score,
//				questionType, unitId, optionQuestionList);
		String respone = iQuestionService.createQuestion(imageFile, audioFile, questionTitle, description, score,
				questionType, unitId, optionTextList, isCorrectList);
//		if (bindingResult.hasErrors()) {
//			String error = "";
//			for (ObjectError object : bindingResult.getAllErrors()) {
//				error += "\n" + object.getDefaultMessage();
//			}
//
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
//		}
//		String respone = iQuestionService.createQuestion(questionRequestDTO);
		System.out.println("End");
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
//		if (!response.contains("SUCCESS")) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/question/{unitId}/questions")
	public ResponseEntity<List<QuestionDTO>> getAllListQuestionByUnitId(@PathVariable long unitId) {
		List<QuestionDTO> responses = iQuestionService.findByUnitIdAndIsDisable(unitId, false);
		return ResponseEntity.status(HttpStatus.OK).body(responses);
	}
}
