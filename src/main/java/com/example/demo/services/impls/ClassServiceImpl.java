package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.ClassResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Classes;
import com.example.demo.models.SchoolGrade;
import com.example.demo.repositories.IClassRepository;
import com.example.demo.repositories.ISchoolGradeRepository;
import com.example.demo.services.IClassService;

@Service
public class ClassServiceImpl implements IClassService{
	
	@Autowired
	ISchoolGradeRepository iSchoolGradeRepository;
	
	@Autowired
	IClassRepository iClassRepository;
	
	@Autowired
	ModelMapper modelMapper;
	

	@Override
	public List<ClassResponseDTO> findBySchoolGradeId(long gradeId, long schoolId) {		
		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId, "DELETED");
		if (schoolGrade == null) {
			throw new ResourceNotFoundException();
		}
		List<ClassResponseDTO> classResponseDTOList = new ArrayList<>();
		List<Classes> classList =  iClassRepository.findBySchoolGradeIdAndIsDisableOrderByClassNameAsc(schoolGrade.getId(), false);
		if(!classList.isEmpty()) {
			for (Classes class1 : classList) {
				classResponseDTOList.add(modelMapper.map(class1, ClassResponseDTO.class));
			}
		}
		
		return classResponseDTOList;
	}


	@Override
	public String createClass(String className) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String deleteClass(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
