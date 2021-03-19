package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.ExerciseDTO;
import com.example.demo.models.Exercise;
import com.example.demo.repositories.IExerciseRepository;
import com.example.demo.services.IExerciseService;

@Service
public class ExerciseServiceImpl implements IExerciseService {

	@Autowired
	private IExerciseRepository iExerciseRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public List<ExerciseDTO> findByLessonIdAndIsDisableOrderByExerciseNameAsc(long lessonId) {
		List<Exercise> exerciseList = iExerciseRepository.findByLessonIdAndIsDisableOrderByExerciseNameAsc(lessonId, false);
		List<ExerciseDTO> exerciseDTOList = new ArrayList<>();
		
		if(!exerciseList.isEmpty()) {
			for (Exercise exercise : exerciseList) {
				exerciseDTOList.add(modelMapper.map(exercise, ExerciseDTO.class));
			}
		}		
		return exerciseDTOList;
	}

	@Override
	public List<ExerciseDTO> findByProgressTestIdAndIsDisableOrderByExerciseNameAsc(long progressTestId) {
		List<Exercise> exerciseList = iExerciseRepository.findByProgressTestIdAndIsDisableOrderByExerciseNameAsc(progressTestId, false);
		List<ExerciseDTO> exerciseDTOList = new ArrayList<>();
		if(!exerciseList.isEmpty()) {
			for (Exercise exercise : exerciseList) {
				exerciseDTOList.add(modelMapper.map(exercise, ExerciseDTO.class));
			}
		}
		return exerciseDTOList;
	}

}
