package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.UnitViewDTO;
import com.example.demo.dtos.UnitDTO;

public interface IUnitService {
	List<UnitDTO> findBySubjectIdOrderByUnitNameAsc(long subjectId);
	List<UnitViewDTO> showUnitViewBySubjectId(long subjectId);
	UnitDTO createUnit(UnitDTO unitDTO);
	UnitDTO updateUnit(UnitDTO unitDTO);
	String deleteUnit(Long id);
}
