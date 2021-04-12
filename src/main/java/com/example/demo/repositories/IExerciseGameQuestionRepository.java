package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.ExerciseGameQuestion;

@Repository
public interface IExerciseGameQuestionRepository extends JpaRepository<ExerciseGameQuestion, Long> {

	List<ExerciseGameQuestion> findByExerciseIdAndIsDisableFalse(long exerciseId);

	List<ExerciseGameQuestion> findByGameIdAndIsDisableFalse(long exerciseId);
	
	List<ExerciseGameQuestion> findByQuestionIdAndIsDisableFalse(long questionId);

	ExerciseGameQuestion findByIdAndIsDisableFalse(long id);

	ExerciseGameQuestion findByQuestionIdAndExerciseIdAndIsDisableFalse(long questionId, long exerciseId);

	ExerciseGameQuestion findByQuestionIdAndGameIdAndIsDisableFalse(long questionId, long exerciseId);
}
