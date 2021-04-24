package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.ExerciseTaken;

@Repository
public interface IExerciseTakenRepository extends JpaRepository<ExerciseTaken, Long> {
	List<ExerciseTaken> findByExerciseIdAndAccountId(long exerciseId, long accountId);

	List<ExerciseTaken> findByUnitIdAndAccountId(long unitId, long accountId);

	List<ExerciseTaken> findByProgressTestIdAndAccountId(long progressTestId, long accountId);
}
