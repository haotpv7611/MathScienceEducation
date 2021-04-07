package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ExerciseDTO;

public interface IExerciseService {

	List<ExerciseDTO> findByLessonIdOrderByExerciseNameAsc(long lessonId);

	List<ExerciseDTO> findByProgressTestIdOrderByExerciseNameAsc(long progressTestId);

	String createExercise(ExerciseDTO exerciseDTO);

	String updateExercise(long id, ExerciseDTO exerciseDTO);

//	String deleteExercise(List<Long> ids);
	
	void deleteOneExercise(long id);
}
