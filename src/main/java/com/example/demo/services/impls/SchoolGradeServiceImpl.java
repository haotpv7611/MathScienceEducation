package com.example.demo.services.impls;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Grade;
import com.example.demo.models.School;
import com.example.demo.models.SchoolGrade;
import com.example.demo.repositories.IGradeRepository;
import com.example.demo.repositories.ISchoolGradeRepository;
import com.example.demo.repositories.ISchoolRepository;
import com.example.demo.services.ISchoolGradeService;

@Service
public class SchoolGradeServiceImpl implements ISchoolGradeService {

	@Autowired
	IGradeRepository iGradeRepository;

	@Autowired
	ISchoolRepository iSchoolRepository;

	@Autowired
	ISchoolGradeRepository iSchoolGradeRepository;

	@Autowired
	ModelMapper modelMapper;

	// validate linked
	@Override
	public String linkGradeAndSchool(long gradeId, long schoolId) {
		Grade grade = iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());
		School school = iSchoolRepository.findByIdAndStatusNot(schoolId, "DELETED");
		if (school == null) {
			throw new ResourceNotFoundException();
		}

		SchoolGrade schoolGrade = new SchoolGrade();
		schoolGrade.setGrade(grade);
		schoolGrade.setSchool(school);
		schoolGrade.setStatus("ACTIVE");
		iSchoolGradeRepository.save(schoolGrade);

		return "LINK SUCCESS!";
	}

	// validate before remove
	// add function active
//	@Override
//	@Transactional
//	public String changeStatusGradeAndSchool(long gradeId, List<Long> schoolIds, String status) {
//		iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());
//		for (Long schoolId : schoolIds) {
//			School school = iSchoolRepository.findByIdAndStatusNot(schoolId, "DELETED");
//			if (school == null) {
//				throw new ResourceNotFoundException();
//			}
//		}
//
//		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndIsDisable(gradeId, schoolId, false);
//		if (schoolGrade == null) {
//			throw new ResourceNotFoundException();
//		}
//		schoolGrade.setDisable(true);
//		iSchoolGradeRepository.save(schoolGrade);
//
//		return "REMOVE LINK SUCCESS!";
//	}
}
