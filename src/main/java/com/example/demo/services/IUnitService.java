package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.UnitViewDTO;
import com.example.demo.models.Unit;
import com.example.demo.dtos.UnitDTO;
import com.example.demo.dtos.UnitRequestDTO;

public interface IUnitService {
	List<UnitDTO> findBySubjectIdOrderByUnitNameAsc(long subjectId);
	Unit findByUnitIdAndIsDisable(long id);
	List<UnitViewDTO> showUnitViewBySubjectId(long subjectId);
	String createUnit(UnitRequestDTO unitRequestDTO);
	String updateUnit(long id, UnitRequestDTO unitRequestDTO);
	String deleteUnit(long id);
}
