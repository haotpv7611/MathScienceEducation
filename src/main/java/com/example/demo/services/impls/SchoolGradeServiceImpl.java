package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.GradeResponseDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.SchoolGradeDTO;
import com.example.demo.dtos.SchoolResponseDTO;
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
	Logger logger = LoggerFactory.getLogger(SchoolGradeServiceImpl.class);

	@Autowired
	IGradeRepository iGradeRepository;

	@Autowired
	ISchoolRepository iSchoolRepository;

	@Autowired
	ISchoolGradeRepository iSchoolGradeRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public List<SchoolResponseDTO> findSchoolLinkedByGradeId(int gradeId) {
		List<SchoolGrade> schoolGradeList = iSchoolGradeRepository.findByGradeIdAndStatusNotOrderByStatusAsc(gradeId,
				"DELETED");
		List<SchoolResponseDTO> schoolResponseDTOList = new ArrayList<>();

		if (!schoolGradeList.isEmpty()) {
			for (SchoolGrade schoolGrade : schoolGradeList) {
				SchoolResponseDTO schoolResponseDTO = (modelMapper.map(schoolGrade.getSchool(),
						SchoolResponseDTO.class));
				schoolResponseDTO.setSchoolStreet(null);
				schoolResponseDTO.setSchoolDistrict(null);
				schoolResponseDTO.setSchoolLevel(null);
				schoolResponseDTO.setStatus(schoolGrade.getStatus());
				schoolResponseDTOList.add(schoolResponseDTO);
			}
		}

		return schoolResponseDTOList;
	}

	// done
	@Override
	public String linkGradeAndSchool(SchoolGradeDTO schoolGradeDTO) {
		int gradeId = schoolGradeDTO.getGradeId();
		long schoolId = schoolGradeDTO.getSchoolId();
		Grade grade = iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());
		School school = iSchoolRepository.findByIdAndStatusNot(schoolId, "DELETED");
		if (school == null) {
			throw new ResourceNotFoundException();
		}

		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
				"DELETED");
		if (schoolGrade != null) {
			return "EXISTED!";
		}
		schoolGrade = new SchoolGrade();
		schoolGrade.setGrade(grade);
		schoolGrade.setSchool(school);
		schoolGrade.setStatus("ACTIVE");
		iSchoolGradeRepository.save(schoolGrade);

		return "LINK SUCCESS!";
	}

//	 validate before remove
//	 add function active
	@Override
	public String changeStatusGradeAndSchool(ListIdAndStatusDTO listIdAndStatusDTO) {
		int gradeId = Math.toIntExact(listIdAndStatusDTO.getIds().get(0));
		long schoolId = listIdAndStatusDTO.getIds().get(1);
		iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());

		School school = iSchoolRepository.findByIdAndStatusNot(schoolId, "DELETED");
		if (school == null) {
			throw new ResourceNotFoundException();
		}

		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
				"DELETED");
		if (schoolGrade == null) {
			throw new ResourceNotFoundException();
		}
		String status = listIdAndStatusDTO.getStatus();
		schoolGrade.setStatus(status);
		iSchoolGradeRepository.save(schoolGrade);

		// change status lower level

		return "CHANGE SUCCESS!";
	}

	// done
	@Override
	public List<GradeResponseDTO> findGradeLinkedBySchoolId(long schoolId) {
		List<GradeResponseDTO> gradeDTOList = new ArrayList<>();
		try {
			// find all schoolGrade active and get all grade
			List<SchoolGrade> schoolGradeList = iSchoolGradeRepository.findBySchoolIdAndStatusNot(schoolId, "DELETED");			
			if (!schoolGradeList.isEmpty()) {
				for (SchoolGrade schoolGrade : schoolGradeList) {
					Grade grade = schoolGrade.getGrade();
					GradeResponseDTO gradeDTO = modelMapper.map(grade, GradeResponseDTO.class);
					gradeDTOList.add(gradeDTO);
				}
				//sort by gradeName
				Collections.sort(gradeDTOList);
			}
		} catch (Exception e) {
			logger.error("Find grade linked by schoolId = " + schoolId + "! " + e.getMessage());

			return null;
		}

		return gradeDTOList;
	}
}
