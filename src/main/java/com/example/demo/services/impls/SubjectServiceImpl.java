package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.demo.dtos.SubjectDTO;
import com.example.demo.models.Subject;
import com.example.demo.repositories.ISubjectRepository;
import com.example.demo.services.ISubjectService;

@Service
public class SubjectServiceImpl implements ISubjectService {

	@Autowired
	ISubjectRepository iSubjectRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public List<SubjectDTO> findSubjectByGradeId(long gradeId) {
		List<Subject> subjectList = iSubjectRepository.findByGradeIdAndIsDisable(gradeId, false);
		List<SubjectDTO> subjectDTOList = new ArrayList<>();
		
		if(!subjectList.isEmpty()) {
			for (Subject subject : subjectList) {
				subjectDTOList.add(modelMapper.map(subject, SubjectDTO.class));
			}
		}
		
		return subjectDTOList;
	}

}
