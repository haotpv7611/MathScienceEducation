package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.ExerciseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
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
		List<Exercise> exerciseList = iExerciseRepository.findByLessonIdAndIsDisableOrderByExerciseNameAsc(lessonId,
				false);
		List<ExerciseDTO> exerciseDTOList = new ArrayList<>();

		if (!exerciseList.isEmpty()) {
			for (Exercise exercise : exerciseList) {
				exerciseDTOList.add(modelMapper.map(exercise, ExerciseDTO.class));
			}
		}
		return exerciseDTOList;
	}

	@Override
	public List<ExerciseDTO> findByProgressTestIdAndIsDisableOrderByExerciseNameAsc(long progressTestId) {
		List<Exercise> exerciseList = iExerciseRepository
				.findByProgressTestIdAndIsDisableOrderByExerciseNameAsc(progressTestId, false);
		List<ExerciseDTO> exerciseDTOList = new ArrayList<>();
		if (!exerciseList.isEmpty()) {
			for (Exercise exercise : exerciseList) {
				exerciseDTOList.add(modelMapper.map(exercise, ExerciseDTO.class));
			}
		}
		return exerciseDTOList;
	}

	@Override
	public String createExercise(ExerciseDTO exerciseDTO) {
		// check lessonId ?
		if (exerciseDTO.getLessonId() != 0) {
			// check exercise name in lesson is existed or not
			List<Exercise> listExercises = iExerciseRepository
					.findByLessonIdOrderByExerciseNameAsc(exerciseDTO.getLessonId());
			for (Exercise exercise1 : listExercises) {
				if (exerciseDTO.getExerciseName().equals(exercise1.getExerciseName())) {
					return "Exercise is existed !";
				}
			}
			Exercise exercise = modelMapper.map(exerciseDTO, Exercise.class);
			exercise.setProgressTestId(6);
			exercise.setProgressTest(false);
			exercise.setDisable(false);
			iExerciseRepository.save(exercise);
		}
		// check progressTestId ?
		if (exerciseDTO.getProgressTestId() != 0) {
			// check exercise name in progress test is existed or not
			List<Exercise> listExercises = iExerciseRepository
					.findByProgressTestIdAndIsDisableOrderByExerciseNameAsc(exerciseDTO.getProgressTestId(), false);
			for (Exercise exercise : listExercises) {
				if (exerciseDTO.getExerciseName().equals(exercise.getExerciseName())) {
					return "Exercise is existed !";
				}
			}
			Exercise exercise1 = modelMapper.map(exerciseDTO, Exercise.class);
			exercise1.setLessonId(4);
			exercise1.setProgressTest(true);
			exercise1.setDisable(false);
			iExerciseRepository.save(exercise1);
		}
		return "CREATE SUCCESS !";
	}

	@Override
	public String updateExercise(ExerciseDTO exerciseDTO) {
		// check exercise is existed or not
		Exercise exercise = iExerciseRepository.findById(exerciseDTO.getId())
				.orElseThrow(() -> new ResourceNotFoundException());
		if (exercise.isDisable()) {
			throw new ResourceNotFoundException();
		}
		if (exerciseDTO.getLessonId() != 0) {
			// check exercise name in lesson is existed or not
			if (!exercise.getExerciseName().equalsIgnoreCase(exerciseDTO.getExerciseName())) {
				List<Exercise> listExercises = iExerciseRepository
						.findByLessonIdOrderByExerciseNameAsc(exerciseDTO.getLessonId());
				for (Exercise exercise1 : listExercises) {
					if (exerciseDTO.getExerciseName().equals(exercise1.getExerciseName())) {
						return "Exercise is existed !";
					}
				}
			}

			exercise.setExerciseName(exerciseDTO.getExerciseName());
			exercise.setDescription(exerciseDTO.getDescription());
			iExerciseRepository.save(exercise);
		}
		// check progressTestId ?
		if (exerciseDTO.getProgressTestId() != 0) {
			// check exercise name in progress test is existed or not
			if (!exercise.getExerciseName().equalsIgnoreCase(exerciseDTO.getExerciseName())) {
				List<Exercise> listExercises = iExerciseRepository
						.findByProgressTestIdAndIsDisableOrderByExerciseNameAsc(exerciseDTO.getProgressTestId(), false);
				for (Exercise exercise1 : listExercises) {
					if (exerciseDTO.getExerciseName().equals(exercise1.getExerciseName())) {
						return "Exercise is existed !";
					}
				}
			}

			exercise.setExerciseName(exerciseDTO.getExerciseName());
			exercise.setDescription(exerciseDTO.getDescription());
			iExerciseRepository.save(exercise);
		}
		return "CREATE SUCCESS !";

	}

	@Override
	public String deleteExercise(long id) {
		Exercise exercise = iExerciseRepository.findByIdAndIsDisableFalse(id);
		if (exercise == null) {
			throw new ResourceNotFoundException();
		}
		
		//????
		if (exercise.getLessonId() != 0) {
			exercise.setDisable(true);
		}
		if (exercise.getProgressTestId() != 0) {
			exercise.setDisable(true);
		}
		iExerciseRepository.save(exercise);
		return "DELETE SUCCESS";
	}

}
