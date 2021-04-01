package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.IdAndStatusDTO;
import com.example.demo.dtos.SchoolRequestDTO;
import com.example.demo.dtos.SchoolResponseDTO;

public interface ISchoolService {

	SchoolResponseDTO findSchoolById(long id);

	String checkSchoolExisted(SchoolRequestDTO schoolRequestDTO);

	String createSchool(SchoolRequestDTO schoolRequestDTO);

	String updateSchool(long id, SchoolRequestDTO schoolRequestDTO);

	String changeStatusSchool(IdAndStatusDTO idAndStatusDTO);

	List<SchoolResponseDTO> findAllSchool();

}
