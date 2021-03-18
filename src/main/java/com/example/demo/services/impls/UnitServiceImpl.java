package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.ProgressTestDTO;
import com.example.demo.dtos.UnitDTO;
import com.example.demo.dtos.UnitViewDTO;
import com.example.demo.models.Unit;
import com.example.demo.repositories.IUnitRepository;
import com.example.demo.services.IProgressTestService;
import com.example.demo.services.IUnitService;

@Service
public class UnitServiceImpl implements IUnitService {

	@Autowired
	private IUnitRepository iUnitRepository;

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

		if (!unitDTOList.isEmpty() && !progressTestDTOList.isEmpty()) {
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

//	public Unit create(Unit unit) {
//		return iUnitRepository.save(unit);
//	}
//	
//	public List<Unit> findAll(){
//		return unitRepository.findAll();
//	}
}
