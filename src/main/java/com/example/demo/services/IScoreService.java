package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ScoreResponseDTO;

public interface IScoreService {
	List<ScoreResponseDTO> findAllExerciseScoreBySubjectId(long subjectId, long accountId);
}
