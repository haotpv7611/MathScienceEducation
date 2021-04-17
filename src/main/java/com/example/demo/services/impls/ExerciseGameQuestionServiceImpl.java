package com.example.demo.services.impls;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.ExerciseGameQuestionRequestDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Exercise;
import com.example.demo.models.ExerciseGameQuestion;
import com.example.demo.models.Game;
import com.example.demo.models.Question;
import com.example.demo.repositories.IExerciseGameQuestionRepository;
import com.example.demo.repositories.IExerciseRepository;
import com.example.demo.repositories.IGameRepository;
import com.example.demo.repositories.IQuestionRepository;
import com.example.demo.services.IExerciseGameQuestionService;

@Service
public class ExerciseGameQuestionServiceImpl implements IExerciseGameQuestionService {
	Logger logger = LoggerFactory.getLogger(ExerciseGameQuestionServiceImpl.class);
	private final String DELETED_STATUS = "DELETED";

	@Autowired
	private IExerciseGameQuestionRepository iExerciseGameQuestionRepository;

	@Autowired
	private IQuestionRepository iQuestionRepository;

	@Autowired
	private IExerciseRepository iExerciseRepository;

	@Autowired
	private IGameRepository iGameRepository;

	// done ok
	@Override
	@Transactional
	public String addExerciseOrGameQuestion(ExerciseGameQuestionRequestDTO exerciseGameQuestionRequestDTO) {
		boolean isExercise = exerciseGameQuestionRequestDTO.isExercise();
		List<Long> questionIds = exerciseGameQuestionRequestDTO.getQuestionIds();
		long exerciseId = exerciseGameQuestionRequestDTO.getExerciseId();
		long gameId = exerciseGameQuestionRequestDTO.getGameId();

		try {
			// validate questionId, exerciseId or gameId and checkName existed
			for (long questionId : questionIds) {
				Question question = iQuestionRepository.findByIdAndIsDisableFalse(questionId);
				if (question == null) {
					throw new ResourceNotFoundException();
				}
			}
			if (isExercise) {
				Exercise exercise = iExerciseRepository.findByIdAndStatusNot(exerciseId, DELETED_STATUS);
				if (exercise == null) {
					throw new ResourceNotFoundException();
				}
				for (long questionId : questionIds) {
					if (iExerciseGameQuestionRepository.findByQuestionIdAndExerciseIdAndIsDisableFalse(questionId,
							exerciseId) != null) {

						return "EXISTED";
					}
				}
			} else {
				Game game = iGameRepository.findByIdAndStatusNot(gameId, DELETED_STATUS);
				if (game == null) {
					throw new ResourceNotFoundException();
				}
				for (long questionId : questionIds) {
					if (iExerciseGameQuestionRepository.findByQuestionIdAndGameIdAndIsDisableFalse(questionId,
							gameId) != null) {

						return "EXISTED";
					}
				}
			}

			// create new data and return
			ExerciseGameQuestion exerciseGameQuestion = null;
			for (long questionId : questionIds) {
				if (isExercise) {
					exerciseGameQuestion = new ExerciseGameQuestion(questionId, exerciseId, 0, isExercise, false);
				} else {
					exerciseGameQuestion = new ExerciseGameQuestion(questionId, 0, gameId, isExercise, false);
				}
				iExerciseGameQuestionRepository.save(exerciseGameQuestion);
			}
		} catch (Exception e) {
			if (isExercise) {
				logger.error("Add list questionIds = " + questionIds.toString() + ", in exerciseId = " + exerciseId
						+ e.getMessage());
			} else {
				logger.error("Add list questionIds = " + questionIds.toString() + ", in gameId = " + gameId + "!"
						+ e.getMessage());
			}
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "ADD FAIL!";
		}

		return "ADD SUCCESS!";

	}

	@Override
	@Transactional
	public String deleteExerciseOrGameQuestion(ExerciseGameQuestionRequestDTO exerciseGameQuestionRequestDTO) {
		boolean isExercise = exerciseGameQuestionRequestDTO.isExercise();
		List<Long> questionIds = exerciseGameQuestionRequestDTO.getQuestionIds();
		long exerciseId = exerciseGameQuestionRequestDTO.getExerciseId();
		long gameId = exerciseGameQuestionRequestDTO.getGameId();

		try {
			// validate questionId, exerciseId or gameId and checkName existed
			for (long questionId : questionIds) {
				Question question = iQuestionRepository.findByIdAndIsDisableFalse(questionId);
				if (question == null) {
					throw new ResourceNotFoundException();
				}
			}

			ExerciseGameQuestion exerciseGameQuestion = null;
			for (long questionId : questionIds) {
				if (isExercise) {
					exerciseGameQuestion = iExerciseGameQuestionRepository
							.findByQuestionIdAndExerciseIdAndIsDisableFalse(questionId, exerciseId);
				} else {
					exerciseGameQuestion = iExerciseGameQuestionRepository
							.findByQuestionIdAndGameIdAndIsDisableFalse(questionId, gameId);
				}
				deleteOneExerciseGameQuestion(exerciseGameQuestion.getId());
			}
		} catch (Exception e) {
			logger.error("Delete list questionIds = " + questionIds.toString() + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "DELETE FAIL!";
		}

		return "DELETE SUCCESS!";
	}

	@Override
	public void deleteOneExerciseGameQuestion(long id) {
		try {
			ExerciseGameQuestion exerciseGameQuestion = iExerciseGameQuestionRepository.findByIdAndIsDisableFalse(id);
			if (exerciseGameQuestion == null) {
				throw new ResourceNotFoundException();
			}
			exerciseGameQuestion.setDisable(true);
			iExerciseGameQuestionRepository.save(exerciseGameQuestion);
		} catch (Exception e) {
			logger.error("Delete exerciseGameQuestion with id = " + id + "! " + e.getMessage());
			throw e;
		}
	}

}
