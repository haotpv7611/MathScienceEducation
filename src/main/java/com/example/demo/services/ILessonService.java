package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.LessonDTO;
import com.example.demo.dtos.LessonRequestDTO;

public interface ILessonService {

	List<LessonDTO> findByUnitIdOrderByLessonNameAsc(long unitId);
	LessonDTO findById(long id);
	String createLesson(LessonRequestDTO lessonRequestDTO);
	String updateLesson(LessonRequestDTO lessonRequestDTO);
	String deleteLesson(long id);
	
}
