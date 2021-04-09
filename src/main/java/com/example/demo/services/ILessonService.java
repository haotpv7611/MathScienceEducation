package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.LessonResponseDTO;
import com.example.demo.dtos.LessonRequestDTO;

public interface ILessonService {

	Object findById(long id);

	List<LessonResponseDTO> findByUnitIdOrderByLessonNameAsc(long unitId);

	String createLesson(LessonRequestDTO lessonRequestDTO);

	String updateLesson(long id, LessonRequestDTO lessonRequestDTO);

	String deleteLesson(long id);

	void deleteOneLesson(long id);
}
