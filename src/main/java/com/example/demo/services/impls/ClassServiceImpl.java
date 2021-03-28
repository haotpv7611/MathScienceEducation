package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.ClassResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Class;
import com.example.demo.models.SchoolGrade;
import com.example.demo.repositories.ISchoolGradeRepository;
import com.example.demo.services.IClassService;

@Service
public class ClassServiceImpl implements IClassService{
	
	@Autowired
	ISchoolGradeRepository iSchoolGradeRepository;
	
	@Autowired
	ModelMapper modelMapper;
	

	@Override
	public List<ClassResponseDTO> findBySchoolGradeId(long gradeId, long schoolId) {		
		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndIsDisable(gradeId, schoolId, false);
		if (schoolGrade == null) {
			throw new ResourceNotFoundException();
		}
		List<ClassResponseDTO> classResponseDTOList = new ArrayList<>();
		List<Class> classList =  schoolGrade.getClassList();
		if(!classList.isEmpty()) {
			for (Class class1 : classList) {
				classResponseDTOList.add(modelMapper.map(class1, ClassResponseDTO.class));
			}
		}
		
		return classResponseDTOList;
	}

}
