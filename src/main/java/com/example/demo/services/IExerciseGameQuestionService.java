package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ExerciseGameQuestionDTO;

public interface IExerciseGameQuestionService {
	List<Long> findAllQuestionIdByExerciseId(long exerciseId);

	List<Long> findAllQuestionIdByGameId(long gameId);

//	List<ExerciseGameQuestionDTO> findAllQuestionByExerciseId1(long exerciseId);
//
//	List<ExerciseGameQuestionDTO> findAllQuestionByGameId1(long gameId);

	String addExerciseOrGameQuestion(ExerciseGameQuestionDTO exerciseGameQuestionDTO);

//	String deleteExerciseOrGameQuestion(List<Long> ids);
	
	void deleteExerciseGameQuestion(long id);
}
