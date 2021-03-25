package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.SchoolRequestDTO;
import com.example.demo.dtos.SchoolResponseDTO;

public interface ISchoolService {
	
	String createSchool(SchoolRequestDTO schoolRequestDTO);
	String updateSchool(long id, SchoolRequestDTO schoolRequestDTO);
	String deleteSchool(long id);
	List<SchoolResponseDTO> findAllSchool();
	SchoolResponseDTO findBySchoolId(long id);
}
