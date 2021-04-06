package com.example.demo.services;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.QuestionResponseDTO;
import com.example.demo.dtos.QuestionViewDTO;;

public interface IQuestionService {

	List<QuestionViewDTO> showQuestionByExerciseId(long exerciseId);

	List<QuestionViewDTO> showQuestionByGameId(long gameId);

	String createExerciseQuestion(MultipartFile imageFile, MultipartFile audioFile, String questionTitle,
			String description, float score, String questionType, long unitId, List<String> optionTextList,
			List<Boolean> isCorrectList) throws SizeLimitExceededException, IOException;

	String createGameFillInBlankQuestion(MultipartFile imageFile, MultipartFile audioFile, String questionTitle,
			String description, float score, String questionType, long unitId, List<String> optionTextList,
			List<String> optionInputTypeList) throws SizeLimitExceededException, IOException;

	String createGameSwappingMatchingChoosingQuestion(String questionTitle, String description, float score,
			String questionType, long unitId, List<MultipartFile> imageFileList, List<String> optionTextList)
			throws SizeLimitExceededException, IOException;

	String updateQuestion(long id, String questionTitle, String questionText, MultipartFile multipartImage,
			MultipartFile multipartAudio, float score, long unitId) throws SizeLimitExceededException, IOException;

	String deleteQuestion(long id);

	List<QuestionResponseDTO> findAllByUnitId(long unitId, boolean isExercise);

}
