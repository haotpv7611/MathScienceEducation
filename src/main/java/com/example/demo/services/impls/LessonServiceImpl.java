package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.LessonResponseDTO;
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

	// done
	@Override
	public List<LessonResponseDTO> findByUnitIdOrderByLessonNameAsc(long unitId) {
		List<Lesson> lessonList = iLessonRepository.findByUnitIdAndIsDisableFalseOrderByLessonNameAsc(unitId);
		List<LessonResponseDTO> lessonResponseDTOList = new ArrayList<>();
		if (!lessonList.isEmpty()) {
			for (Lesson lesson : lessonList) {
				LessonResponseDTO lessonResponseDTO = modelMapper.map(lesson, LessonResponseDTO.class);
				lessonResponseDTOList.add(lessonResponseDTO);
			}
		}

		return lessonResponseDTOList;
	}

	// done
	@Override
	public LessonResponseDTO findById(long id) {
		Lesson lesson = iLessonRepository.findByIdAndIsDisableFalse(id);
		if (lesson == null) {
			throw new ResourceNotFoundException();
		}
		LessonResponseDTO lessonResponseDTO = modelMapper.map(lesson, LessonResponseDTO.class);

		return lessonResponseDTO;
	}

	// done
	@Override
	public String createLesson(LessonRequestDTO lessonRequestDTO) {
		Unit unit = iUnitRepository.findByIdAndIsDisableFalse(lessonRequestDTO.getUnitId());
		if (unit == null) {
			throw new ResourceNotFoundException();
		}

		long unitId = lessonRequestDTO.getUnitId();
		String lessonName = lessonRequestDTO.getLessonName();
		if (iLessonRepository.findByUnitIdAndLessonNameIgnoreCaseAndIsDisableFalse(unitId, lessonName) != null) {
			return "EXISTED";
		}

		Lesson lesson = modelMapper.map(lessonRequestDTO, Lesson.class);
		lesson.setDisable(false);
		iLessonRepository.save(lesson);

		return "CREATE SUCCESS!";
	}

	// done
	@Override
	public String updateLesson(long id, LessonRequestDTO lessonRequestDTO) {
		Lesson lesson = iLessonRepository.findByIdAndIsDisableFalse(id);
		if (lesson == null) {
			throw new ResourceNotFoundException();
		}
		Unit unit = iUnitRepository.findByIdAndIsDisableFalse(lessonRequestDTO.getUnitId());
		if (unit == null) {
			throw new ResourceNotFoundException();
		}

		long unitId = lessonRequestDTO.getUnitId();
		String lessonName = lessonRequestDTO.getLessonName();
		if (!lesson.getLessonName().equalsIgnoreCase(lessonName)) {
			if (iLessonRepository.findByUnitIdAndLessonNameIgnoreCaseAndIsDisableFalse(unitId, lessonName) != null) {
				return "EXISTED";
			}
		}

		lesson.setLessonName(lessonRequestDTO.getLessonName());
		lesson.setLessonUrl(lessonRequestDTO.getLessonUrl());
		iLessonRepository.save(lesson);

		return "UPDATE SUCCESS!";

	}

	@Override
	@Transactional
	public String deleteLesson(long id) {
		Lesson lesson = iLessonRepository.findByIdAndIsDisableFalse(id);
		if (lesson == null) {
			throw new ResourceNotFoundException();
		}
		List<Exercise> exercises = iExerciseRepository.findByLessonIdOrderByExerciseNameAsc(id);
		if (!exercises.isEmpty()) {
			for (Exercise exercise : exercises) {
				iExerciseService.deleteExercise(exercise.getId());
			}
		}

		// thieu delete game

		lesson.setDisable(true);
		iLessonRepository.save(lesson);
		return "DELETE SUCCESS !";
	}

}
