package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.UnitViewDTO;
import com.example.demo.dtos.UnitDTO;

public interface IUnitService {
	List<UnitDTO> findBySubjectIdOrderByUnitNameAsc(long subjectId);
	List<UnitViewDTO> showUnitViewBySubjectId(long subjectId);
	String createUnit(long subjectId, int unitName, String description);
	String updateUnit(long id, int unitName, String description);
	void deleteUnit(long id);
}
