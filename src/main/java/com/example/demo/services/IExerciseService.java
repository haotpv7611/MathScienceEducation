package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ExerciseDTO;
import com.example.demo.dtos.ExerciseRequestDTO;

public interface IExerciseService {

	List<ExerciseDTO> findByLessonIdOrderByExerciseNameAsc(long lessonId);

	List<ExerciseDTO> findByProgressTestIdOrderByExerciseNameAsc(long progressTestId);

	String createExercise(ExerciseRequestDTO exerciseRequestDTO);

	String updateExercise(long id, ExerciseRequestDTO exerciseRequestDTO);

	String deleteExercise(long id);
	
	void deleteOneExercise(long id);
}
