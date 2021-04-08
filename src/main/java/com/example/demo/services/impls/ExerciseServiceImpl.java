package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.ExerciseDTO;
import com.example.demo.dtos.ExerciseRequestDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Exercise;
import com.example.demo.models.Lesson;
import com.example.demo.models.ProgressTest;
import com.example.demo.repositories.IExerciseRepository;
import com.example.demo.repositories.ILessonRepository;
import com.example.demo.repositories.IProgressTestRepository;
import com.example.demo.services.IExerciseGameQuestionService;
import com.example.demo.services.IExerciseService;

@Service
public class ExerciseServiceImpl implements IExerciseService {
	Logger logger = LoggerFactory.getLogger(OptionQuestionServiceImpl.class);

	@Autowired
	private IExerciseRepository iExerciseRepository;

	@Autowired
	private IExerciseGameQuestionService iExerciseGameQuestionService;

	@Autowired
	private IProgressTestRepository iProgressTestRepository;

	@Autowired
	ILessonRepository iLessonRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<ExerciseDTO> findByLessonIdOrderByExerciseNameAsc(long lessonId) {
		Lesson lesson = iLessonRepository.findByIdAndIsDisableFalse(lessonId);
		if (lesson == null) {
			throw new ResourceNotFoundException();
		}

		List<Exercise> exerciseList = iExerciseRepository
				.findByLessonIdAndIsDisableFalseOrderByExerciseNameAsc(lessonId);
		List<ExerciseDTO> exerciseDTOList = new ArrayList<>();
		if (!exerciseList.isEmpty()) {
			for (Exercise exercise : exerciseList) {
				exerciseDTOList.add(modelMapper.map(exercise, ExerciseDTO.class));
			}
		}

		return exerciseDTOList;
	}

	@Override
	public List<ExerciseDTO> findByProgressTestIdOrderByExerciseNameAsc(long progressTestId) {
		ProgressTest progressTest = iProgressTestRepository.findByIdAndIsDisableFalse(progressTestId);
		if (progressTest == null) {
			throw new ResourceNotFoundException();
		}

		List<Exercise> exerciseList = iExerciseRepository
				.findByProgressTestIdAndIsDisableFalseOrderByExerciseNameAsc(progressTestId);
		List<ExerciseDTO> exerciseDTOList = new ArrayList<>();
		if (!exerciseList.isEmpty()) {
			for (Exercise exercise : exerciseList) {
				exerciseDTOList.add(modelMapper.map(exercise, ExerciseDTO.class));
			}
		}

		return exerciseDTOList;
	}

	// done ok
	@Override
	public String createExercise(ExerciseRequestDTO exerciseRequestDTO) {
		boolean isProgressTest = exerciseRequestDTO.isProgressTest();
		long lessonId = exerciseRequestDTO.getLessonId();
		long progressTestId = exerciseRequestDTO.getProgressTestId();
		String exerciseName = exerciseRequestDTO.getExerciseName();
		String description = exerciseRequestDTO.getDescription();

		try {
			if (isProgressTest) {
				ProgressTest progressTest = iProgressTestRepository.findByIdAndIsDisableFalse(progressTestId);
				if (progressTest == null) {
					throw new ResourceNotFoundException();
				}
			} else {
				Lesson lesson = iLessonRepository.findByIdAndIsDisableFalse(lessonId);
				if (lesson == null) {
					throw new ResourceNotFoundException();
				}
			}

			Exercise exercise = null;
			if (isProgressTest) {
				exercise = new Exercise(exerciseName, description, 0, progressTestId, isProgressTest, false);
			} else {
				exercise = new Exercise(exerciseName, description, lessonId, 0, isProgressTest, false);
			}
			iExerciseRepository.save(exercise);

			return "CREATE SUCCESS!";
		} catch (Exception e) {
			logger.error(e.getMessage());

			return "CREATE FAIL!";
		}
	}

	// done ok
	@Override
	public String updateExercise(long id, ExerciseRequestDTO exerciseRequestDTO) {
		boolean isProgressTest = exerciseRequestDTO.isProgressTest();
		long progressTestId = exerciseRequestDTO.getProgressTestId();
		long lessonId = exerciseRequestDTO.getLessonId();
		String exerciseName = exerciseRequestDTO.getExerciseName();
		String description = exerciseRequestDTO.getDescription();

		try {
			// validate exerciseId
			Exercise exercise = iExerciseRepository.findByIdAndIsDisableFalse(id);
			if (exercise == null) {
				throw new ResourceNotFoundException();
			}
			if (isProgressTest) {
				ProgressTest progressTest = iProgressTestRepository.findByIdAndIsDisableFalse(progressTestId);
				if (progressTest == null) {
					throw new ResourceNotFoundException();
				}
			} else {
				Lesson lesson = iLessonRepository.findByIdAndIsDisableFalse(lessonId);
				if (lesson == null) {
					throw new ResourceNotFoundException();
				}
			}

			if (!exercise.getExerciseName().equalsIgnoreCase(exerciseName)) {
				if (isProgressTest) {
					if (iExerciseRepository.findByProgressTestIdAndExerciseNameAndIsDisableFalse(progressTestId,
							exerciseName) != null) {
						return "EXISTED";
					}
				} else {
					if (iExerciseRepository.findByLessonIdAndExerciseNameAndIsDisableFalse(lessonId,
							exerciseName) != null) {
						return "EXISTED";
					}
				}
			}
			exercise.setExerciseName(exerciseName);
			exercise.setDescription(description);
			iExerciseRepository.save(exercise);

			return "UPDATE SUCCESS!";
		} catch (Exception e) {
			logger.error(e.getMessage());

			return "UPDATE FAIL!";
		}
	}

	// done ok
	@Override
	public String deleteExercise(long id) {
		try {
			deleteOneExercise(id);
		} catch (Exception e) {
			logger.error(e.getMessage());

			return "DELETE SUCCESS!";
		}

		return "DELETE FAIL!";
	}

	// done ok
	@Override
	@Transactional
	public void deleteOneExercise(long id) {
		try {
			Exercise exercise = iExerciseRepository.findByIdAndIsDisableFalse(id);
			if (exercise == null) {
				throw new ResourceNotFoundException();
			}

			List<Long> exerciseQuestionIdList = iExerciseGameQuestionService.findAllQuestionIdByExerciseId(id);
			if (!exerciseQuestionIdList.isEmpty()) {
				for (Long exerciseQuestionId : exerciseQuestionIdList) {
					iExerciseGameQuestionService.deleteOneExerciseGameQuestion(exerciseQuestionId);
				}
			}
			exercise.setDisable(true);
			iExerciseRepository.save(exercise);
		} catch (Exception e) {
			throw e;
		}
	}

}
