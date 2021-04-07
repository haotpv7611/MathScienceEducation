package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.GameRequestDTO;
import com.example.demo.dtos.GameResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Game;
import com.example.demo.models.Lesson;
import com.example.demo.repositories.IGameRepository;
import com.example.demo.repositories.ILessonRepository;
import com.example.demo.services.IExerciseGameQuestionService;
import com.example.demo.services.IGameService;

@Service
public class GameServiceImpl implements IGameService {
	Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

	@Autowired
	IGameRepository iGameRepository;

	@Autowired
	private ILessonRepository iLessonRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IExerciseGameQuestionService iExerciseGameQuestionService;

	// done
	@Override
	public List<GameResponseDTO> findAllByLessonId(long lessonId) {
		Lesson lesson = iLessonRepository.findByIdAndIsDisableFalse(lessonId);
		if (lesson == null) {
			throw new ResourceNotFoundException();
		}

		List<Game> gameList = iGameRepository.findByLessonIdAndIsDisableFalse(lessonId);
		List<GameResponseDTO> gameResponseDTOList = new ArrayList<>();
		if (!gameList.isEmpty()) {
			for (Game game : gameList) {
				GameResponseDTO gameResponseDTO = modelMapper.map(game, GameResponseDTO.class);
				gameResponseDTOList.add(gameResponseDTO);
			}
		}

		return gameResponseDTOList;
	}

	// done
	@Override
	public GameResponseDTO findGameById(long id) {

//		try {
		Game game = iGameRepository.findByIdAndIsDisableFalse(id);
		if (game == null) {
			throw new ResourceNotFoundException();
		}
		GameResponseDTO gameResponseDTO = modelMapper.map(game, GameResponseDTO.class);

		return gameResponseDTO;

//		} catch (ResourceNotFoundException ex) {
//			logger.warn(ex.getClass().getName() + ex.getMessage() + "\n");
//			throw new ResourceNotFoundException();

//		}catch (Exception e) {
//			if (e instanceof ResourceNotFoundException){
//				logger.warn("ResourceNotFound " + e.getClass().getName() + ": " + e.getMessage() + "\n");
//				
//				throw new ResourceNotFoundException();
//			}
//			System.out.println(e.getMessage());
//			logger.warn(e.getMessage() + "\n");
//			return null;
//		}

	}

	@Override
	public String updateGame(long id, GameRequestDTO gameRequestDTO) {
		String gameNameDTO = gameRequestDTO.getGameName();
		long lessonId = gameRequestDTO.getLessonId();
		Lesson lesson = iLessonRepository.findByIdAndIsDisableFalse(lessonId);
		if (lesson == null) {
			throw new ResourceNotFoundException();
		}
		Game game = iGameRepository.findByIdAndIsDisableFalse(id);
		if (game == null) {
			throw new ResourceNotFoundException();
		}

		if (!game.getGameName().equalsIgnoreCase(gameNameDTO)) {
			if (iGameRepository.findByLessonIdAndGameNameAndIsDisableFalse(lessonId, gameNameDTO) != null) {
				return "EXISTED";
			}
		}

		game.setGameName(gameNameDTO);
		game.setDescription(gameRequestDTO.getDescription());
		iGameRepository.save(game);

		return "UPDATE SUCCESS!";
	}

	@Override
	public String createGame(GameRequestDTO gameRequestDTO) {
		String gameName = gameRequestDTO.getGameName();
		long lessonId = gameRequestDTO.getLessonId();
		Lesson lesson = iLessonRepository.findByIdAndIsDisableFalse(lessonId);
		if (lesson == null) {
			throw new ResourceNotFoundException();
		}
		if (iGameRepository.findByLessonIdAndGameNameAndIsDisableFalse(lessonId, gameName) != null) {
			return "EXISTED";
		}
		Game game = modelMapper.map(gameRequestDTO, Game.class);
		iGameRepository.save(game);

		return "CREATE SUCCESS!";
	}

	@Override
	public String deleteGame(List<Long> ids) {
		for (Long id : ids) {
			deleteOneGame(id);
		}

//		// xoa bang giua
//		List<ExerciseGameQuestion> exerciseGameQuestionLists = iExerciseGameQuestionRepository.findByGameId(id);
//		if (exerciseGameQuestionLists != null) {
//			for (ExerciseGameQuestion exerciseGameQuestion : exerciseGameQuestionLists) {
//				iExerciseGameQuestionService.deleteQuestionFromExercise(exerciseGameQuestion.getId());
//			}
//		}

		return "DELETE SUCESS!";
	}
	
	@Override
	@Transactional
	public void deleteOneGame(long id) {
		Game game = iGameRepository.findByIdAndIsDisableFalse(id);
		if (game == null) {
			throw new ResourceNotFoundException();
		}

		List<Long> gameQuestionIdList = iExerciseGameQuestionService.findAllQuestionIdByGameId(id);
		if (!gameQuestionIdList.isEmpty()) {
			for (Long gameQuestionId : gameQuestionIdList) {
				iExerciseGameQuestionService.deleteExerciseGameQuestion(gameQuestionId);
			}				
		}
		game.setDisable(true);
		iGameRepository.save(game);
	}

}
