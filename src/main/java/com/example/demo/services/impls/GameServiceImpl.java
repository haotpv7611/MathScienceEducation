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
	public Object findGameById(long id) {
		GameResponseDTO gameResponseDTO = null;
		try {
			Game game = iGameRepository.findByIdAndIsDisableFalse(id);
			if (game == null) {
				throw new ResourceNotFoundException();
			}
			gameResponseDTO = modelMapper.map(game, GameResponseDTO.class);
		} catch (Exception e) {
			logger.error("FIND: gameId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "FIND FAIL!";
		}

		return gameResponseDTO;
	}

	// done
	@Override
	public List<GameResponseDTO> findAllByLessonId(long lessonId) {
		List<GameResponseDTO> gameResponseDTOList = new ArrayList<>();
		try {
			List<Game> gameList = iGameRepository.findByLessonIdAndIsDisableFalse(lessonId);

			if (!gameList.isEmpty()) {
				for (Game game : gameList) {
					List<Long> questionIdList = iExerciseGameQuestionService
							.findAllQuestionIdByGameId(game.getId());
					if (!questionIdList.isEmpty()) {
						GameResponseDTO gameResponseDTO = modelMapper.map(game, GameResponseDTO.class);
						gameResponseDTOList.add(gameResponseDTO);
					}
				}
			}
		} catch (Exception e) {
			logger.error("FIND: all game by lessonId = " + lessonId + "! " + e.getMessage());

			return null;
		}

		return gameResponseDTOList;
	}

	@Override
	public String createGame(GameRequestDTO gameRequestDTO) {
		String gameName = gameRequestDTO.getGameName();
		long lessonId = gameRequestDTO.getLessonId();

		try {
			// validate lessonId and check gameName existed
			Lesson lesson = iLessonRepository.findByIdAndIsDisableFalse(lessonId);
			if (lesson == null) {
				throw new ResourceNotFoundException();
			}
			if (iGameRepository.findByLessonIdAndGameNameAndIsDisableFalse(lessonId, gameName) != null) {
				return "EXISTED";
			}
			Game game = modelMapper.map(gameRequestDTO, Game.class);
			iGameRepository.save(game);
		} catch (Exception e) {
			logger.error("CREATE: gameName = " + gameName + " in lessonId =  " + lessonId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CREATE FAIL!";
		}

		return "CREATE SUCCESS!";
	}

	@Override
	public String updateGame(long id, GameRequestDTO gameRequestDTO) {
		String gameNameDTO = gameRequestDTO.getGameName();
		long lessonId = gameRequestDTO.getLessonId();

		try {
			// validate gameId, lessonId and check gameName existed
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
		} catch (Exception e) {
			logger.error("UPDATE: gameId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "UPDATE FAIL!";
		}

		return "UPDATE SUCCESS!";
	}

	@Override
	public String deleteGame(long id) {
		try {
			deleteOneGame(id);
		} catch (Exception e) {
			logger.error("DELETE: gameId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "DELETE FAIL!";
		}

		return "DELETE SUCESS!";
	}

	@Override
	@Transactional
	public void deleteOneGame(long id) {
		try {
			// validate gameId
			Game game = iGameRepository.findByIdAndIsDisableFalse(id);
			if (game == null) {
				throw new ResourceNotFoundException();
			}

			List<Long> gameQuestionIdList = iExerciseGameQuestionService.findAllQuestionIdByGameId(id);
			if (!gameQuestionIdList.isEmpty()) {
				for (long gameQuestionId : gameQuestionIdList) {
					iExerciseGameQuestionService.deleteOneExerciseGameQuestion(gameQuestionId);
				}
			}
			game.setDisable(true);
			iGameRepository.save(game);
		} catch (Exception e) {
			throw e;
		}
	}

}
