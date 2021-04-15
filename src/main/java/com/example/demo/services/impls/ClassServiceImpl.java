package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.ClassRequestDTO;
import com.example.demo.dtos.ClassResponseDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.SchoolGradeDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Classes;
import com.example.demo.models.SchoolGrade;
import com.example.demo.repositories.IClassRepository;
import com.example.demo.repositories.ISchoolGradeRepository;
import com.example.demo.services.IClassService;

@Service
public class ClassServiceImpl implements IClassService{
	private final String DELETED_STATUS = "DELETED";
	
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
	public Map<Long, String> findAllClass(){
		Map<Long, String> classesMap = new HashMap<>();
		List<Classes> classesList = iClassRepository.findByStatusNot(DELETED_STATUS);
		for (Classes classes : classesList) {
			classesMap.put(classes.getId(), classes.getClassName());
		}
		
		return classesMap;
	}

	@Override
	public String createClass( ClassRequestDTO classRequestDTO) {
		long gradeId = classRequestDTO.getGradeId();
		long schoolId = classRequestDTO.getSchoolId();
		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId, "DELETED");
		if (schoolGrade == null) {
			throw new ResourceNotFoundException();
		}
		List<Classes> classList = schoolGrade.getClassList();
		
		String className = classRequestDTO.getClassName();
		for (Classes classes : classList) {
			if (!classes.getStatus().equals("DELETE") && classes.getClassName().equalsIgnoreCase(className)) {
				return "EXISTED";
			}
			
		}		
		
		Classes classes = new Classes();
		classes.setSchoolGrade(schoolGrade);
		classes.setClassName(className);
		classes.setStatus("ACTIVE");
		iClassRepository.save(classes);
		
		return "CREATE SUCCESS!";
	}


	@Override
	@Transactional
	public String changeStatusClass(ListIdAndStatusDTO listIdAndStatusDTO) {
		List<Long> ids = listIdAndStatusDTO.getIds();
		String status = listIdAndStatusDTO.getStatus();

		// 1. connect database through repository
		// 2. find entity by id
		// 3. if not existed throw exception
		for (long id : ids) {
			Classes classes = iClassRepository.findByIdAndStatusNot(id, "DELETED");
			if (classes == null) {
				throw new ResourceNotFoundException();
			}

			// 4. update entity with isDisable = true
			classes.setStatus(status);
			iClassRepository.save(classes);
		}

		return "CHANGE SUCCESS!";
	}
	
}
