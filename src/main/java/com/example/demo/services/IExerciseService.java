package com.example.demo.services;

import java.util.List;
import java.util.Map;

import com.example.demo.dtos.ExerciseResponseDTO;
import com.example.demo.dtos.IdAndStatusDTO;
import com.example.demo.dtos.ExerciseRequestDTO;

public interface IExerciseService {

	List<ExerciseResponseDTO> findByLessonIdOrderByExerciseNameAsc(long lessonId);

	List<ExerciseResponseDTO> findByLessonIdStudentView(long lessonId, long accountId);

	List<ExerciseResponseDTO> findByProgressTestIdOrderByExerciseNameAsc(long progressTestId);

	List<ExerciseResponseDTO> findByProgressTestIdStudentView(long progressTestId, long accountId);

	Map<Long, Integer> findAllExercise();

	String createExercise(ExerciseRequestDTO exerciseRequestDTO);

	String updateExercise(long id, ExerciseRequestDTO exerciseRequestDTO);

	String changeExerciseStatus(IdAndStatusDTO idAndStatusDTO);

	void changeStatusOne(IdAndStatusDTO idAndStatusDTO);
}
