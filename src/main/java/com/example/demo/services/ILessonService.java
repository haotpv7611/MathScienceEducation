package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.LessonDTO;

public interface ILessonService {

	List<LessonDTO> findByUnitIdOrderByLessonNameAsc(long unitId);
	LessonDTO findById(long id);
}
