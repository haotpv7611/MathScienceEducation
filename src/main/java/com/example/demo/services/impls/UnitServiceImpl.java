package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

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
import com.example.demo.models.Lesson;
import com.example.demo.models.Question;
import com.example.demo.models.Subject;
import com.example.demo.models.Unit;
import com.example.demo.repositories.ILessonRepository;
import com.example.demo.repositories.IQuestionRepository;
import com.example.demo.repositories.ISubjectRepository;
import com.example.demo.repositories.IUnitRepository;
import com.example.demo.services.ILessonService;
import com.example.demo.services.IProgressTestService;
import com.example.demo.services.IQuestionService;
import com.example.demo.services.IUnitService;

@Service
public class UnitServiceImpl implements IUnitService {
	Logger logger = LoggerFactory.getLogger(UnitServiceImpl.class);

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
	public List<UnitResponseDTO> findBySubjectIdOrderByUnitNameAsc(long subjectId) {
		List<UnitResponseDTO> unitDTOList = new ArrayList<>();
		try {
			// find all active entities and have subjectId = ?, sort acscending by unitName
			List<Unit> unitList = iUnitRepository.findBySubjectIdAndIsDisableFalseOrderByUnitNameAsc(subjectId);

			// convert all entities to dtos and return
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

	// not done
	@Override
	public List<UnitViewDTO> showUnitViewBySubjectId(long subjectId) {
		// 1. get list unitDTO and progressTestDTO by subjectId
		List<UnitResponseDTO> unitDTOList = findBySubjectIdOrderByUnitNameAsc(subjectId);
		List<ProgressTestResponseDTO> progressTestDTOList = iProgressTestService.findBySubjectId(subjectId);

		List<UnitViewDTO> unitViewDTOList = new ArrayList<>();

		if (!unitDTOList.isEmpty()) {
			List<UnitResponseDTO> unitDTOListSplit = new ArrayList<>();

			// 2. split list unitDTO into small lists by progresTest unitAfterId
			// 3. group small list unitDTO and break progressTestDTO into unitViewDTO
			// 4. add all unitViewDTO into unitViewDTOList
			for (int i = 0; i < unitDTOList.size(); i++) {
				unitDTOListSplit.add(unitDTOList.get(i));
				for (int j = 0; j < progressTestDTOList.size(); j++) {
					if (progressTestDTOList.get(j).getUnitAfterId() == unitDTOList.get(i).getId()) {
						UnitViewDTO unitViewDTO = new UnitViewDTO(unitDTOListSplit, progressTestDTOList.get(j));
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
				UnitViewDTO unitViewDTO = new UnitViewDTO(unitDTOListSplit, null);
				unitViewDTOList.add(unitViewDTO);
			}
		}

		return unitViewDTOList;
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
			Subject subject = iSubjectRepository.findByIdAndIsDisableFalse(subjectId);
			if (subject == null) {
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
	public String deleteUnit(long id) {
		try {
			deleteOneUnit(id);
		} catch (Exception e) {
			logger.error("DELETE: unitId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "DELETE FAIL!";
		}
		
		return "DELETE SUCCESS!";
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
			throw e;
		}
	}

}
