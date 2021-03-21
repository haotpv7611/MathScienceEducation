package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.ExerciseGameQuestion;

@Repository
public interface IExerciseGameQuestionRepository extends JpaRepository<ExerciseGameQuestion, Long>{
	
	@Query(value = "SELECT questionId FROM ExerciseGameQuestion WHERE exerciseId = ?1 and isDisable = false")
	List<Long> findAllQuestionIdByExerciseId(long exerciseId);
	
	@Query(value = "SELECT questionId FROM ExerciseGameQuestion WHERE gameId = ?1 and isDisable = false")
	List<Long> findAllQuestionIdByGameId(long gameId);
}
