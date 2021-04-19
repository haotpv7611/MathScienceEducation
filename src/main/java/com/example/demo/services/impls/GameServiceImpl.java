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

import com.example.demo.dtos.GameRequestDTO;
import com.example.demo.dtos.GameResponseDTO;
import com.example.demo.dtos.IdAndStatusDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.ExerciseGameQuestion;
import com.example.demo.models.Game;
import com.example.demo.models.Lesson;
import com.example.demo.repositories.IExerciseGameQuestionRepository;
import com.example.demo.repositories.IGameRepository;
import com.example.demo.repositories.ILessonRepository;
import com.example.demo.services.IExerciseGameQuestionService;
import com.example.demo.services.IGameService;

@Service
public class GameServiceImpl implements IGameService {
	Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);
	private final String INACTIVE_STATUS = "INACTIVE";
	private final String DELETED_STATUS = "DELETED";

	@Autowired
	IGameRepository iGameRepository;

	@Autowired
	private ILessonRepository iLessonRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IExerciseGameQuestionService iExerciseGameQuestionService;

	@Autowired
	private IExerciseGameQuestionRepository iExerciseGameQuestionRepository;

	// done
	@Override
	public Object findGameById(long id) {
		GameResponseDTO gameResponseDTO = null;
		try {
			Game game = iGameRepository.findByIdAndStatusNot(id, DELETED_STATUS);
			if (game == null) {
				throw new ResourceNotFoundException();
			}
			gameResponseDTO = modelMapper.map(game, GameResponseDTO.class);
		} catch (Exception e) {
			logger.error("Find game with id = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "FIND FAIL!";
		}

		return gameResponseDTO;
	}

	// should modify find by lesson --> admin or student view
	@Override
	public List<GameResponseDTO> findAllByLessonId(long lessonId) {
		List<GameResponseDTO> gameResponseDTOList = new ArrayList<>();
		try {
			List<Game> gameList = iGameRepository.findByLessonIdAndStatusNot(lessonId, DELETED_STATUS);
			if (!gameList.isEmpty()) {
				for (Game game : gameList) {
					GameResponseDTO gameResponseDTO = modelMapper.map(game, GameResponseDTO.class);
					gameResponseDTOList.add(gameResponseDTO);
				}
			}
		} catch (Exception e) {
			logger.error("Find all games by lessonId = " + lessonId + "! " + e.getMessage());

			return null;
		}

		return gameResponseDTOList;
	}

	@Override
	public List<GameResponseDTO> findAllByLessonIdStudentView(long lessonId) {
		List<GameResponseDTO> gameResponseDTOList = new ArrayList<>();
		try {
			List<Game> gameList = iGameRepository.findByLessonIdAndStatusNot(lessonId, DELETED_STATUS);
			if (!gameList.isEmpty()) {
				for (Game game : gameList) {
					List<ExerciseGameQuestion> exerciseGameQuestionList = iExerciseGameQuestionRepository
							.findByGameIdAndIsDisableFalse(game.getId());
					if (!exerciseGameQuestionList.isEmpty()) {
						GameResponseDTO gameResponseDTO = modelMapper.map(game, GameResponseDTO.class);
						gameResponseDTOList.add(gameResponseDTO);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Find all games student view by lessonId = " + lessonId + "! " + e.getMessage());

			return null;
		}

		return gameResponseDTOList;
	}

	@Override
	public Map<Long, Integer> findAllGame() {
		Map<Long, Integer> gameMap = new HashMap<>();
		try {
			List<Game> gameList = iGameRepository.findByStatusNot(DELETED_STATUS);
			for (Game game : gameList) {
				gameMap.put(game.getId(), game.getGameName());
			}
		} catch (Exception e) {
			logger.error("Find all games! " + e.getMessage());
		}

		return gameMap;
	}

	@Override
	public String createGame(GameRequestDTO gameRequestDTO) {
		int gameName = gameRequestDTO.getGameName();
		long lessonId = gameRequestDTO.getLessonId();

		try {
			// validate lessonId and check gameName existed
			Lesson lesson = iLessonRepository.findByIdAndIsDisableFalse(lessonId);
			if (lesson == null) {
				throw new ResourceNotFoundException();
			}
			if (iGameRepository.findByLessonIdAndGameNameAndStatusNot(lessonId, gameName, DELETED_STATUS) != null) {

				return "EXISTED";
			}
			Game game = modelMapper.map(gameRequestDTO, Game.class);
			game.setStatus(INACTIVE_STATUS);
			iGameRepository.save(game);
		} catch (Exception e) {
			logger.error("Create game with name = " + gameName + " in lessonId =  " + lessonId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CREATE FAIL!";
		}

		return "CREATE SUCCESS!";
	}

	@Override
	public String updateGame(long id, GameRequestDTO gameRequestDTO) {
		int gameName = gameRequestDTO.getGameName();
		long lessonId = gameRequestDTO.getLessonId();

		try {
			// validate gameId and check gameName existed
			Game game = iGameRepository.findByIdAndStatusNot(id, DELETED_STATUS);
			if (game == null) {
				throw new ResourceNotFoundException();
			}
			if (game.getGameName() != gameName) {
				if (iGameRepository.findByLessonIdAndGameNameAndStatusNot(lessonId, gameName, DELETED_STATUS) != null) {

					return "EXISTED";
				}
			}

			game.setGameName(gameName);
			game.setDescription(gameRequestDTO.getDescription());
			iGameRepository.save(game);
		} catch (Exception e) {
			logger.error("Update game with id = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "UPDATE FAIL!";
		}

		return "UPDATE SUCCESS!";
	}

	@Override
	@Transactional
	public void changeOneGameStatus(IdAndStatusDTO idAndStatusDTO) {
		long id = idAndStatusDTO.getId();
		String status = idAndStatusDTO.getStatus();
		try {
			// validate gameId
			Game game = iGameRepository.findByIdAndStatusNot(id, DELETED_STATUS);
			if (game == null) {
				throw new ResourceNotFoundException();
			}

			// if delete must delete question in exercise first
			if (status.equals(DELETED_STATUS)) {
				List<ExerciseGameQuestion> exerciseGameQuestionList = iExerciseGameQuestionRepository
						.findByGameIdAndIsDisableFalse(id);
				if (!exerciseGameQuestionList.isEmpty()) {
					for (ExerciseGameQuestion exerciseGameQuestion : exerciseGameQuestionList) {
						iExerciseGameQuestionService.deleteOneExerciseGameQuestion(exerciseGameQuestion.getId());
					}
				}
			}
			game.setStatus(status);
			iGameRepository.save(game);
		} catch (Exception e) {
			throw e;
		}
	}

}
