package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.ProgressTestDTO;
import com.example.demo.dtos.UnitDTO;
import com.example.demo.dtos.UnitRequestDTO;
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
	private ModelMapper modelMapper;

	@Autowired
	private UnitServiceImpl unitServiceImpl;

	@Autowired
	private IProgressTestService iProgressTestService;

	@Override
	public List<UnitDTO> findBySubjectIdOrderByUnitNameAsc(long subjectId) {

		// 1. connect database through repository
		// 2. find all entities are not disable and have subjectId = ?, sort acscending
		// by unitName
		List<Unit> unitList = iUnitRepository.findBySubjectIdAndIsDisableOrderByUnitNameAsc(subjectId, false);

		List<UnitDTO> unitDTOList = new ArrayList<>();

		// 3. convert all entities to dtos
		// 4. add all dtos to newsDTOList and return
		if (!unitList.isEmpty()) {
			for (Unit unit : unitList) {
				UnitDTO unitDTO = modelMapper.map(unit, UnitDTO.class);
				unitDTOList.add(unitDTO);
			}
		}

		return unitDTOList;
	}

	@Override
	public List<UnitViewDTO> showUnitViewBySubjectId(long subjectId) {

		// 1. get list unitDTO and progressTestDTO by subjectId
		List<UnitDTO> unitDTOList = unitServiceImpl.findBySubjectIdOrderByUnitNameAsc(subjectId);
		List<ProgressTestDTO> progressTestDTOList = iProgressTestService.findBySubjectId(subjectId);

		List<UnitViewDTO> unitViewDTOList = new ArrayList<>();

		if (!unitDTOList.isEmpty()) {
			List<UnitDTO> unitDTOListSplit = new ArrayList<>();

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

	@Override
	public String createUnit(UnitRequestDTO unitRequestDTO) {
		Subject subject = iSubjectRepository.findByIdAndIsDisable(unitRequestDTO.getSubjectId(), false);
		if (subject == null) {
			throw new ResourceNotFoundException();
		}
//		List<Unit> listUnits = iUnitRepository
//				.findBySubjectIdAndIsDisableOrderByUnitNameAsc(unitRequestDTO.getSubjectId(), false);
//		for (Unit unit : listUnits) {
//			if (unitRequestDTO.getUnitName() == unit.getUnitName()) {
//				return "Unit is existed !";
//			}
//		}
		
		if (iUnitRepository.findBySubjectIdAndUnitNameAndIsDisable(unitRequestDTO.getSubjectId(), unitRequestDTO.getUnitName(), false) != null) {
			return "EXISTED";
		}
		Unit unit = modelMapper.map(unitRequestDTO, Unit.class);
		unit.setDisable(false);
		iUnitRepository.save(unit);
		return "CREATE SUCCESS!";
	}

	@Override
	public String updateUnit(long id, UnitRequestDTO unitRequestDTO) {
		int unitName = unitRequestDTO.getUnitName();
		Unit unit = iUnitRepository.findByIdAndIsDisable(id, false);
		System.out.println(unit);

		if (unit == null) {
			throw new ResourceNotFoundException();
		}

//		Unit unit = iUnitRepository.findById(unitRequestDTO.getId()).orElseThrow(() -> new ResourceNotFoundException());
//		if (unit.isDisable()) {
//			throw new ResourceNotFoundException();
//		}

		if (unit.getUnitName() != unitName) {
			if (iUnitRepository.findBySubjectIdAndUnitNameAndIsDisable(unit.getSubjectId(), unitName, false) != null) {
				return "EXISTED";
			}
		}
//		List<Unit> listUnits = iUnitRepository
//				.findBySubjectIdAndIsDisableOrderByUnitNameAsc(unitRequestDTO.getSubjectId(), false);
//		for (Unit unit1 : listUnits) {
//			if (unitRequestDTO.getUnitName() == unit1.getUnitName()) {
//				return "Unit is existed !";
//			}
//		}
		unit.setUnitName(unitRequestDTO.getUnitName());
		unit.setDescription(unitRequestDTO.getDescription());
		iUnitRepository.save(unit);
		return "UPDATE SUCCESS !";
	}

	@Override
	@Transactional
	public String deleteUnit(long id) {
		Unit unit = iUnitRepository.findByIdAndIsDisable(id, false);
		if (unit == null) {
			throw new ResourceNotFoundException();
		}
		
		List<Lesson> listLesson = iLessonRepository.findByUnitIdAndIsDisableOrderByLessonNameAsc(id, false);
		if (!listLesson.isEmpty()) {
			for (Lesson lesson : listLesson) {
				iLessonService.deleteLesson(lesson.getId());
			}
		}

		List<Question> questionLists = iQuestionRepository.findByUnitIdAndIsDisable(id, false);
		if (!questionLists.isEmpty()) {
			for (Question question : questionLists) {
				iQuestionService.deleteQuestion(question.getId());
			}
		}

		unit.setDisable(true);
		iUnitRepository.save(unit);
		return "DELETE SUCCESS !";
	}

//	@Override
//	public Unit findById(long id) {
//		Unit unit = iUnitRepository.findByIdAndIsDisable(id, false);
//		if (unit == null) {
//			throw new ResourceNotFoundException();
//		}
//		return unit;
//	}

}
