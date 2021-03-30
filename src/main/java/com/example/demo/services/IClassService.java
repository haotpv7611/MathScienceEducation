package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ClassResponseDTO;

public interface IClassService {
	List<ClassResponseDTO> findBySchoolGradeId(long gradeId, long schoolId);
	String createClass(String className);
	String deleteClass(long id);
}
