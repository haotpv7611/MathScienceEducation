package com.example.demo.services;

import com.example.demo.dtos.ExerciseGameQuestionRequestDTO;

public interface IExerciseGameQuestionService {

	String addExerciseOrGameQuestion(ExerciseGameQuestionRequestDTO exerciseGameQuestionRequestDTO);

	String deleteExerciseOrGameQuestion(ExerciseGameQuestionRequestDTO exerciseGameQuestionRequestDTO);

	void deleteOneExerciseGameQuestion(long id);

}
