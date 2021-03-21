package com.example.demo.services.impls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.IExerciseGameQuestionRepository;
import com.example.demo.services.IExerciseGameQuestionService;

@Service
public class ExerciseGameQuestionServiceImpl implements IExerciseGameQuestionService {

	@Autowired
	private IExerciseGameQuestionRepository iExerciseGameQuestionRepository;

	@Override
	public List<Long> findAllQuestionByExerciseId(long exerciseId) {
		
		return iExerciseGameQuestionRepository.findAllQuestionIdByExerciseId(exerciseId);
	}

	@Override
	public List<Long> findAllQuestionByGameId(long gameId) {
		
		return iExerciseGameQuestionRepository.findAllQuestionIdByExerciseId(gameId);
	}

}
