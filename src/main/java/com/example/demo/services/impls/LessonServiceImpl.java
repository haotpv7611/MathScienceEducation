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

import com.example.demo.dtos.LessonResponseDTO;
import com.example.demo.dtos.IdAndStatusDTO;
import com.example.demo.dtos.LessonRequestDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Exercise;
import com.example.demo.models.Game;
import com.example.demo.models.Lesson;
import com.example.demo.models.Unit;
import com.example.demo.repositories.IExerciseRepository;
import com.example.demo.repositories.IGameRepository;
import com.example.demo.repositories.ILessonRepository;
import com.example.demo.repositories.IUnitRepository;
import com.example.demo.services.IExerciseService;
import com.example.demo.services.IGameService;
import com.example.demo.services.ILessonService;

@Service
public class LessonServiceImpl implements ILessonService {
	Logger logger = LoggerFactory.getLogger(LessonServiceImpl.class);
	private final String DELETED_STATUS = "DELETED";

	@Autowired
	private ILessonRepository iLessonRepository;

	@Autowired
	private IUnitRepository iUnitRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IExerciseRepository iExerciseRepository;

	@Autowired
	private IExerciseService iExerciseService;

	@Autowired
	private IGameRepository iGameRepository;

	@Autowired
	private IGameService iGameService;

	// done
	@Override
	public Object findById(long id) {
		LessonResponseDTO lessonResponseDTO = null;
		try {
			Lesson lesson = iLessonRepository.findByIdAndIsDisableFalse(id);
			if (lesson == null) {
				throw new ResourceNotFoundException();
			}
			lessonResponseDTO = modelMapper.map(lesson, LessonResponseDTO.class);
		} catch (Exception e) {
			logger.error("FIND: lessonId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "FIND FAIL!";
		}

		return lessonResponseDTO;
	}

	// done
	@Override
	public List<LessonResponseDTO> findByUnitIdOrderByLessonNameAsc(long unitId) {
		List<LessonResponseDTO> lessonResponseDTOList = new ArrayList<>();
		try {
			List<Lesson> lessonList = iLessonRepository.findByUnitIdAndIsDisableFalseOrderByLessonNameAsc(unitId);
			if (!lessonList.isEmpty()) {
				for (Lesson lesson : lessonList) {
					LessonResponseDTO lessonResponseDTO = modelMapper.map(lesson, LessonResponseDTO.class);
					lessonResponseDTOList.add(lessonResponseDTO);
				}
			}
		} catch (Exception e) {
			logger.error("FIND: all lesson by unitId = " + unitId + "! " + e.getMessage());

			return null;
		}

		return lessonResponseDTOList;
	}

	// done
	@Override
	public Map<String, List<LessonResponseDTO>> findByUnitIdStudentView(long unitId) {
		Map<String, List<LessonResponseDTO>> lessonMap = new HashMap<>();		
		try {
			List<Lesson> lessonList = iLessonRepository.findByUnitIdAndIsDisableFalseOrderByLessonNameAsc(unitId);
			if (!lessonList.isEmpty()) {
				List<LessonResponseDTO> lessonResponseDTOList = new ArrayList<>();
				for (Lesson lesson : lessonList) {
					LessonResponseDTO lessonResponseDTO = modelMapper.map(lesson, LessonResponseDTO.class);
					lessonResponseDTOList.add(lessonResponseDTO);
				}
				
				Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);
				String unitName = "";
				if (unit != null) {
					unitName = "Unit " + unit.getUnitName();
				}
				lessonMap.put(unitName, lessonResponseDTOList);
			}
		} catch (Exception e) {
			logger.error("FIND: all lesson student view by unitId = " + unitId + "! " + e.getMessage());

			return null;
		}

		return lessonMap;
	}

	@Override
	public Map<Long, Integer> findAllLesson() {
		Map<Long, Integer> lessonMap = new HashMap<>();
		try {

			List<Lesson> lessonList = iLessonRepository.findByIsDisableFalse();
			for (Lesson lesson : lessonList) {
				lessonMap.put(lesson.getId(), lesson.getLessonName());
			}
		} catch (Exception e) {
			logger.error("Find all lessons! " + e.getMessage());
		}

		return lessonMap;
	}

	// done
	@Override
	public String createLesson(LessonRequestDTO lessonRequestDTO) {
		long unitId = lessonRequestDTO.getUnitId();
		int lessonName = lessonRequestDTO.getLessonName();

		try {
			// validate unitId and check lessonName existed
			Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);
			if (unit == null) {
				throw new ResourceNotFoundException();
			}
			if (iLessonRepository.findByUnitIdAndLessonNameAndIsDisableFalse(unitId, lessonName) != null) {

				return "EXISTED";
			}

			Lesson lesson = modelMapper.map(lessonRequestDTO, Lesson.class);
			lesson.setDisable(false);
			iLessonRepository.save(lesson);
		} catch (Exception e) {
			logger.error("CREATE: lessonName = " + lessonName + " in unitId =  " + unitId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CREATE FAIL!";
		}

		return "CREATE SUCCESS!";
	}

	// done
	@Override
	public String updateLesson(long id, LessonRequestDTO lessonRequestDTO) {
		int lessonName = lessonRequestDTO.getLessonName();
		String lessonUrl = lessonRequestDTO.getLessonUrl();

		try {
			// validate lessonId, unitId and check lessonName existed
			Lesson lesson = iLessonRepository.findByIdAndIsDisableFalse(id);
			if (lesson == null) {
				throw new ResourceNotFoundException();
			}
			long unitId = lessonRequestDTO.getUnitId();
			if (lesson.getLessonName() != lessonName) {
				if (iLessonRepository.findByUnitIdAndLessonNameAndIsDisableFalse(unitId, lessonName) != null) {

					return "EXISTED";
				}
			}

			lesson.setLessonName(lessonName);
			lesson.setLessonUrl(lessonUrl);
			iLessonRepository.save(lesson);
		} catch (Exception e) {
			logger.error("UPDATE: lessonId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "UPDATE FAIL!";
		}

		return "UPDATE SUCCESS!";

	}

	@Override
	@Transactional
	public String deleteOneLesson(long id) {
		try {
			// validate lessonId
			Lesson lesson = iLessonRepository.findByIdAndIsDisableFalse(id);
			if (lesson == null) {
				throw new ResourceNotFoundException();
			}

			List<Exercise> exerciseList = iExerciseRepository.findByLessonIdAndStatusNot(id, DELETED_STATUS);
			if (!exerciseList.isEmpty()) {
				for (Exercise exercise : exerciseList) {
					IdAndStatusDTO idAndStatusDTO = new IdAndStatusDTO(exercise.getId(), DELETED_STATUS);
					String response = iExerciseService.changeOneExerciseStatus(idAndStatusDTO);
					if (!response.equalsIgnoreCase("OK")) {

						return response;
					}
				}
			}
			List<Game> gameList = iGameRepository.findByLessonIdAndStatusNot(id, DELETED_STATUS);
			if (!gameList.isEmpty()) {
				for (Game game : gameList) {
					IdAndStatusDTO idAndStatusDTO = new IdAndStatusDTO(game.getId(), DELETED_STATUS);
					String response = iGameService.changeOneGameStatus(idAndStatusDTO);
					if (!response.equalsIgnoreCase("OK")) {

						return response;
					}
				}
			}

			lesson.setDisable(true);
			iLessonRepository.save(lesson);
		} catch (Exception e) {
			logger.error("DELETE: lessonId = " + id + "! " + e.getMessage());
			throw e;
		}
		
		return "OK";
	}

}
