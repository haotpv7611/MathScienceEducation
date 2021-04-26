package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.GradeResponseDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.SchoolGradeDTO;
import com.example.demo.dtos.SchoolResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Classes;
import com.example.demo.models.Grade;
import com.example.demo.models.School;
import com.example.demo.models.SchoolGrade;
import com.example.demo.repositories.IClassRepository;
import com.example.demo.repositories.IGradeRepository;
import com.example.demo.repositories.ISchoolGradeRepository;
import com.example.demo.repositories.ISchoolRepository;
import com.example.demo.services.IClassService;
import com.example.demo.services.ISchoolGradeService;

@Service
public class SchoolGradeServiceImpl implements ISchoolGradeService {
	Logger logger = LoggerFactory.getLogger(SchoolGradeServiceImpl.class);
	private final String ACTIVE_STATUS = "ACTIVE";
	private final String INACTIVE_STATUS = "INACTIVE";
	private final String DELETED_STATUS = "DELETED";

	@Autowired
	private IGradeRepository iGradeRepository;

	@Autowired
	private ISchoolRepository iSchoolRepository;

	@Autowired
	private ISchoolGradeRepository iSchoolGradeRepository;

	@Autowired
	private IClassRepository iClassRepository;

	@Autowired
	private IClassService iClassService;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<SchoolResponseDTO> findSchoolLinkedByGradeId(int gradeId) {
		List<SchoolResponseDTO> schoolResponseDTOList = new ArrayList<>();
		try {
			List<SchoolGrade> schoolGradeList = iSchoolGradeRepository
					.findByGradeIdAndStatusNotOrderByStatusAsc(gradeId, DELETED_STATUS);
			if (!schoolGradeList.isEmpty()) {
				for (SchoolGrade schoolGrade : schoolGradeList) {
					SchoolResponseDTO schoolResponseDTO = (modelMapper.map(schoolGrade.getSchool(),
							SchoolResponseDTO.class));
					schoolResponseDTO.setSchoolStreet(null);
					schoolResponseDTO.setSchoolDistrict(null);
					schoolResponseDTO.setSchoolLevel(schoolGrade.getSchool().getSchoolLevel().getDescription());
					schoolResponseDTO.setStatus(schoolGrade.getStatus());
					schoolResponseDTOList.add(schoolResponseDTO);
				}
			}
		} catch (Exception e) {
			logger.error("FIND: all school linked by gradeId = " + gradeId + "! " + e.getMessage());

			return null;
		}

		return schoolResponseDTOList;
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
				// sort by gradeName
				Collections.sort(gradeDTOList);
			}
		} catch (Exception e) {
			logger.error("Find grade linked by schoolId = " + schoolId + "! " + e.getMessage());

			return null;
		}

		return gradeDTOList;
	}

	// done
	@Override
	public String linkGradeAndSchool(SchoolGradeDTO schoolGradeDTO) {
		int gradeId = schoolGradeDTO.getGradeId();
		long schoolId = schoolGradeDTO.getSchoolId();
		try {
			Grade grade = iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());
			School school = iSchoolRepository.findByIdAndStatusNot(schoolId, DELETED_STATUS);
			if (school == null) {
				throw new ResourceNotFoundException();
			}

			if (school.getStatus().equalsIgnoreCase(INACTIVE_STATUS)) {
				return "CANNOT LINK INACTIVE SCHOOL";
			}

			SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
					DELETED_STATUS);
			if (schoolGrade != null) {

				return "EXISTED!";
			}
			schoolGrade = new SchoolGrade();
			schoolGrade.setGrade(grade);
			schoolGrade.setSchool(school);
			schoolGrade.setStatus(ACTIVE_STATUS);
			iSchoolGradeRepository.save(schoolGrade);
		} catch (Exception e) {
			logger.error("LINK: schoolId = " + schoolId + " and gradeId = " + gradeId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "FIND FAIL!";
		}

		return "LINK SUCCESS!";
	}

//	 validate before remove
//	 add function active
	@Override
	@Transactional
	public void changeStatusGradeAndSchool(ListIdAndStatusDTO listIdAndStatusDTO) {
		int gradeId = Math.toIntExact(listIdAndStatusDTO.getIds().get(0));
		long schoolId = listIdAndStatusDTO.getIds().get(1);
		try {
			SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
					DELETED_STATUS);
			if (schoolGrade == null) {
				throw new ResourceNotFoundException();
			}

			String status = listIdAndStatusDTO.getStatus();

			List<Classes> classesList = iClassRepository.findBySchoolGradeIdAndStatusNot(schoolGrade.getId(),
					DELETED_STATUS);
			for (Classes classes : classesList) {
				iClassService.changeStatusOneClass(classes.getId(), status);
			}

			schoolGrade.setStatus(status);
			iSchoolGradeRepository.save(schoolGrade);
		} catch (Exception e) {
			logger.error("Change status " + listIdAndStatusDTO.getStatus() + " with schoolId = " + schoolId
					+ " and gradeId = " + gradeId + "! " + e.getMessage());
			throw e;
		}
	}

}
