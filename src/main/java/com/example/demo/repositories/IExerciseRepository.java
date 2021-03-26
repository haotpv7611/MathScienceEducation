package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Exercise;

@Repository
public interface IExerciseRepository extends JpaRepository<Exercise, Long> {

	List<Exercise> findByLessonIdAndIsDisableOrderByExerciseNameAsc(long lessonId, boolean isDisable);
	
	List<Exercise> findByProgressTestIdAndIsDisableOrderByExerciseNameAsc(long progressTestId, boolean isDisable);
}