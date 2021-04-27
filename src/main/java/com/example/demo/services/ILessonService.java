package com.example.demo.services;

import java.util.List;
import java.util.Map;

import com.example.demo.dtos.LessonResponseDTO;
import com.example.demo.dtos.LessonRequestDTO;

public interface ILessonService {

	Object findById(long id);

	List<LessonResponseDTO> findByUnitIdOrderByLessonNameAsc(long unitId);
	
	Map<String, List<LessonResponseDTO>> findByUnitIdStudentView(long unitId);

	Map<Long, Integer> findAllLesson();

	String createLesson(LessonRequestDTO lessonRequestDTO);

	String updateLesson(long id, LessonRequestDTO lessonRequestDTO);

	void deleteOneLesson(long id);
}
