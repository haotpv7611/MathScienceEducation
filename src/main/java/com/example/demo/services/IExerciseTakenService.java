package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ExerciseTakenRequestDTO;
import com.example.demo.dtos.ExerciseTakenResponseDTO;
import com.example.demo.dtos.ScoreResponseDTO;

public interface IExerciseTakenService {

	String findTakenObjectById(long id);

	List<ExerciseTakenResponseDTO> findAllByExerciseId(long exerciseId, long accountId);

	String doExercise(ExerciseTakenRequestDTO exerciseTakenRequestDTO);

	int countExerciseNotDone(long accountId, long exerciseId);
	
	List<ScoreResponseDTO> findAllExerciseScoreBySubjectId(long subjectId, long accountId);
}
