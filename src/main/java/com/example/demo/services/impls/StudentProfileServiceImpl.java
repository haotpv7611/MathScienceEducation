package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.ClassResponseDTO;
import com.example.demo.dtos.SchoolGradeDTO;
import com.example.demo.dtos.StudentRequestDTO;
import com.example.demo.dtos.StudentResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Classes;
import com.example.demo.models.School;
import com.example.demo.models.SchoolGrade;
import com.example.demo.models.StudentProfile;
import com.example.demo.repositories.IClassRepository;
import com.example.demo.repositories.ISchoolGradeRepository;
import com.example.demo.repositories.ISchoolRepository;
import com.example.demo.repositories.IStudentProfileRepository;
import com.example.demo.services.IClassService;
import com.example.demo.services.IStudentProfileService;

@Service
public class StudentProfileServiceImpl implements IStudentProfileService {

	private final String DELETE_STATUS = "DELETED";

	@Autowired
	ISchoolRepository iSchoolRepository;

	@Autowired
	ISchoolGradeRepository iSchoolGradeRepository;

	@Autowired
	IClassRepository iClassRepository;

	@Autowired
	IStudentProfileRepository iStudentProfileRepository;

	@Autowired
	IClassService iClassService;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public List<StudentResponseDTO> findStudentByListId(List<Long> ids) {
		long schoolId = ids.get(0);
		long gradeId = ids.get(1);
		long classId = ids.get(2);

		List<StudentResponseDTO> studentResponseDTOList = new ArrayList<>();

		//find if have classesId
		if (classId != 0) {
			studentResponseDTOList.addAll(findStudentByClassedId(classId));
		}

		if (schoolId != 0) {
			// check school existed
			School school = iSchoolRepository.findByIdAndStatusNot(schoolId, DELETE_STATUS);
			if (school == null) {
				throw new ResourceNotFoundException();
			}
			
			SchoolGradeDTO schoolGradeDTO = new SchoolGradeDTO(schoolId, gradeId);
			List<ClassResponseDTO> classResponseDTOList = new ArrayList<>();
			
			// find if only have schoolId
			if (gradeId == 0 && classId == 0) {
				//get all grade linked
				List<SchoolGrade> schoolGradeList = iSchoolGradeRepository.findBySchoolIdAndStatusNot(schoolId,
						DELETE_STATUS);
				if (!schoolGradeList.isEmpty()) {
					for (SchoolGrade schoolGrade : schoolGradeList) {
						schoolGradeDTO = modelMapper.map(schoolGrade, SchoolGradeDTO.class);
						//get all class
						classResponseDTOList.addAll(iClassService.findBySchoolGradeId(schoolGradeDTO));
						if (!classResponseDTOList.isEmpty()) {
							for (ClassResponseDTO classResponseDTO : classResponseDTOList) {
								//get all student
								studentResponseDTOList.addAll(findStudentByClassedId(classResponseDTO.getId()));
							}
						}
					}

				}
			}
			// find if have schoolId and grade Id
			if (gradeId != 0 && classId == 0) {
				//get all class with schoolGradeId
				classResponseDTOList = iClassService.findBySchoolGradeId(schoolGradeDTO);
				if (!classResponseDTOList.isEmpty()) {
					for (ClassResponseDTO classResponseDTO : classResponseDTOList) {
						//get all student
						studentResponseDTOList.addAll(findStudentByClassedId(classResponseDTO.getId()));
					}
				}
			}

		}

		return studentResponseDTOList;
	}

	public List<StudentResponseDTO> findStudentByClassedId(long classesId) {
		Classes classes = iClassRepository.findByIdAndStatusNot(classesId, DELETE_STATUS);
		if (classes == null) {
			throw new ResourceNotFoundException();
		}
		List<StudentResponseDTO> studentResponseDTOList = new ArrayList<>();
		List<StudentProfile> studentProfileList = iStudentProfileRepository.findByClassesIdAndStatusNot(classesId,
				DELETE_STATUS);
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
