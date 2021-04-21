package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.SchoolLevelResponseDTO;

public interface ISchoolLevelService {

	List<SchoolLevelResponseDTO> findAll();
}
