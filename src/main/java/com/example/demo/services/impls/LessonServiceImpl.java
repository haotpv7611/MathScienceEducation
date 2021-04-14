package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

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
	ILessonRepository iLessonRepository;

	@Autowired
	IUnitRepository iUnitRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	IExerciseRepository iExerciseRepository;

	@Autowired
	IExerciseService iExerciseService;

	@Autowired
	IGameRepository iGameRepository;

	@Autowired
	IGameService iGameService;

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
				Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);
				int unitName = 0;
				if (unit != null) {
					unitName = unit.getUnitName();
				}

				for (Lesson lesson : lessonList) {
					LessonResponseDTO lessonResponseDTO = modelMapper.map(lesson, LessonResponseDTO.class);
					lessonResponseDTO.setUnitName(unitName);
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
			Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);
			if (unit == null) {
				throw new ResourceNotFoundException();
			}
			if (lesson.getLessonName() != lessonName) {
				if (iLessonRepository.findByUnitIdAndLessonNameAndIsDisableFalse(unitId,
						lessonName) != null) {

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
	public String deleteLesson(long id) {
		try {
			deleteOneLesson(id);
		} catch (Exception e) {
			logger.error("DELETE: lessonId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "DELETE FAIL!";
		}

		return "DELETE SUCCESS!";
	}

	@Override
	@Transactional
	public void deleteOneLesson(long id) {
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
					iExerciseService.changeStatusOne(idAndStatusDTO);
				}
			}
			List<Game> gameList = iGameRepository.findByLessonIdAndStatusNot(id, DELETED_STATUS);
			if (!gameList.isEmpty()) {
				for (Game game : gameList) {
					IdAndStatusDTO idAndStatusDTO = new IdAndStatusDTO(game.getId(), DELETED_STATUS);
					iGameService.changeStatusOne(idAndStatusDTO);
				}
			}

			lesson.setDisable(true);
			iLessonRepository.save(lesson);
		} catch (Exception e) {
			throw e;
		}
	}

}
