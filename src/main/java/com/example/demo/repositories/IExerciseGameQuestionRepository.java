package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.ExerciseGameQuestion;

@Repository
public interface IExerciseGameQuestionRepository extends JpaRepository<ExerciseGameQuestion, Long>{
	
//	List<ExerciseGameQuestion> findByExerciseId(long exerciseId);	
//	
//	List<ExerciseGameQuestion> findByGameId(long gameId);

	@Query(value = "SELECT questionId FROM ExerciseGameQuestion WHERE exerciseId = ?1 and isDisable = false")
	List<Long> findAllQuestionIdByExerciseId(long exerciseId);
	
	@Query(value = "SELECT questionId FROM ExerciseGameQuestion WHERE gameId = ?1 and isDisable = false")
	List<Long> findAllQuestionIdByGameId(long gameId);
	
	ExerciseGameQuestion findByIdAndIsDisableFalse(long id);
	
	ExerciseGameQuestion findByQuestionIdAndExerciseIdAndIsDisableFalse(long questionId, long exerciseId);
	
	ExerciseGameQuestion findByQuestionIdAndGameIdAndIsDisableFalse(long questionId, long exerciseId);
}
