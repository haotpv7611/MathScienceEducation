package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ExerciseGameQuestionRequestDTO;

public interface IExerciseGameQuestionService {
	List<Long> findAllQuestionIdByExerciseId(long exerciseId);

	List<Long> findAllQuestionIdByGameId(long gameId);

//	List<ExerciseGameQuestionDTO> findAllQuestionByExerciseId1(long exerciseId);
//
//	List<ExerciseGameQuestionDTO> findAllQuestionByGameId1(long gameId);

	String addExerciseOrGameQuestion(ExerciseGameQuestionRequestDTO exerciseGameQuestionRequestDTO);

	String deleteExerciseOrGameQuestion(ExerciseGameQuestionRequestDTO exerciseGameQuestionRequestDTO);
	
	void deleteOneExerciseGameQuestion(long id);
}
