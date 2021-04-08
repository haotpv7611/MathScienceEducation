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

	@Autowired
	private IExerciseGameQuestionRepository iExerciseGameQuestionRepository;

	@Autowired
	private IQuestionRepository iQuestionRepository;

	@Autowired
	private IExerciseRepository iExerciseRepository;

	@Autowired
	private IGameRepository iGameRepository;

//
//	@Override
//	public List<ExerciseGameQuestionDTO> findAllQuestionByExerciseId1(long exerciseId) {
//		List<ExerciseGameQuestion> list = iExerciseGameQuestionRepository.findByExerciseId(exerciseId);
//		List<ExerciseGameQuestionDTO> listGameQuestionExercise = new ArrayList<>();
//		for (ExerciseGameQuestion exerciseGameQuestion : list) {
//			ExerciseGameQuestionDTO exerciseGameQuestionDTO = modelMapper.map(exerciseGameQuestion,
//					ExerciseGameQuestionDTO.class);
//			listGameQuestionExercise.add(exerciseGameQuestionDTO);
//		}
//		return listGameQuestionExercise;
//	}
//
//	@Override
//	public List<ExerciseGameQuestionDTO> findAllQuestionByGameId1(long gameId) {
//
//		List<ExerciseGameQuestion> list = iExerciseGameQuestionRepository.findByGameId(gameId);
//		List<ExerciseGameQuestionDTO> listGameQuestionExercise = new ArrayList<>();
//		for (ExerciseGameQuestion exerciseGameQuestion : list) {
//			ExerciseGameQuestionDTO exerciseGameQuestionDTO = modelMapper.map(exerciseGameQuestion,
//					ExerciseGameQuestionDTO.class);
//			listGameQuestionExercise.add(exerciseGameQuestionDTO);
//		}
//
//		return listGameQuestionExercise;
//	}

	@Override
	public List<Long> findAllQuestionIdByExerciseId(long exerciseId) {

		return iExerciseGameQuestionRepository.findAllQuestionIdByExerciseId(exerciseId);
	}

	@Override
	public List<Long> findAllQuestionIdByGameId(long gameId) {

		return iExerciseGameQuestionRepository.findAllQuestionIdByGameId(gameId);
	}

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
				Exercise exercise = iExerciseRepository.findByIdAndIsDisableFalse(exerciseId);
				if (exercise == null) {
					throw new ResourceNotFoundException();
				}
				for (long questionId : questionIds) {
					if (!iExerciseGameQuestionRepository
							.findByQuestionIdAndExerciseIdAndIsDisableFalse(questionId, exerciseId).isEmpty()) {
						return "EXISTED";
					}
				}
			} else {
				Game game = iGameRepository.findByIdAndIsDisableFalse(gameId);
				if (game == null) {
					throw new ResourceNotFoundException();
				}
				for (long questionId : questionIds) {
					if (!iExerciseGameQuestionRepository.findByQuestionIdAndGameIdAndIsDisableFalse(questionId, gameId)
							.isEmpty()) {
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
			logger.error(e.getMessage());

			return "ADD FAIL!";
		}

		return "ADD SUCCESS!";
	}

	// done ok
	@Override
	public String deleteExerciseOrGameQuestion(long id) {
		try {
			deleteOneExerciseGameQuestion(id);
		} catch (Exception e) {
			logger.error(e.getMessage());

			return "DELETE FAIL!";
		}

		return "DELETE SUCCESS!";
	}

	// done ok
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
			throw e;
		}
	}

}
