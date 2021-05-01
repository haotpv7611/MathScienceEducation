package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.GradeResponseDTO;
import com.example.demo.models.Grade;
import com.example.demo.repositories.IGradeRepository;
import com.example.demo.services.IGradeService;

@Service
public class GradeServiceImpl implements IGradeService {
	Logger logger = LoggerFactory.getLogger(GradeServiceImpl.class);

	@Autowired
	private IGradeRepository iGradeRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<GradeResponseDTO> findAllGrades() {
		List<GradeResponseDTO> gradeResponseDTOList = new ArrayList<>();
		try {
			List<Grade> gradeList = iGradeRepository.findAll(Sort.by(Sort.Direction.ASC, "gradeName"));
			if (!gradeList.isEmpty()) {
				for (Grade grade : gradeList) {
					GradeResponseDTO gradeResponseDTO = modelMapper.map(grade, GradeResponseDTO.class);
					gradeResponseDTOList.add(gradeResponseDTO);
				}
			}
		} catch (Exception e) {
			logger.error("Find all grades! " + e.getMessage());

			return null;
		}

		return gradeResponseDTOList;
	}

//	public String createGrade(int gradeName) {
//		try {
//			// validate subjectId and check unitName existed
//			if (iGradeRepository.findByGradeName(gradeName) != null) {
//
//				return "EXISTED";
//			}
//
//			// save data and return
//			Grade grade = new Grade();
//			grade.setGradeName(gradeName);
//			iGradeRepository.save(grade);
//		} catch (Exception e) {
//			logger.error("CREATE: gradeName = " + gradeName + "! " + e.getMessage());
//
//			return "CREATE FAIL!";
//		}
//
//		return "CREATE SUCCESS!";
//	}

}
