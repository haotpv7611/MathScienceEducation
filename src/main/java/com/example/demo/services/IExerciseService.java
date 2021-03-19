package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ExerciseDTO;

public interface IExerciseService {

	List<ExerciseDTO> findByLessonIdAndIsDisableOrderByExerciseNameAsc(long lessonId);
	
	List<ExerciseDTO> findByProgressTestIdAndIsDisableOrderByExerciseNameAsc(long progressTestId);
}
