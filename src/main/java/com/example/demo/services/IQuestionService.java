package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.QuestionViewDTO;

public interface IQuestionService {

//	List<QuestionDTO> findByExerciseId(Long exerciseId);
//	Question findOneById(Long id);

	List<QuestionViewDTO> showQuestionByExerciseId(long exerciseId);

	List<QuestionViewDTO> showQuestionByGameId(long gameId);
}
