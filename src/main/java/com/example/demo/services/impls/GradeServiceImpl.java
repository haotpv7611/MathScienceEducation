package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.GradeDTO;
import com.example.demo.models.Grade;
import com.example.demo.repositories.IGradeRepository;
import com.example.demo.services.IGradeService;

@Service
public class GradeServiceImpl implements IGradeService {

	@Autowired
	IGradeRepository iGradeRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<GradeDTO> findAllGrade() {
		List<Grade> gradeList = iGradeRepository.findAll(Sort.by(Sort.Direction.ASC, "gradeName"));
		List<GradeDTO> gradeDTOList = new ArrayList<>();
		if (!gradeList.isEmpty()) {
			for (Grade grade : gradeList) {
				GradeDTO gradeDTO = modelMapper.map(grade, GradeDTO.class);
				gradeDTOList.add(gradeDTO);
			}
		}

		return gradeDTOList;
	}

}
