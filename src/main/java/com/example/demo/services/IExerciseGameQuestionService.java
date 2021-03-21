package com.example.demo.services;

import java.util.List;

public interface IExerciseGameQuestionService {
	List<Long> findAllQuestionByExerciseId(long exerciseId);
	List<Long> findAllQuestionByGameId(long gameId);
}
