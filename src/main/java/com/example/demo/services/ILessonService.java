package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.LessonResponseDTO;
import com.example.demo.dtos.LessonRequestDTO;

public interface ILessonService {

	List<LessonResponseDTO> findByUnitIdOrderByLessonNameAsc(long unitId);
	LessonResponseDTO findById(long id);
	String createLesson(LessonRequestDTO lessonRequestDTO);
	String updateLesson(long id, LessonRequestDTO lessonRequestDTO);
	String deleteLesson(long id);
	
}
