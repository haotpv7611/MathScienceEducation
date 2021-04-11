package com.example.demo.services;

import com.example.demo.dtos.ExerciseTakenRequestDTO;

public interface IExerciseTakenService {
	String doExercise(ExerciseTakenRequestDTO exerciseTakenRequestDTO);
	
	 int countExerciseNotDone(long accountId, long exerciseId);
}
