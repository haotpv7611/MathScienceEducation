package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.LessonDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Lesson;
import com.example.demo.repositories.ILessonRepository;
import com.example.demo.services.ILessonService;

@Service
public class LessonServiceImpl implements ILessonService {

	@Autowired
	ILessonRepository iLessonRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public List<LessonDTO> findByUnitIdOrderByLessonNameAsc(long unitId) {
		List<Lesson> lessonList = iLessonRepository.findByUnitIdAndIsDisableOrderByLessonNameAsc(unitId, false);
		List<LessonDTO> lessonDTOList = new ArrayList<>();
		
		if(!lessonList.isEmpty()) {
			for (Lesson lesson : lessonList) {
				lessonDTOList.add(modelMapper.map(lesson, LessonDTO.class));
			}
		}
		return lessonDTOList;
	}

	@Override
	public LessonDTO findById(long id) {
		Lesson lesson = iLessonRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());			
		return modelMapper.map(lesson, LessonDTO.class);
	}

}
