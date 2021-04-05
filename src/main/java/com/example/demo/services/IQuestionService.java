package com.example.demo.services;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.OptionQuestionDTO;
import com.example.demo.dtos.QuestionDTO;
import com.example.demo.dtos.QuestionRequestDTO;
import com.example.demo.dtos.QuestionViewDTO;;

public interface IQuestionService {

	List<QuestionViewDTO> showQuestionByExerciseId(long exerciseId);

	List<QuestionViewDTO> showQuestionByGameId(long gameId);

//	String createQuestion(String questionTitle, String questionText, MultipartFile multipartImage,
//			MultipartFile multipartAudio, float score, long questionTypeId, long unitId)
//			throws SizeLimitExceededException, IOException;

//	String createQuestion(MultipartFile imageFile, MultipartFile audioFile, String questionTitle, String description,
//			float score, String questionType, long unitId, List<OptionQuestionDTO> optionQuestionList)
//			throws SizeLimitExceededException, IOException;
	String createQuestion(MultipartFile imageFile, MultipartFile audioFile, String questionTitle, String description,
			float score, String questionType, long unitId, List<String> optionTextList, List<Boolean> isCorrectList)
			throws SizeLimitExceededException, IOException;
//	String createQuestion(QuestionRequestDTO questionRequestDTO)
//			throws SizeLimitExceededException, IOException;

	String updateQuestion(long id, String questionTitle, String questionText, MultipartFile multipartImage,
			MultipartFile multipartAudio, float score, long unitId) throws SizeLimitExceededException, IOException;

	String deleteQuestion(long id);

	List<QuestionDTO> findByUnitIdAndIsDisable(long unitId, boolean isDisable);

}
