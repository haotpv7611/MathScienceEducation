package com.example.demo.services.impls;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.ExerciseGameQuestionDTO;
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

	@Autowired
	private IExerciseGameQuestionRepository iExerciseGameQuestionRepository;

	@Autowired
	private IQuestionRepository iQuestionRepository;

	@Autowired
	private IExerciseRepository iExerciseRepository;

	@Autowired
	private IGameRepository iGameRepository;

	@Autowired
	private ModelMapper modelMapper;
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
	public String addExerciseOrGameQuestion(ExerciseGameQuestionDTO exerciseGameQuestionDTO) {
		boolean isExercise = exerciseGameQuestionDTO.isExercise();
		long questionId = exerciseGameQuestionDTO.getQuestionId();
		long exerciseId = exerciseGameQuestionDTO.getExerciseId();
		long gameId = exerciseGameQuestionDTO.getGameId();

		// validate data input
		Question question = iQuestionRepository.findByIdAndIsDisableFalse(questionId);
		if (question == null) {
			throw new ResourceNotFoundException();
		}

		if (isExercise) {
			Exercise exercise = iExerciseRepository.findByIdAndIsDisableFalse(exerciseId);
			if (exercise == null) {
				throw new ResourceNotFoundException();
			}
			if (!iExerciseGameQuestionRepository.findByQuestionIdAndExerciseIdAndIsDisableFalse(questionId, exerciseId)
					.isEmpty()) {
				return "EXISTED";
			}
		} else {
			Game game = iGameRepository.findByIdAndIsDisableFalse(gameId);
			if (game == null) {
				throw new ResourceNotFoundException();
			}
			if (!iExerciseGameQuestionRepository.findByQuestionIdAndGameIdAndIsDisableFalse(questionId, gameId)
					.isEmpty()) {
				return "EXISTED";
			}
		}

		ExerciseGameQuestion exerciseGameQuestion = null;
		if (isExercise) {
			exerciseGameQuestion = new ExerciseGameQuestion(questionId, exerciseId, 0, isExercise, false);
		} else {
			exerciseGameQuestion = new ExerciseGameQuestion(questionId, 0, gameId, isExercise, false);
		}
		iExerciseGameQuestionRepository.save(exerciseGameQuestion);

		return "CREATE SUCCESS!";
	}

//	@Override
//	public String deleteExerciseOrGameQuestion(List<Long> ids) {
//		for (Long id : ids) {
//			deleteExerciseGameQuestion(id);
//		}
//
//		return "DELETE SUCCESS!";
//	}

	@Override
	public List<Long> findAllQuestionIdByExerciseId(long exerciseId) {

		return iExerciseGameQuestionRepository.findAllQuestionIdByExerciseId(exerciseId);
	}

	@Override
	public List<Long> findAllQuestionIdByGameId(long gameId) {

		return iExerciseGameQuestionRepository.findAllQuestionIdByGameId(gameId);
	}

	@Override
	public void deleteExerciseGameQuestion(long id) {
		ExerciseGameQuestion exerciseGameQuestion = iExerciseGameQuestionRepository.findByIdAndIsDisableFalse(id);
		if (exerciseGameQuestion == null) {
			throw new ResourceNotFoundException();
		}
		exerciseGameQuestion.setDisable(true);
		iExerciseGameQuestionRepository.save(exerciseGameQuestion);
	}

}
