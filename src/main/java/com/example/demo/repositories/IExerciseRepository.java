package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Exercise;

@Repository
public interface IExerciseRepository extends JpaRepository<Exercise, Long> {

	List<Exercise> findByLessonIdAndIsDisableFalse(long lessonId);

	List<Exercise> findByLessonIdAndIsDisableFalseOrderByExerciseNameAsc(long lessonId);

	List<Exercise> findByProgressTestIdAndIsDisableFalse(long progressTestId);

	List<Exercise> findByProgressTestIdAndIsDisableFalseOrderByExerciseNameAsc(long progressTestId);

	Exercise findByIdAndIsDisableFalse(long id);

	Exercise findByLessonIdAndExerciseNameAndIsDisableFalse(long lessonId, String exerciseName);

	Exercise findByProgressTestIdAndExerciseNameAndIsDisableFalse(long progressTestId, String exerciseName);
}
