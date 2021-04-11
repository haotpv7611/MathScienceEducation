package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ExerciseResponseDTO;
import com.example.demo.dtos.ExerciseRequestDTO;

public interface IExerciseService {

	List<ExerciseResponseDTO> findByLessonIdOrderByExerciseNameAsc(long lessonId);

	List<ExerciseResponseDTO> findByLessonIdStudentView(long lessonId, long accountId);

	List<ExerciseResponseDTO> findByProgressTestIdOrderByExerciseNameAsc(long progressTestId);
	
	List<ExerciseResponseDTO> findByProgressTestIdStudentView(long progressTestId, long accountId);

	String createExercise(ExerciseRequestDTO exerciseRequestDTO);

	String updateExercise(long id, ExerciseRequestDTO exerciseRequestDTO);

	String deleteExercise(long id);

	void deleteOneExercise(long id);
}
