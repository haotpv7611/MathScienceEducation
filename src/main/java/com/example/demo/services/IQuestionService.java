package com.example.demo.services;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.QuestionResponseDTO;
import com.example.demo.dtos.QuestionExerciseViewDTO;;

public interface IQuestionService {

	Object findQuestionById(long id, String questionType);

	// show list only question in each exercise or game role admin (subject menu)
	List<QuestionResponseDTO> findQuestionByExerciseIdOrGameId(long id, boolean isExercise);

	// show list only question in question bank role admin
	List<QuestionResponseDTO> findAllByUnitId(long unitId, boolean isExercise);

	// show list question and option in role student (do exercise)
	List<QuestionExerciseViewDTO> findQuestionByExerciseId(long exerciseId);

	List<Object> findQuestionByGameId(long gameId);

	String createExerciseQuestion(MultipartFile imageFile, MultipartFile audioFile, String questionTitle,
			String description, float score, String questionType, long unitId, List<String> optionTextList,
			List<Boolean> isCorrectList) throws SizeLimitExceededException, IOException;

	String createGameFillInBlankQuestion(MultipartFile imageFile, String questionTitle, String description, float score,
			String questionType, long unitId, List<String> optionTextList, List<String> optionInputTypeList)
			throws SizeLimitExceededException, IOException;

	String createGameSwappingMatchingChoosingQuestion(String questionTitle, String description, float score,
			String questionType, long unitId, List<MultipartFile> imageFileList, List<String> optionTextList)
			throws SizeLimitExceededException, IOException;

	String updateExerciseQuestion(long id, MultipartFile imageFile, MultipartFile audioFile, String questionTitle,
			String description, float score, List<Long> optionIdList, List<String> optionTextList,
			List<Boolean> isCorrectList, List<Long> optionIdDeleteList) throws SizeLimitExceededException, IOException;

	String updateGameFillInBlankQuestion(long id, MultipartFile imageFile, String questionTitle, String description,
			float score, List<Long> optionIdList, List<String> optionTextList, List<String> optionInputTypeList, List<Long> optionIdDeleteList)
			throws SizeLimitExceededException, IOException;

	String updateGameSwappingMatchingChoosingQuestion(long id, String questionTitle, String description, float score,
			List<Long> optionIdList, List<MultipartFile> imageFileList, List<String> optionTextList)
			throws SizeLimitExceededException, IOException;

	String deleteQuestion(List<Long> ids);

	String deleteOneQuestion(long id);

}
