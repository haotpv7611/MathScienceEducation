package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.LessonDTO;
import com.example.demo.dtos.LessonRequestDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Exercise;
import com.example.demo.models.Lesson;
import com.example.demo.models.Unit;
import com.example.demo.repositories.IExerciseRepository;
import com.example.demo.repositories.ILessonRepository;
import com.example.demo.repositories.IUnitRepository;
import com.example.demo.services.IExerciseService;
import com.example.demo.services.ILessonService;

@Service
public class LessonServiceImpl implements ILessonService {

	@Autowired
	ILessonRepository iLessonRepository;
	@Autowired
	IUnitRepository iUnitRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	IExerciseRepository iExerciseRepository;
	@Autowired
	IExerciseService iExerciseService;

	@Override
	public List<LessonDTO> findByUnitIdOrderByLessonNameAsc(long unitId) {
		List<Lesson> lessonList = iLessonRepository.findByUnitIdAndIsDisableOrderByLessonNameAsc(unitId, false);
		List<LessonDTO> lessonDTOList = new ArrayList<>();
		if (!lessonList.isEmpty()) {
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

	@Override
	public String createLesson(LessonRequestDTO lessonRequestDTO) {
		Unit unit = iUnitRepository.findByIdAndIsDisable(lessonRequestDTO.getUnitId(), false);
		if (unit == null) {
			return "Unit is not existed !";
		}
		List<Lesson> listLessons = iLessonRepository
				.findByUnitIdAndIsDisableOrderByLessonNameAsc(lessonRequestDTO.getUnitId(), false);
		for (Lesson lesson : listLessons) {
			if (lessonRequestDTO.getLessonName().equalsIgnoreCase(lesson.getLessonName())) {
				return "Lesson is existed !";
			}
		}
		Lesson lesson = modelMapper.map(lessonRequestDTO, Lesson.class);
		lesson.setDisable(false);
		iLessonRepository.save(lesson);
		return "CREATE SUCCESS !";
	}

	@Override
	public String updateLesson(long id, LessonRequestDTO lessonRequestDTO) {

		Lesson lesson = iLessonRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
		if (lesson.isDisable()) {
			throw new ResourceNotFoundException();
		}
		if (!lesson.getLessonName().equalsIgnoreCase(lessonRequestDTO.getLessonName())) {
			List<Lesson> listLessons = iLessonRepository
					.findByUnitIdAndIsDisableOrderByLessonNameAsc(lessonRequestDTO.getUnitId(), false);
			for (Lesson lesson1 : listLessons) {
				if (lessonRequestDTO.getLessonName().equalsIgnoreCase(lesson1.getLessonName())) {
					return "Lesson is existed !";
				}
			}
		}

		lesson.setLessonName(lessonRequestDTO.getLessonName());
		lesson.setLessonUrl(lessonRequestDTO.getLessonUrl());
		iLessonRepository.save(lesson);
		return "UPDATE SUCCESS !";

	}

	@Override
	@Transactional
	public String deleteLesson(long id) {
		Lesson lesson = iLessonRepository.findByIdAndIsDisable(id, false);
		if (lesson == null) {
			throw new ResourceNotFoundException();
		}
		List<Exercise> exercises = iExerciseRepository.findByLessonIdOrderByExerciseNameAsc(id);
		if (!exercises.isEmpty()) {
			for (Exercise exercise : exercises) {
				iExerciseService.deleteExercise(exercise.getId());
			}
		}
		
		//thieu delete game

		lesson.setDisable(true);
		iLessonRepository.save(lesson);
		return "DELETE SUCCESS !";
	}

}
