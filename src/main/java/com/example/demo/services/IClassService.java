package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ClassRequestDTO;
import com.example.demo.dtos.ClassResponseDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.SchoolGradeDTO;

public interface IClassService {
	List<ClassResponseDTO> findBySchoolGradeId(SchoolGradeDTO schoolGradeDTO);

	String createClass(ClassRequestDTO classRequestDTO);

	String changeStatusClass(ListIdAndStatusDTO listIdAndStatusDTO);
}
