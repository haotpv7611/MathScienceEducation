package com.example.demo.services;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.OptionQuestionDTO;

public interface IOptionQuestionService {
	List<OptionQuestionDTO> findByQuestionId(long questionId);

	void createExerciseOptionQuestion(long questionId, String optionText, boolean isCorrect);

	void createGameFillInBlankOptionQuestion(long questionId, String optionText, String optionInputType);

	void createGameSwappingMatchingChoosingOptionQuestion(long questionId, String optionText, MultipartFile imageFile)
			throws SizeLimitExceededException, IOException;
	
	void deleteOptionQuestion(long id);
}
