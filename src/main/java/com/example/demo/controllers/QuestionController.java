package com.example.demo.controllers;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.QuestionResponseDTO;
import com.example.demo.dtos.QuestionExerciseViewDTO;
import com.example.demo.services.IQuestionService;

@CrossOrigin
@RestController
public class QuestionController {

	@Autowired
	private IQuestionService iQuestionService;

	@GetMapping("/question/{id}")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<?> findQuestionById(@PathVariable long id, @RequestParam String questionType) {
		Object response = iQuestionService.findQuestionById(id, questionType);
		if (response.equals("NOT FOUND!")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.equals("FIND FAIL!")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);

	}

	
	@GetMapping("/game/{gameId}/questions")
	// @PreAuthorize("hasRole('student')")
	public ResponseEntity<List<Object>> getListQuestionByGameId(@PathVariable long gameId){
		List<Object> response = iQuestionService.findQuestionByGameId(gameId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
		return ResponseEntity.ok(response);
	}

	@GetMapping("exerciseOrGame/{id}/questions")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<List<QuestionResponseDTO>> findQuestionByExerciseIdRoleAdmin(@PathVariable long id,
			@RequestParam boolean isExericse) {
		List<QuestionResponseDTO> response = iQuestionService.findQuestionByExerciseIdOrGameId(id, isExericse);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/unit/{unitId}/questions")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<List<QuestionResponseDTO>> findAllByUnitId(@PathVariable long unitId,
			@RequestParam boolean isExercise) {
		List<QuestionResponseDTO> response = iQuestionService.findAllByUnitId(unitId, isExercise);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/exersise/{exerciseId}/questions")
	// @PreAuthorize("hasRole('student')")
	public ResponseEntity<List<QuestionExerciseViewDTO>> findQuestionByExerciseId(@PathVariable long exerciseId) {
		List<QuestionExerciseViewDTO> response = iQuestionService.findQuestionByExerciseId(exerciseId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/question/exercise")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<String> createExerciseQuestion(@RequestParam(required = false) MultipartFile imageFile,
			@RequestParam(required = false) MultipartFile audioFile, @RequestParam String questionTitle,
			@RequestParam(required = false) String description, @RequestParam float score,
			@RequestParam String questionType, @RequestParam long unitId, @RequestParam List<String> optionTextList,
			@RequestParam List<Boolean> isCorrectList) throws SizeLimitExceededException, IOException {
		String response = iQuestionService.createExerciseQuestion(imageFile, audioFile, questionTitle, description,
				score, questionType, unitId, optionTextList, isCorrectList);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (response.contains("SUCCESS")) {

			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PostMapping("/question/game/fillInBlank")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<String> createGameFillInBlankQuestion(@RequestParam(required = false) MultipartFile imageFile,
			@RequestParam String questionTitle, @RequestParam(required = false) String description,
			@RequestParam float score, @RequestParam String questionType, @RequestParam long unitId,
			@RequestParam List<String> optionTextList, @RequestParam List<String> optionInputTypeList)
			throws SizeLimitExceededException, IOException {
		String response = iQuestionService.createGameFillInBlankQuestion(imageFile, questionTitle, description, score,
				questionType, unitId, optionTextList, optionInputTypeList);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (response.contains("SUCCESS")) {

			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PostMapping("/question/game/others")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<String> createGameSwappingMatchingChoosingQuestion(@RequestParam String questionTitle,
			@RequestParam(required = false) String description, @RequestParam float score,
			@RequestParam String questionType, @RequestParam long unitId,
			@RequestParam List<MultipartFile> imageFileList, @RequestParam List<String> optionTextList)
			throws SizeLimitExceededException, IOException {
		String response = iQuestionService.createGameSwappingMatchingChoosingQuestion(questionTitle, description, score,
				questionType, unitId, imageFileList, optionTextList);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (response.contains("SUCCESS")) {

			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PutMapping("/question/{id}/exercise")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<String> updateExerciseQuestion(@PathVariable long id,
			@RequestParam(required = false) MultipartFile imageFile,
			@RequestParam(required = false) MultipartFile audioFile, @RequestParam String questionTitle,
			@RequestParam(required = false) String description, @RequestParam float score,
			@RequestParam List<Long> optionIdList, @RequestParam List<String> optionTextList,
			@RequestParam List<Boolean> isCorrectList, @RequestParam List<Long> optionIdDeleteList) throws SizeLimitExceededException, IOException {

		String response = iQuestionService.updateExerciseQuestion(id, imageFile, audioFile, questionTitle, description,
				score, optionIdList, optionTextList, isCorrectList, optionIdDeleteList);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (response.contains("SUCCESS")) {

			return ResponseEntity.ok(response);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PutMapping("/question/{id}/game/fillInBlank")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<String> updateGameFillInBlankQuestion(@PathVariable long id,
			@RequestParam(required = false) MultipartFile imageFile, @RequestParam String questionTitle,
			@RequestParam(required = false) String description, @RequestParam float score,
			@RequestParam List<Long> optionIdList, @RequestParam List<String> optionTextList,
			@RequestParam List<String> optionInputTypeList,@RequestParam List<Long> optionIdDeleteList) throws SizeLimitExceededException, IOException {

		String response = iQuestionService.updateGameFillInBlankQuestion(id, imageFile, questionTitle, description,
				score, optionIdList, optionTextList, optionInputTypeList, optionIdDeleteList);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (response.contains("SUCCESS")) {

			return ResponseEntity.ok(response);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PutMapping("/question/{id}/game/others")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<String> updateGameSwappingMatchingChoosingQuestion(@PathVariable long id,
			@RequestParam String questionTitle, @RequestParam(required = false) String description,
			@RequestParam float score, @RequestParam List<Long> optionIdList, @RequestParam List<String> optionTextList,
			@RequestParam(required = false) List<MultipartFile> imageFileList)
			throws SizeLimitExceededException, IOException {

		for (int i = 0; i < imageFileList.size(); i++) {
			if (imageFileList.get(i).getOriginalFilename().equalsIgnoreCase("fakeFile")) {
				imageFileList.set(i, null);
			}
		}

		String response = iQuestionService.updateGameSwappingMatchingChoosingQuestion(id, questionTitle, description,
				score, optionIdList, imageFileList, optionTextList);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (response.contains("SUCCESS")) {

			return ResponseEntity.ok(response);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PutMapping("/question/delete")
	// @PreAuthorize("hasRole('admin') or hasRole('staff')")
	public ResponseEntity<String> deleteQuestion(@RequestParam List<Long> ids) {
		String response = iQuestionService.deleteQuestion(ids);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}		
		if (response.contains("CANNOT")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.ok(response);
	}

}
