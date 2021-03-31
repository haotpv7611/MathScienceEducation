package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.IdAndStatusDTO;
import com.example.demo.dtos.SchoolRequestDTO;
import com.example.demo.dtos.SchoolResponseDTO;

public interface ISchoolService {

	List<SchoolResponseDTO> findSchoolLinkedByGradeId(long gradeId);

	SchoolResponseDTO findSchoolById(long id);

	String checkSchoolExisted(String schoolName, String district, String schoolLevel);

	String createSchool(SchoolRequestDTO schoolRequestDTO);

	String updateSchool(long id, SchoolRequestDTO schoolRequestDTO);

	String changeStatusSchool(IdAndStatusDTO idAndStatusDTO);

	List<SchoolResponseDTO> findAllSchool();

}
