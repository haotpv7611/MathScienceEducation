package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.GradeDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Grade;
import com.example.demo.models.School;
import com.example.demo.models.SchoolGrade;
import com.example.demo.repositories.iGradeRepository;
import com.example.demo.repositories.ISchoolRepository;
import com.example.demo.services.IGradeService;

@Service
public class GradeServiceImpl implements IGradeService{

	@Autowired
	iGradeRepository iGradeRepository;
	
	@Autowired
	ISchoolRepository iSchoolRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	
	@Override
	public List<GradeDTO> findAllGrade() {
		List<Grade> gradeList = iGradeRepository.findAll(Sort.by(Sort.Direction.ASC, "gradeName"));
		List<GradeDTO> gradeDTOList = new ArrayList<>();
		if(!gradeList.isEmpty()) {
			for (Grade grade : gradeList) {
				GradeDTO gradeDTO = modelMapper.map(grade, GradeDTO.class);
				gradeDTOList.add(gradeDTO);
			}
		}
		return gradeDTOList;
	}
	
	@Override
	public List<String> findLinkedGradeBySchoolId(long id) {
		School school = iSchoolRepository.findByIdAndIsDisable(id, false);
		if (school == null) {
			throw new ResourceNotFoundException();
		}
		
		List<String> linkedGradeList = new ArrayList<>();
		Set<SchoolGrade> schoolGradeList = school.getSchoolGrade();
		if (!schoolGradeList.isEmpty()) {
			for (SchoolGrade schoolGrade : schoolGradeList) {
				linkedGradeList.add("Grade " + schoolGrade.getGrade().getGradeName());
			}
		}

		return linkedGradeList;
	}

	public void testMany(long gradeId){
//		Grade grade = iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());
//		School schools = iSchoolRepository.findById(1L);
////		System.out.println(grade.getGradeName());
////		System.out.println(grade.getSchools());
//		Set<SchoolGrade> schoolList =  grade.getSchoolGrade();
//		for (SchoolGrade school : schoolList) {
//			System.out.println(school.);
//			System.out.println(school.getSchool().getSchoolName());
////			System.out.println(school.getSchoolName());
//		}
//		
//		System.out.println(grade.getSchoolGrade().toString());
	}
//	
//	public void linkSchool(long gradeId, long schoolId) {
//		Grade grade = iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());
//		School school = iSchoolRepository.findById(schoolId).orElseThrow(() -> new ResourceNotFoundException());
//		grade.getSchools().add(school);
//		iGradeRepository.save(grade);
//	}
	
	
}
