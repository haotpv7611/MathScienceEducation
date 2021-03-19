package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.SubjectDTO;

public interface ISubjectService {

	public List<SubjectDTO> findSubjectByGradeId(long gradeId);
	
}
