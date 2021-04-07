package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.ExerciseGameQuestionDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.ExerciseGameQuestion;
import com.example.demo.repositories.IExerciseGameQuestionRepository;
import com.example.demo.services.IExerciseGameQuestionService;

@Service
public class ExerciseGameQuestionServiceImpl implements IExerciseGameQuestionService {

	@Autowired
	private IExerciseGameQuestionRepository iExerciseGameQuestionRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public List<ExerciseGameQuestionDTO> findAllQuestionByExerciseId1(long exerciseId) {
		List<ExerciseGameQuestion> list = iExerciseGameQuestionRepository.findByExerciseId(exerciseId);
		List<ExerciseGameQuestionDTO> listGameQuestionExercise = new ArrayList<>();
		for (ExerciseGameQuestion exerciseGameQuestion : list) {
			ExerciseGameQuestionDTO exerciseGameQuestionDTO = modelMapper.map(exerciseGameQuestion,
					ExerciseGameQuestionDTO.class);
			listGameQuestionExercise.add(exerciseGameQuestionDTO);
		}
		return listGameQuestionExercise;
	}

	@Override
	public List<ExerciseGameQuestionDTO> findAllQuestionByGameId1(long gameId) {

		List<ExerciseGameQuestion> list = iExerciseGameQuestionRepository.findByGameId(gameId);
		List<ExerciseGameQuestionDTO> listGameQuestionExercise = new ArrayList<>();
		for (ExerciseGameQuestion exerciseGameQuestion : list) {
			ExerciseGameQuestionDTO exerciseGameQuestionDTO = modelMapper.map(exerciseGameQuestion,
					ExerciseGameQuestionDTO.class);
			listGameQuestionExercise.add(exerciseGameQuestionDTO);
		}

		return listGameQuestionExercise;
	}

	@Override
	public String addQuestionToExercise(ExerciseGameQuestionDTO exerciseGameQuestionDTO) {
		// get List Question of this exercise is existed !

		if (exerciseGameQuestionDTO.getExerciseId() != 0 && exerciseGameQuestionDTO.getExerciseId() != 1) {

			List<ExerciseGameQuestionDTO> listQuestionExercise = findAllQuestionByExerciseId1(
					exerciseGameQuestionDTO.getExerciseId());

			for (ExerciseGameQuestionDTO exerciseGameQuestion1 : listQuestionExercise) {

				if (exerciseGameQuestion1.getQuestionId() == exerciseGameQuestionDTO.getQuestionId()) {
					return "This question is existed !";
				}
			}
			ExerciseGameQuestion exerciseGameQuestion = modelMapper.map(exerciseGameQuestionDTO,
					ExerciseGameQuestion.class);
			exerciseGameQuestion.setGameId(1);
			exerciseGameQuestion.setDisable(false);
			exerciseGameQuestion.setGQuestion(false);
			iExerciseGameQuestionRepository.save(exerciseGameQuestion);
		}

		if (exerciseGameQuestionDTO.getGameId() != 0 && exerciseGameQuestionDTO.getGameId() != 1) {
			List<ExerciseGameQuestionDTO> listQuestionGame = findAllQuestionByGameId1(
					exerciseGameQuestionDTO.getGameId());
			for (ExerciseGameQuestionDTO exerciseGameQuestion1 : listQuestionGame) {
				if (exerciseGameQuestion1.getQuestionId() == exerciseGameQuestionDTO.getQuestionId()) {
					return "This question is existed !";
				}
			}
			ExerciseGameQuestion exerciseGameQuestion = modelMapper.map(exerciseGameQuestionDTO,
					ExerciseGameQuestion.class);
			exerciseGameQuestion.setExerciseId(1);
			exerciseGameQuestion.setGQuestion(true);
			exerciseGameQuestion.setDisable(false);
			iExerciseGameQuestionRepository.save(exerciseGameQuestion);
		}
		return "CREATE SUCCESS !";
	}

	@Override
	public String deleteQuestionFromExercise(long id) {
		ExerciseGameQuestion exerciseGameQuestion = iExerciseGameQuestionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
		if (exerciseGameQuestion == null) {
			throw new ResourceNotFoundException();
		}
		exerciseGameQuestion.setDisable(true);
		return "DELETE SUCCESS !";
	}

	@Override
	public List<Long> findAllQuestionIdByExerciseId(long exerciseId) {

		return iExerciseGameQuestionRepository.findAllQuestionIdByExerciseId(exerciseId);
	}

	@Override
	public List<Long> findAllQuestionIdByGameId(long gameId) {

		return iExerciseGameQuestionRepository.findAllQuestionIdByGameId(gameId);
	}

}
