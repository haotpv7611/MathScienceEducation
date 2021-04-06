package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Lesson;

@Repository
public interface ILessonRepository extends JpaRepository<Lesson, Long> {
	List<Lesson> findByUnitIdAndIsDisableFalse(long unitId);

	List<Lesson> findByUnitIdAndIsDisableFalseOrderByLessonNameAsc(long unitId);

	Lesson findByIdAndIsDisableFalse(long id);
	
	Lesson findByUnitIdAndLessonNameIgnoreCaseAndIsDisableFalse(long id, String lessonName);
}
