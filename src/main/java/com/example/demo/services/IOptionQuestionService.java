package com.example.demo.services;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.OptionQuestionDTO;

public interface IOptionQuestionService {
	List<OptionQuestionDTO> findByQuestionId(long questionId);

	// create option for question in exercise
	void createExerciseOptionQuestion(long questionId, String optionText, boolean isCorrect);

	// create option for question in game fill in blank
	void createGameFillInBlankOptionQuestion(long questionId, String optionText, String optionInputType);

	// create option for question in another games
	void createGameSwappingMatchingChoosingOptionQuestion(long questionId, String optionText, MultipartFile imageFile)
			throws SizeLimitExceededException, IOException;

	// update option for question in exercise
	void updateExerciseOptionQuestion(long id, String optionText, boolean isCorrect);

	// update option for question in game fill in blank
	void updateGameFillInBlankOptionQuestion(long id, String optionText, String optionInputType);

	// update option for question in another games
	void updateGameSwappingMatchingChoosingOptionQuestion(long id, String optionText, MultipartFile imageFile)
			throws SizeLimitExceededException, IOException;
	
	void deleteOptionQuestion(long id);
}
