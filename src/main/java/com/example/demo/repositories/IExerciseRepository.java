package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Exercise;

@Repository
public interface IExerciseRepository extends JpaRepository<Exercise, Long> {
	
	List<Exercise> findByStatusNot(String status);

	List<Exercise> findByLessonIdAndStatusNot(long lessonId, String status);

	List<Exercise> findByLessonIdAndStatusNotOrderByExerciseNameAsc(long lessonId, String status);
	
	List<Exercise> findByLessonIdAndStatusOrderByExerciseNameAsc(long lessonId, String status);

	List<Exercise> findByProgressTestIdAndStatusNot(long progressTestId, String status);

	List<Exercise> findByProgressTestIdAndStatusNotOrderByExerciseNameAsc(long progressTestId, String status);
	
	List<Exercise> findByProgressTestIdAndStatusOrderByExerciseNameAsc(long progressTestId, String status);

	Exercise findByIdAndStatusNot(long id, String status);

	Exercise findByLessonIdAndExerciseNameAndStatusNot(long lessonId, int exerciseName, String status);

	Exercise findByProgressTestIdAndExerciseNameAndStatusNot(long progressTestId, int exerciseName, String status);
}
