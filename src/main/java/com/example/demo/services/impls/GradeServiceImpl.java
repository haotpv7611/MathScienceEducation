package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.GradeDTO;
import com.example.demo.models.Grade;
import com.example.demo.repositories.iGradeRepository;
import com.example.demo.services.IGradeService;

@Service
public class GradeServiceImpl implements IGradeService{

	@Autowired
	iGradeRepository iGradeRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	
	@Override
	public List<GradeDTO> findByIsDisable(boolean isDisable) {
		List<Grade> gradeList = iGradeRepository.findByIsDisable(false);
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
	public String createGrade(int gradeName) {
		Grade grade = new Grade();
		grade.setDisable(false);
		grade.setGradeName(gradeName);
		iGradeRepository.save(grade);
		return "CREATE SUCCESSS!";
	}

	@Override
	public void deleteGrade(long id) {
		// TODO Auto-generated method stub
		
	}

}
