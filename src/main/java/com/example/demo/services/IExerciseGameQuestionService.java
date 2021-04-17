package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ExerciseGameQuestionRequestDTO;
import com.example.demo.models.ExerciseGameQuestion;

public interface IExerciseGameQuestionService {

	String addExerciseOrGameQuestion(ExerciseGameQuestionRequestDTO exerciseGameQuestionRequestDTO);

	String deleteExerciseOrGameQuestion(ExerciseGameQuestionRequestDTO exerciseGameQuestionRequestDTO);

	void deleteOneExerciseGameQuestion(long id);
	
//	List<ExerciseGameQuestion> findAllByGameId(long gameId);
}
