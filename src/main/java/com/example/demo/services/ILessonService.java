package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.LessonDTO;

public interface ILessonService {

	List<LessonDTO> findByUnitIdOrderByLessonNameAsc(Long unitId);
	LessonDTO findById(Long id);
}
