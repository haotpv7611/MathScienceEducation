package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.GradeDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.SchoolGradeDTO;
import com.example.demo.dtos.SchoolResponseDTO;

public interface ISchoolGradeService {
	String linkGradeAndSchool(SchoolGradeDTO schoolGradeDTO);

	String changeStatusGradeAndSchool(ListIdAndStatusDTO listIdAndStatusDTO);

	List<SchoolResponseDTO> findSchoolLinkedByGradeId(long gradeId);
	
//	List<GradeDTO> findGradeLinkedBySchoolId(long schoolId);
}
