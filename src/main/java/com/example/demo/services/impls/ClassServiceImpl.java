package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.ClassRequestDTO;
import com.example.demo.dtos.ClassResponseDTO;
import com.example.demo.dtos.SchoolGradeDTO;
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
	public List<ClassResponseDTO> findBySchoolGradeId(SchoolGradeDTO schoolGradeDTO) {	
		long gradeId = schoolGradeDTO.getGradeId();
		long schoolId = schoolGradeDTO.getSchoolId();
		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId, "DELETED");
		if (schoolGrade == null) {
			throw new ResourceNotFoundException();
		}
		List<ClassResponseDTO> classResponseDTOList = new ArrayList<>();
		List<Classes> classList =  iClassRepository.findBySchoolGradeIdAndStatusNotOrderByStatusAscClassNameAsc(schoolGrade.getId(), "DELETED");
		if(!classList.isEmpty()) {
			for (Classes classes : classList) {
				classResponseDTOList.add(modelMapper.map(classes, ClassResponseDTO.class));
			}
		}
		
		return classResponseDTOList;
	}


	@Override
	public String createClass( ClassRequestDTO classRequestDTO) {
		long gradeId = classRequestDTO.getGradeId();
		long schoolId = classRequestDTO.getSchoolId();
		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId, "DELETED");
		if (schoolGrade == null) {
			throw new ResourceNotFoundException();
		}
		String className = classRequestDTO.getClassName();
		Classes classes = new Classes();
		classes.setSchoolGrade(schoolGrade);
		classes.setClassName(className);
		classes.setStatus("ACTIVE");
		iClassRepository.save(classes);
		
		return "CREATE SUCCESS!";
	}


	@Override
	public String deleteClass(long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
