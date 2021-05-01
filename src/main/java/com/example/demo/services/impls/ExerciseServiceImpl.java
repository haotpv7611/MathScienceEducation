package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.ExerciseRequestDTO;
import com.example.demo.dtos.ExerciseResponseDTO;
import com.example.demo.dtos.IdAndStatusDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Exercise;
import com.example.demo.models.ExerciseGameQuestion;
import com.example.demo.models.ExerciseTaken;
import com.example.demo.models.Lesson;
import com.example.demo.models.ProgressTest;
import com.example.demo.repositories.IExerciseGameQuestionRepository;
import com.example.demo.repositories.IExerciseRepository;
import com.example.demo.repositories.IExerciseTakenRepository;
import com.example.demo.repositories.ILessonRepository;
import com.example.demo.repositories.IProgressTestRepository;
import com.example.demo.services.IExerciseGameQuestionService;
import com.example.demo.services.IExerciseService;

@Service
public class ExerciseServiceImpl implements IExerciseService {
	Logger logger = LoggerFactory.getLogger(ExerciseServiceImpl.class);
	private final String ACTIVE_STATUS = "ACTIVE";
	private final String INACTIVE_STATUS = "INACTIVE";
	private final String DELETED_STATUS = "DELETED";

	@Autowired
	private IExerciseRepository iExerciseRepository;

	@Autowired
	private IExerciseGameQuestionService iExerciseGameQuestionService;

	@Autowired
	private IExerciseGameQuestionRepository iExerciseGameQuestionRepository;

	@Autowired
	private IProgressTestRepository iProgressTestRepository;

	@Autowired
	private ILessonRepository iLessonRepository;

	@Autowired
	private IExerciseTakenRepository iExerciseTakenRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public String findExerciseStatusById(long id) {
		String status = null;
		try {
			Exercise exercise = iExerciseRepository.findByIdAndStatusNot(id, DELETED_STATUS);
			if (exercise == null) {
				throw new ResourceNotFoundException();
			}
			status = exercise.getStatus();
		} catch (Exception e) {
			logger.error("Find all exercises by id = " + id + "! " + e.getMessage());
		}

		return status;
	}

	// should modify find by lesson --> lesson or progressTest
	@Override
	public List<ExerciseResponseDTO> findByLessonIdOrderByExerciseNameAsc(long lessonId) {
		List<ExerciseResponseDTO> exerciseResponseDTOList = new ArrayList<>();
		try {
			List<Exercise> exerciseList = iExerciseRepository.findByLessonIdAndStatusNotOrderByExerciseNameAsc(lessonId,
					DELETED_STATUS);
			if (!exerciseList.isEmpty()) {
				for (Exercise exercise : exerciseList) {
					ExerciseResponseDTO exerciseResponseDTO = modelMapper.map(exercise, ExerciseResponseDTO.class);
					exerciseResponseDTOList.add(exerciseResponseDTO);
				}
			}
		} catch (Exception e) {
			logger.error("Find all exercises by lessonId = " + lessonId + "! " + e.getMessage());

			return null;
		}

		return exerciseResponseDTOList;
	}

	// should modify find by lesson student view --> lesson or progressTest student
	// view
	@Override
	public List<ExerciseResponseDTO> findByLessonIdStudentView(long lessonId, long accountId) {
		List<ExerciseResponseDTO> exerciseResponseDTOList = new ArrayList<>();
		try {
			// find all exercise
			List<Exercise> exerciseList = iExerciseRepository.findByLessonIdAndStatusOrderByExerciseNameAsc(lessonId,
					ACTIVE_STATUS);
			if (!exerciseList.isEmpty()) {
				for (Exercise exercise : exerciseList) {
					// check have question in exercise
					// if not have, not display for student
					List<ExerciseGameQuestion> exerciseGameQuestionList = iExerciseGameQuestionRepository
							.findByExerciseIdAndIsDisableFalse(exercise.getId());
					if (!exerciseGameQuestionList.isEmpty()) {
						ExerciseResponseDTO exerciseResponseDTO = modelMapper.map(exercise, ExerciseResponseDTO.class);
						// check exercise isTaken
						List<ExerciseTaken> exerciseTakenList = iExerciseTakenRepository
								.findByExerciseIdAndAccountId(exercise.getId(), accountId);
						exerciseResponseDTO.setDone(false);
						if (!exerciseTakenList.isEmpty()) {
							exerciseResponseDTO.setDone(true);
						}
						exerciseResponseDTOList.add(exerciseResponseDTO);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Find all exercises student view by lessonId = " + lessonId + "! " + e.getMessage());

			return null;
		}

		return exerciseResponseDTOList;
	}

	@Override
	public List<ExerciseResponseDTO> findByProgressTestIdOrderByExerciseNameAsc(long progressTestId) {
		List<ExerciseResponseDTO> exerciseResponseDTOList = new ArrayList<>();
		try {
			List<Exercise> exerciseList = iExerciseRepository
					.findByProgressTestIdAndStatusNotOrderByExerciseNameAsc(progressTestId, DELETED_STATUS);
			if (!exerciseList.isEmpty()) {
				for (Exercise exercise : exerciseList) {
					ExerciseResponseDTO exerciseResponseDTO = modelMapper.map(exercise, ExerciseResponseDTO.class);
					exerciseResponseDTOList.add(exerciseResponseDTO);
				}
			}
		} catch (Exception e) {
			logger.error("Find all exercises by progressTestId = " + progressTestId + "! " + e.getMessage());

			return null;
		}

		return exerciseResponseDTOList;
	}

	@Override
	public List<ExerciseResponseDTO> findByProgressTestIdStudentView(long progressTestId, long accountId) {
		List<ExerciseResponseDTO> exerciseResponseDTOList = new ArrayList<>();
		try {
			// find all exercise
			List<Exercise> exerciseList = iExerciseRepository
					.findByProgressTestIdAndStatusOrderByExerciseNameAsc(progressTestId, ACTIVE_STATUS);
			if (!exerciseList.isEmpty()) {
				for (Exercise exercise : exerciseList) {
					// check have question in exercise
					// if not have, not display for student
					List<ExerciseGameQuestion> exerciseGameQuestionList = iExerciseGameQuestionRepository
							.findByExerciseIdAndIsDisableFalse(exercise.getId());
					if (!exerciseGameQuestionList.isEmpty()) {
						ExerciseResponseDTO exerciseResponseDTO = modelMapper.map(exercise, ExerciseResponseDTO.class);
						// check exercise isTaken
						List<ExerciseTaken> exerciseTakenList = iExerciseTakenRepository
								.findByExerciseIdAndAccountId(exercise.getId(), accountId);
						if (!exerciseTakenList.isEmpty()) {
							exerciseResponseDTO.setDone(true);
						}
						exerciseResponseDTOList.add(exerciseResponseDTO);
					}
				}
			}
		} catch (Exception e) {
			logger.error(
					"Find all exercises student view by progressTestId = " + progressTestId + "! " + e.getMessage());

			return null;
		}

		return exerciseResponseDTOList;
	}

	@Override
	public Map<Long, Integer> findAllExercise() {
		Map<Long, Integer> exerciseMap = new HashMap<>();
		try {

			List<Exercise> exerciseList = iExerciseRepository.findByStatusNot(DELETED_STATUS);
			for (Exercise exercise : exerciseList) {
				exerciseMap.put(exercise.getId(), exercise.getExerciseName());
			}
		} catch (Exception e) {
			logger.error("Find all exercises! " + e.getMessage());

			return null;
		}

		return exerciseMap;
	}

	@Override
	public String createExercise(ExerciseRequestDTO exerciseRequestDTO) {
		boolean isProgressTest = exerciseRequestDTO.isProgressTest();
		long lessonId = exerciseRequestDTO.getLessonId();
		long progressTestId = exerciseRequestDTO.getProgressTestId();
		int exerciseName = exerciseRequestDTO.getExerciseName();
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
				if (iExerciseRepository.findByProgressTestIdAndExerciseNameAndStatusNot(progressTestId,
						exerciseName, DELETED_STATUS) != null) {

					return "EXISTED";
				}
				exercise = new Exercise(exerciseName, description, 0, progressTestId, isProgressTest, INACTIVE_STATUS);
			} else {
				if (iExerciseRepository.findByLessonIdAndExerciseNameAndStatusNot(lessonId, exerciseName,
						DELETED_STATUS) != null) {

					return "EXISTED";
				}
				exercise = new Exercise(exerciseName, description, lessonId, 0, isProgressTest, INACTIVE_STATUS);
			}
			iExerciseRepository.save(exercise);
		} catch (Exception e) {
			if (isProgressTest) {
				logger.error("Create exercise with name= " + exerciseName + ", in progressTestId =  " + progressTestId
						+ "! " + e.getMessage());
			} else {
				logger.error("Create exercise with name= " + exerciseName + ", in lessonId =  " + lessonId + "! "
						+ e.getMessage());
			}
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CREATE FAIL!";
		}

		return "CREATE SUCCESS!";
	}

	@Override
	public String updateExercise(long id, ExerciseRequestDTO exerciseRequestDTO) {
		boolean isProgressTest = exerciseRequestDTO.isProgressTest();
		long progressTestId = exerciseRequestDTO.getProgressTestId();
		long lessonId = exerciseRequestDTO.getLessonId();
		int exerciseName = exerciseRequestDTO.getExerciseName();
		String description = exerciseRequestDTO.getDescription();

		try {
			// validate exerciseId and check exerciseName existed
			Exercise exercise = iExerciseRepository.findByIdAndStatusNot(id, DELETED_STATUS);
			if (exercise == null) {
				throw new ResourceNotFoundException();
			}

			if (exercise.getExerciseName() != exerciseName) {
				if (isProgressTest) {
					if (iExerciseRepository.findByProgressTestIdAndExerciseNameAndStatusNot(progressTestId,
							exerciseName, DELETED_STATUS) != null) {

						return "EXISTED";
					}
				} else {
					if (iExerciseRepository.findByLessonIdAndExerciseNameAndStatusNot(lessonId, exerciseName,
							DELETED_STATUS) != null) {

						return "EXISTED";
					}
				}
			}
			exercise.setExerciseName(exerciseName);
			exercise.setDescription(description);
			iExerciseRepository.save(exercise);
		} catch (Exception e) {
			logger.error("Update exercise with id= " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "UPDATE FAIL!";
		}

		return "UPDATE SUCCESS!";
	}

	@Override
	@Transactional
	public String changeOneExerciseStatus(IdAndStatusDTO idAndStatusDTO) {
		long id = idAndStatusDTO.getId();
		String status = idAndStatusDTO.getStatus();
		try {
			// validate exerciseId
			Exercise exercise = iExerciseRepository.findByIdAndStatusNot(id, DELETED_STATUS);
			if (exercise == null) {
				throw new ResourceNotFoundException();
			}

			// if delete must delete question in exercise first
			if (status.equals(DELETED_STATUS)) {
				if (exercise.getStatus().equalsIgnoreCase(ACTIVE_STATUS)) {

					return "CANNOT DELETE";
				}
				List<ExerciseGameQuestion> exerciseGameQuestionList = iExerciseGameQuestionRepository
						.findByExerciseIdAndIsDisableFalse(exercise.getId());
				if (!exerciseGameQuestionList.isEmpty()) {
					for (ExerciseGameQuestion exerciseGameQuestion : exerciseGameQuestionList) {
						iExerciseGameQuestionService.deleteOneExerciseGameQuestion(exerciseGameQuestion.getId());
					}
				}
			}

			exercise.setStatus(status);
			iExerciseRepository.save(exercise);
		} catch (Exception e) {
			throw e;
		}
		
		return "OK";
	}

}
