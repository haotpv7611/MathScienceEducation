package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.SchoolDTO;

public interface ISchoolService {
	
	SchoolDTO createSchool(SchoolDTO schoolDTO);
	SchoolDTO updateSchool(SchoolDTO schoolDTO);
	String deleteSchool(long id);
	List<SchoolDTO> findAllSchool();
	SchoolDTO findBySchoolId(long id);
}
