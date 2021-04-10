package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.example.demo.models.Account;
import com.example.demo.models.Classes;
import com.example.demo.models.Grade;
import com.example.demo.models.School;
import com.example.demo.models.SchoolGrade;
import com.example.demo.models.StudentProfile;
import com.example.demo.repositories.IAccountRepository;
import com.example.demo.repositories.IClassRepository;
import com.example.demo.repositories.IGradeRepository;
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
	IGradeRepository iGradeRepository;

	@Autowired
	ISchoolGradeRepository iSchoolGradeRepository;

	@Autowired
	IClassRepository iClassRepository;

	@Autowired
	IStudentProfileRepository iStudentProfileRepository;

	@Autowired
	IAccountRepository iAccountRepository;

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

		if (schoolId != 0) {
			// check school existed
			School school = iSchoolRepository.findByIdAndStatusNot(schoolId, DELETE_STATUS);
			if (school == null) {
				throw new ResourceNotFoundException();
			}
			// if existed: get schoolName
			String schoolName = school.getSchoolName();

			SchoolGradeDTO schoolGradeDTO = null;
			List<ClassResponseDTO> classResponseDTOList = new ArrayList<>();

			// find if only have schoolId
			if (gradeId == 0 && classId == 0) {
				// get all grade linked
				List<SchoolGrade> schoolGradeList = iSchoolGradeRepository.findBySchoolIdAndStatusNot(schoolId,
						DELETE_STATUS);

				if (!schoolGradeList.isEmpty()) {
//					System.out.println("schoolGrade" + schoolGradeList.size());
					List<Classes> classesList = new ArrayList<>();
					for (SchoolGrade schoolGrade : schoolGradeList) {

						classesList.addAll(iClassRepository.findBySchoolGradeIdAndStatusNotOrderByStatusAscClassNameAsc(
								schoolGrade.getId(), DELETE_STATUS));

					}

					if (!classesList.isEmpty()) {
						for (Classes classes : classesList) {
							// get all student in class

							Grade grade = classes.getSchoolGrade().getGrade();

							int gradeName = grade.getGradeName();
							studentResponseDTOList
									.addAll(findStudentByClassedId(classes.getId(), schoolName, gradeName));

						}
					}

				}
			}
			// find if have schoolId and grade Id
			if (gradeId != 0) {
				Grade grade = iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());
				int gradeName = grade.getGradeName();
				if (classId == 0) {
					schoolGradeDTO = new SchoolGradeDTO(schoolId, gradeId);

					// get all class with schoolGradeId
					classResponseDTOList = iClassService.findBySchoolGradeId(schoolGradeDTO);
					if (!classResponseDTOList.isEmpty()) {
						for (ClassResponseDTO classResponseDTO : classResponseDTOList) {
							// get all student
							studentResponseDTOList
									.addAll(findStudentByClassedId(classResponseDTO.getId(), schoolName, gradeName));
						}
					}
				}
//				 find if have classesId
				if (classId != 0) {
					studentResponseDTOList.addAll(findStudentByClassedId(classId, schoolName, gradeName));
				}
			}

		}

		return studentResponseDTOList;
	}

	public List<StudentResponseDTO> findStudentByClassedId(long classesId, String schoolName, int gradeName) {
		Classes classes = iClassRepository.findByIdAndStatusNot(classesId, DELETE_STATUS);
		if (classes == null) {
			throw new ResourceNotFoundException();
		}
		String className = classes.getClassName();
		List<StudentResponseDTO> studentResponseDTOList = new ArrayList<>();
		List<StudentProfile> studentProfileList = iStudentProfileRepository.findByClassesIdAndStatusNot(classesId,
				DELETE_STATUS);
		for (StudentProfile studentProfile : studentProfileList) {
			Account account = studentProfile.getAccount();
			StudentResponseDTO studentResponseDTO = modelMapper.map(account, StudentResponseDTO.class);
			studentResponseDTO.setSchoolName(schoolName);
			studentResponseDTO.setGradeName(gradeName);
			studentResponseDTO.setClassName(className);
			studentResponseDTO.setGender(studentProfile.getGender());
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
		// create account before

		long schoolId = studentRequestDTO.getSchoolId();
		School school = iSchoolRepository.findByIdAndStatusNot(schoolId, DELETE_STATUS);
		if (school == null) {
			throw new ResourceNotFoundException();
		}

		long gradeId = studentRequestDTO.getGradeId();
		Grade grade = iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());

		String schoolCode = school.getSchoolCode() + school.getSchoolCount();
		int gradeName = grade.getGradeName();

		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
				DELETE_STATUS);
		List<StudentProfile> studentProfileList = new ArrayList<>();
		List<Classes> classesList = schoolGrade.getClassList();

		for (Classes classes2 : classesList) {
			StudentProfile studentProfile = null;
			if (!classes2.getClassName().equalsIgnoreCase("PENDING")
					&& !classes.getClassName().equalsIgnoreCase(DELETE_STATUS)) {
				studentProfile = iStudentProfileRepository
						.findFirstByClassesIdAndStatusLikeOrderByStudentCountDesc(classes2.getId(), "ACTIVE");
			}
			studentProfileList.add(studentProfile);
		}
		StudentProfile studentProfile = null;
		if (!studentProfileList.isEmpty()) {
			studentProfile = Collections.max(studentProfileList, Comparator.comparing(s -> s.getStudentCount()));
		}
		long studentCount = 1;
		if (studentProfile != null) {
			studentCount = studentProfile.getStudentCount() + 1;
		}

		Account account = new Account();
		account.setFirstName(studentRequestDTO.getFirtName());
		account.setLastName(studentRequestDTO.getLastName());
		String username = generateUsername(schoolCode, gradeName, studentCount);
		account.setUsername(username);
		account.setPassword(username);
		account.setRoleId(3);
		account.setStatus("ACTIVE");

		iAccountRepository.save(account);

		studentProfile = new StudentProfile(studentRequestDTO.getDoB(), studentRequestDTO.getGender(),
				studentRequestDTO.getParentName(), studentRequestDTO.getParentPhone(), account, classes);
		studentProfile.setStudentCount(studentCount);
		studentProfile.setStatus("ACTIVE");
//		if (accountId != 0) {
//
//		}

		iStudentProfileRepository.save(studentProfile);

		return "CREATE SUCCESS !";
	}

	private String generateUsername(String schoolCode, int gradeName, long studentCount) {
		String username = schoolCode + String.format("%02d", gradeName) + String.format("%03d", studentCount);
		return username;
	}

}
