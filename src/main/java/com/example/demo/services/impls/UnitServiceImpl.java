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

import com.example.demo.dtos.ProgressTestResponseDTO;
import com.example.demo.dtos.UnitRequestDTO;
import com.example.demo.dtos.UnitResponseDTO;
import com.example.demo.dtos.UnitViewDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Exercise;
import com.example.demo.models.Lesson;
import com.example.demo.models.ProgressTest;
import com.example.demo.models.Question;
import com.example.demo.models.Subject;
import com.example.demo.models.Unit;
import com.example.demo.repositories.IExerciseRepository;
import com.example.demo.repositories.ILessonRepository;
import com.example.demo.repositories.IProgressTestRepository;
import com.example.demo.repositories.IQuestionRepository;
import com.example.demo.repositories.ISubjectRepository;
import com.example.demo.repositories.IUnitRepository;
import com.example.demo.services.IExerciseTakenService;
import com.example.demo.services.ILessonService;
import com.example.demo.services.IProgressTestService;
import com.example.demo.services.IQuestionService;
import com.example.demo.services.IUnitService;

@Service
public class UnitServiceImpl implements IUnitService {
	Logger logger = LoggerFactory.getLogger(UnitServiceImpl.class);
	private final String DELETED_STATUS = "DELETED";

	@Autowired
	private IUnitRepository iUnitRepository;

	@Autowired
	private ILessonRepository iLessonRepository;

	@Autowired
	IQuestionRepository iQuestionRepository;
	@Autowired

	ILessonService iLessonService;
	@Autowired

	IQuestionService iQuestionService;
	@Autowired
	private ISubjectRepository iSubjectRepository;

	@Autowired
	private IProgressTestService iProgressTestService;

	@Autowired
	private IProgressTestRepository iProgressTestRepository;

	@Autowired
	private IExerciseRepository iExerciseRepository;

	@Autowired
	private IExerciseTakenService iExerciseTakenService;

	@Autowired
	private ModelMapper modelMapper;

	// done
	@Override
	public Object findById(long id) {
		UnitResponseDTO unitResponseDTO = null;
		try {
			Unit unit = iUnitRepository.findByIdAndIsDisableFalse(id);
			if (unit == null) {
				throw new ResourceNotFoundException();
			}
			unitResponseDTO = modelMapper.map(unit, UnitResponseDTO.class);
		} catch (Exception e) {
			logger.error("FIND: unitId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "FIND FAIL!";
		}

		return unitResponseDTO;
	}

	// done
	@Override
	public List<UnitResponseDTO> findBySubjectId(long subjectId) {
		List<UnitResponseDTO> unitDTOList = new ArrayList<>();
		try {
			// find all active entities and have subjectId = ?, sort acscending by unitName
			List<Unit> unitList = iUnitRepository.findBySubjectIdAndIsDisableFalseOrderByUnitNameAsc(subjectId);
			if (!unitList.isEmpty()) {
				for (Unit unit : unitList) {
					UnitResponseDTO unitResponseDTO = modelMapper.map(unit, UnitResponseDTO.class);
					unitDTOList.add(unitResponseDTO);
				}
			}
		} catch (Exception e) {
			logger.error("FIND: all unit by subjectId = " + subjectId + "! " + e.getMessage());

			return null;
		}

		return unitDTOList;
	}

	@Override
//	public Map<Long, Integer> findAllUnitAfterIdsBySubjectId(long subjectId) {
	public List<UnitResponseDTO> findAllUnitAfterIdsBySubjectId(long subjectId) {
		List<UnitResponseDTO> unitResponseDTOList = new ArrayList<>();

		try {
			// find all unit by gradeID
			List<Unit> unitList = iUnitRepository.findBySubjectIdAndIsDisableFalseOrderByUnitNameAsc(subjectId);
			List<ProgressTest> progressTestList = iProgressTestRepository.findBySubjectIdAndIsDisableFalse(subjectId);

			if (!unitList.isEmpty()) {
				for (int i = 0; i < unitList.size(); i++) {
					if (!progressTestList.isEmpty()) {
						for (ProgressTest progressTest : progressTestList) {
							if (unitList.get(i).getId() == progressTest.getUnitAfterId()) {
								unitList.remove(unitList.get(i));
							}
						}
					}
				}
			}
			if (!unitList.isEmpty()) {
				for (Unit unit : unitList) {
					UnitResponseDTO unitResponseDTO = modelMapper.map(unit, UnitResponseDTO.class);
					unitResponseDTOList.add(unitResponseDTO);
				}
			}
		} catch (Exception e) {
			logger.error("FIND: all unitAfter by subjectId = " + subjectId + "! " + e.getMessage());

			return null;
		}

		return unitResponseDTOList;
	}

	// not done
	@Override
	public List<UnitViewDTO> showUnitViewBySubjectId(long subjectId, long accountId) {
		// 1. get list unitDTO and progressTestDTO by subjectId
		List<UnitResponseDTO> unitDTOList = findBySubjectId(subjectId);
		List<ProgressTestResponseDTO> progressTestDTOList = iProgressTestService.findBySubjectId(subjectId);

		List<UnitViewDTO> unitViewDTOList = new ArrayList<>();

		if (!unitDTOList.isEmpty()) {
			Subject subject = iSubjectRepository.findByIdAndIsDisableFalse(subjectId);
			String subjectName = "";
			if (subject != null) {
				subjectName = subject.getSubjectName();
			}

			List<UnitResponseDTO> unitDTOListSplit = new ArrayList<>();

			// 2. split list unitDTO into small lists by progresTest unitAfterId
			// 3. group small list unitDTO and break progressTestDTO into unitViewDTO
			// 4. add all unitViewDTO into unitViewDTOList
			for (int i = 0; i < unitDTOList.size(); i++) {
				unitDTOListSplit.add(unitDTOList.get(i));

				for (int j = 0; j < progressTestDTOList.size(); j++) {

					if (progressTestDTOList.get(j).getUnitAfterId() == unitDTOList.get(i).getId()) {
						ProgressTestResponseDTO progressTestResponseDTO = progressTestDTOList.get(j);
						List<Exercise> exerciseList = iExerciseRepository
								.findByProgressTestIdAndStatusNot(progressTestResponseDTO.getId(), DELETED_STATUS);
						int countNotDone = 0;
						if (!exerciseList.isEmpty()) {

							for (Exercise exercise : exerciseList) {
								countNotDone += iExerciseTakenService.countExerciseNotDone(accountId, exercise.getId());
							}
						}
						progressTestResponseDTO.setDone(true);
						if (countNotDone != 0) {
							progressTestResponseDTO.setDone(false);
						}

						UnitViewDTO unitViewDTO = new UnitViewDTO(subjectName, unitDTOListSplit,
								progressTestResponseDTO);
						unitViewDTOList.add(unitViewDTO);
						progressTestDTOList.remove(j);
						unitDTOListSplit = new ArrayList<>();

						break;
					}
				}
			}

			// 5. if list unitDTO split more than progressTestDTO, set unitViewDTO with
			// progressTestDTO is null
			if (!unitDTOListSplit.isEmpty()) {
				UnitViewDTO unitViewDTO = new UnitViewDTO(subjectName, unitDTOListSplit, null);
				unitViewDTOList.add(unitViewDTO);
			}
		}

		return unitViewDTOList;
	}

	@Override
	public Map<Long, Integer> findAllUnit() {
		Map<Long, Integer> unitMap = new HashMap<>();
		try {
			List<Unit> unitList = iUnitRepository.findByIsDisableFalse();
			for (Unit unit : unitList) {
				unitMap.put(unit.getId(), unit.getUnitName());
			}
		} catch (Exception e) {
			logger.error("Find all unit! " + e.getMessage());
		}

		return unitMap;
	}

	// done
	@Override
	public String createUnit(UnitRequestDTO unitRequestDTO) {
		long subjectId = unitRequestDTO.getSubjectId();
		int unitName = unitRequestDTO.getUnitName();
		try {

			// validate subjectId and check unitName existed
			Subject subject = iSubjectRepository.findByIdAndIsDisableFalse(subjectId);
			if (subject == null) {
				throw new ResourceNotFoundException();
			}
			if (iUnitRepository.findBySubjectIdAndUnitNameAndIsDisableFalse(subjectId, unitName) != null) {

				return "EXISTED";
			}

			// save data and return
			Unit unit = modelMapper.map(unitRequestDTO, Unit.class);
			unit.setDisable(false);
			iUnitRepository.save(unit);
		} catch (Exception e) {
			logger.error("CREATE: unitName = " + unitName + " in subjectId =  " + subjectId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CREATE FAIL!";
		}

		return "CREATE SUCCESS!";
	}

	// done
	@Override
	public String updateUnit(long id, UnitRequestDTO unitRequestDTO) {
		long subjectId = unitRequestDTO.getSubjectId();
		int unitName = unitRequestDTO.getUnitName();
		String description = unitRequestDTO.getDescription();

		try {
			// validate unitId, subjectId and check unitName existed
			Unit unit = iUnitRepository.findByIdAndIsDisableFalse(id);
			if (unit == null) {
				throw new ResourceNotFoundException();
			}
			if (unit.getUnitName() != unitName) {
				if (iUnitRepository.findBySubjectIdAndUnitNameAndIsDisableFalse(subjectId, unitName) != null) {
					
					return "EXISTED";
				}
			}

			// save data and return
			unit.setUnitName(unitName);
			unit.setDescription(description);
			iUnitRepository.save(unit);
		} catch (Exception e) {
			logger.error("UPDATE: unitId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "UPDATE FAIL!";
		}

		return "UPDATE SUCCESS!";
	}

	@Override
	@Transactional
	public void deleteOneUnit(long id) {
		try {
			// validate unitId
			Unit unit = iUnitRepository.findByIdAndIsDisableFalse(id);
			if (unit == null) {
				throw new ResourceNotFoundException();
			}

			List<Lesson> lessonList = iLessonRepository.findByUnitIdAndIsDisableFalse(id);
			if (!lessonList.isEmpty()) {
				for (Lesson lesson : lessonList) {
					iLessonService.deleteOneLesson(lesson.getId());
				}
			}
			List<Question> questionList = iQuestionRepository.findByUnitIdAndIsDisableFalse(id);
			if (questionList.isEmpty()) {
				for (Question question : questionList) {
					iQuestionService.deleteOneQuestion(question.getId());
				}
			}

			unit.setDisable(true);
			iUnitRepository.save(unit);
		} catch (Exception e) {
			logger.error("DELETE: unitId = " + id + "! " + e.getMessage());
			throw e;
		}
	}

}
