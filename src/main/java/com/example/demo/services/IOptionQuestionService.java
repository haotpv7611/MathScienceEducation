package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.OptionQuestionDTO;

public interface IOptionQuestionService {
	List<OptionQuestionDTO> findByQuestionId(long questionId);

	void createOptionQuestion(long questionId, String optionText, boolean isCorrect);
}
