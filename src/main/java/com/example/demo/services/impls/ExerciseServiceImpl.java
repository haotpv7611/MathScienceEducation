package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.ExerciseDTO;
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

	@Override
	public String createExercise(ExerciseDTO exerciseDTO) {
		boolean isProgressTest = exerciseDTO.isProgressTest();
		long lessonId = exerciseDTO.getLessonId();
		long progressTestId = exerciseDTO.getProgressTestId();
		String exerciseName = exerciseDTO.getExerciseName();
		String description = exerciseDTO.getDescription();

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
//		// check lessonId ?
//		if (exerciseDTO.getLessonId() != 0) {
//			// check exercise name in lesson is existed or not
//			List<Exercise> listExercises = iExerciseRepository
//					.findByLessonIdOrderByExerciseNameAsc(exerciseDTO.getLessonId());
//			for (Exercise exercise1 : listExercises) {
//				if (exerciseDTO.getExerciseName().equals(exercise1.getExerciseName())) {
//					return "Exercise is existed !";
//				}
//			}
//			Exercise exercise = modelMapper.map(exerciseDTO, Exercise.class);
//			exercise.setProgressTestId(6);
//			exercise.setProgressTest(false);
//			exercise.setDisable(false);
//			iExerciseRepository.save(exercise);
//		}
//		// check progressTestId ?
//		if (exerciseDTO.getProgressTestId() != 0) {
//			// check exercise name in progress test is existed or not
//			List<Exercise> listExercises = iExerciseRepository
//					.findByProgressTestIdAndIsDisableOrderByExerciseNameAsc(exerciseDTO.getProgressTestId(), false);
//			for (Exercise exercise : listExercises) {
//				if (exerciseDTO.getExerciseName().equals(exercise.getExerciseName())) {
//					return "Exercise is existed !";
//				}
//			}
//			Exercise exercise1 = modelMapper.map(exerciseDTO, Exercise.class);
//			exercise1.setLessonId(4);
//			exercise1.setProgressTest(true);
//			exercise1.setDisable(false);
//			iExerciseRepository.save(exercise1);
//		}

	}

	@Override
	public String updateExercise(long id, ExerciseDTO exerciseDTO) {

		// check exercise is existed or not
		boolean isProgressTest = exerciseDTO.isProgressTest();

		long progressTestId = exerciseDTO.getProgressTestId();
		long lessonId = exerciseDTO.getLessonId();
		String exerciseName = exerciseDTO.getExerciseName();
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
		exercise.setExerciseName(exerciseDTO.getExerciseName());
		exercise.setDescription(exerciseDTO.getDescription());
		iExerciseRepository.save(exercise);

		return "UPDATE SUCCESS!";
//
//		if (exerciseDTO.getLessonId() != 0) {
//			// check exercise name in lesson is existed or not
//			if (!exercise.getExerciseName().equalsIgnoreCase(exerciseDTO.getExerciseName())) {
//				List<Exercise> listExercises = iExerciseRepository
//						.findByLessonIdOrderByExerciseNameAsc(exerciseDTO.getLessonId());
//				for (Exercise exercise1 : listExercises) {
//					if (exerciseDTO.getExerciseName().equals(exercise1.getExerciseName())) {
//						return "Exercise is existed !";
//					}
//				}
//			}
//
//			exercise.setExerciseName(exerciseDTO.getExerciseName());
//			exercise.setDescription(exerciseDTO.getDescription());
//			iExerciseRepository.save(exercise);
//		}
//		// check progressTestId ?
//		if (exerciseDTO.getProgressTestId() != 0) {
//			// check exercise name in progress test is existed or not
//			if (!exercise.getExerciseName().equalsIgnoreCase(exerciseDTO.getExerciseName())) {
//				List<Exercise> listExercises = iExerciseRepository
//						.findByProgressTestIdAndIsDisableOrderByExerciseNameAsc(exerciseDTO.getProgressTestId(), false);
//				for (Exercise exercise1 : listExercises) {
//					if (exerciseDTO.getExerciseName().equals(exercise1.getExerciseName())) {
//						return "Exercise is existed !";
//					}
//				}
//			}
//
//			exercise.setExerciseName(exerciseDTO.getExerciseName());
//			exercise.setDescription(exerciseDTO.getDescription());
//			iExerciseRepository.save(exercise);
//		}

	}
//
//	@Override
//	public String deleteExercise(List<Long> ids) {
//		for (Long id : ids) {
//			deleteOneExercise(id);
//		}
//
//		return "DELETE SUCCESS!";
//	}

//		Exercise exercise = iExerciseRepository.findByIdAndIsDisableFalse(id);
//		if (exercise == null) {
//			throw new ResourceNotFoundException();
//		}
//		List<ExerciseGameQuestion> exerciseGameQuestionLists = iExerciseGameQuestionRepository.findByExerciseId(id);
//		if (exerciseGameQuestionLists != null) {
//			for (ExerciseGameQuestion exerciseGameQuestion : exerciseGameQuestionLists) {
//				iExerciseGameQuestionService.deleteQuestionFromExercise(exerciseGameQuestion.getId());
//			}
//		}
//		exercise.setDisable(true);
//		iExerciseRepository.save(exercise);
//		return "DELETE SUCCESS";

	@Override
	@Transactional
	public void deleteOneExercise(long id) {
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
	}

}
