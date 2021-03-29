package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.SubjectDTO;
import com.example.demo.models.Subject;

public interface ISubjectService {

	public List<SubjectDTO> findSubjectByGradeId(long gradeId);
	Subject findByIdAndIsDisable(long id, boolean isDisable);
	
}
