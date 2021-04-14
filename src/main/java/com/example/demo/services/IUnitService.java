package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.UnitRequestDTO;
import com.example.demo.dtos.UnitResponseDTO;
import com.example.demo.dtos.UnitViewDTO;

public interface IUnitService {

	Object findById(long id);

	List<UnitResponseDTO> findBySubjectId(long subjectId);

	List<UnitResponseDTO> findAllUnitAfterIdsBySubjectId(long subjectId);

	List<UnitViewDTO> showUnitViewBySubjectId(long subjectId, long accountId);

	String createUnit(UnitRequestDTO unitRequestDTO);

	String updateUnit(long id, UnitRequestDTO unitRequestDTO);

	String deleteUnit(long id);

	void deleteOneUnit(long id);
}
