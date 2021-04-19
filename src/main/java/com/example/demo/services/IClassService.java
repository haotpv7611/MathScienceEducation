package com.example.demo.services;

import java.util.List;
import java.util.Map;

import com.example.demo.dtos.ClassRequestDTO;
import com.example.demo.dtos.ClassResponseDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.SchoolGradeDTO;

public interface IClassService {
	List<ClassResponseDTO> findBySchoolGradeId(SchoolGradeDTO schoolGradeDTO);

	String createClass(ClassRequestDTO classRequestDTO);
	
	String updateClass(long id, ClassRequestDTO classRequestDTO);

	String changeStatusClass(ListIdAndStatusDTO listIdAndStatusDTO);
	
	Map<Long, String> findAllClass();
}
