package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.StudentRequestDTO;
import com.example.demo.dtos.StudentResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Classes;
import com.example.demo.models.SchoolGrade;
import com.example.demo.models.StudentProfile;
import com.example.demo.repositories.IClassRepository;
import com.example.demo.repositories.ISchoolGradeRepository;
import com.example.demo.repositories.IStudentProfileRepository;
import com.example.demo.services.IStudentProfileService;

@Service
public class StudentProfileServiceImpl implements IStudentProfileService {

	private final String DELETE_STATUS = "DELETED";
	@Autowired
	ISchoolGradeRepository iSchoolGradeRepository;

	@Autowired
	IClassRepository iClassRepository;

	@Autowired
	IStudentProfileRepository iStudentProfileRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public List<StudentResponseDTO> findStudentByListId(ListIdAndStatusDTO listIdAndStatusDTO) {
		long schoolId = listIdAndStatusDTO.getIds().get(0);
		long gradeId = listIdAndStatusDTO.getIds().get(1);
		long classId = listIdAndStatusDTO.getIds().get(2);

		List<StudentProfile> studentProfileList = new ArrayList<>();
		List<Classes> classesList = new ArrayList<>();

		if (gradeId == 0 && classId == 0) {
			List<SchoolGrade> schoolGradeList = iSchoolGradeRepository.findBySchoolIdAndStatusNot(schoolId,
					DELETE_STATUS);
			if (!schoolGradeList.isEmpty()) {
				for (SchoolGrade schoolGrade : schoolGradeList) {
					addClasses(schoolGrade, classesList);
					if (!classesList.isEmpty()) {
						for (Classes classes : classesList) {
							addStudentProfile(classes, studentProfileList);
						}
					}
				}

			}
		} else if (classId == 0) {
			SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
					"DELETED");
			if (schoolGrade != null) {
				addClasses(schoolGrade, classesList);
				if (!classesList.isEmpty()) {
					for (Classes classes : classesList) {
						addStudentProfile(classes, studentProfileList);
					}
				}
			}
		} else {
			Classes classes = iClassRepository.findByIdAndStatusNot(classId, DELETE_STATUS);
			if (classes != null) {
				addStudentProfile(classes, studentProfileList);
			}
		}
		List<StudentResponseDTO> studentResponseDTOList = new ArrayList<>();
		for (StudentProfile studentProfile : studentProfileList) {
			StudentResponseDTO studentResponseDTO = modelMapper.map(studentProfile, StudentResponseDTO.class);
			studentResponseDTOList.add(studentResponseDTO);
		}

		return studentResponseDTOList;
	}

	@Override
	@Transactional
	public String createStudenProfile(StudentRequestDTO studentRequestDTO) {
		long classId = studentRequestDTO.getClassId();
		Classes classes = iClassRepository.findByIdAndStatusNot(classId, DELETE_STATUS);
		if (classes == null) {
			throw new ResourceNotFoundException();
		}

		long accountId = studentRequestDTO.getAccountId();

		SchoolGrade schoolGrade = classes.getSchoolGrade();
		String schoolCode = schoolGrade.getSchool().getSchoolCode() + schoolGrade.getSchool().getSchoolCount();
		int gradeName = schoolGrade.getGrade().getGradeName();
		long schoolGradeId = schoolGrade.getId();
		List<Classes> classesList = schoolGrade.getClassList();
		

		if (accountId != 0) {

		}

		StudentProfile studentProfile = modelMapper.map(studentRequestDTO, StudentProfile.class);

		return null;
	}

	private void addClasses(SchoolGrade schoolGrade, List<Classes> classesList) {
		classesList.addAll(iClassRepository
				.findBySchoolGradeIdAndStatusNotOrderByStatusAscClassNameAsc(schoolGrade.getId(), DELETE_STATUS));
	}

	private void addStudentProfile(Classes classes, List<StudentProfile> studentProfileList) {
		studentProfileList
				.addAll(iStudentProfileRepository.findByClassesIdAndStatusNot(classes.getId(), DELETE_STATUS));
	}

	private String generateUsername(String schoolCode, int gradeName, int studentCount) {
		String username = schoolCode + String.format("%02d", gradeName) + String.format("%03d", studentCount);
		return username;
	}

}
