package com.example.demo.services.impls;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Grade;
import com.example.demo.models.School;
import com.example.demo.models.SchoolGrade;
import com.example.demo.repositories.iGradeRepository;
import com.example.demo.repositories.ISchoolGradeRepository;
import com.example.demo.repositories.ISchoolRepository;
import com.example.demo.services.ISchoolGradeService;

@Service
public class SchoolGradeServiceImpl implements ISchoolGradeService {

	@Autowired
	iGradeRepository iGradeRepository;

	@Autowired
	ISchoolRepository iSchoolRepository;

	@Autowired
	ISchoolGradeRepository iSchoolGradeRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public String linkGradeAndSchool(long gradeId, long schoolId) {
		Grade grade = iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());
		School school = iSchoolRepository.findByIdAndIsDisable(schoolId, false);
		if (school == null) {
			throw new ResourceNotFoundException();
		}

		SchoolGrade schoolGrade = new SchoolGrade();
		schoolGrade.setGrade(grade);
		schoolGrade.setSchool(school);
		schoolGrade.setDisable(false);
		iSchoolGradeRepository.save(schoolGrade);

		return "LINK SUCCESS!";
	}

	public String removeLinkGradeAndSchool(long gradeId, long schoolId) {
		iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());
		School school = iSchoolRepository.findByIdAndIsDisable(schoolId, false);
		if (school == null) {
			throw new ResourceNotFoundException();
		}

		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndIsDisable(gradeId, schoolId, false);
		if (schoolGrade == null) {
			throw new ResourceNotFoundException();
		}
		schoolGrade.setDisable(true);
		iSchoolGradeRepository.save(schoolGrade);

		return "REMOVE LINK SUCCESS!";
	}
}
