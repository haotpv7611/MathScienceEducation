package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.ClassResponseDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;
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
	Logger logger = LoggerFactory.getLogger(StudentProfileServiceImpl.class);

	private final String DELETE_STATUS = "DELETED";
	private final String PENDING_STATUS = "PENDING";
	private final String DEFAULT_PASSWORD = "abc123456";
	private final int STUDENT_ROLE = 3;

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
	public StudentResponseDTO findStudentByAccountId(long accountId) {
		StudentResponseDTO studentResponseDTO = new StudentResponseDTO();
		try {
			Account account = iAccountRepository.findByIdAndStatus(accountId, "ACTIVE");
			System.out.println(account);
			if (account != null) {
				StudentProfile studentProfile = account.getStudentProfile();
				String className = studentProfile.getClasses().getClassName();
				String schoolName = studentProfile.getClasses().getSchoolGrade().getSchool().getSchoolName();

				studentResponseDTO.setFirstName(account.getFirstName());
				studentResponseDTO.setLastName(account.getLastName());
				studentResponseDTO.setClassName(className);
				studentResponseDTO.setSchoolName(schoolName);
			}
		} catch (Exception e) {
			logger.error("FIND: student by accountId = " + accountId + "! " + e.getMessage());

			return null;
		}

		return studentResponseDTO;
	}

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

//classId --> schoolGradeId --> gradeName --> schoolCode
//schoolGradeId --> all className != pending --> all student active or inactive --> count max + 1
	@Override
	@Transactional
	public String createStudenProfile(StudentRequestDTO studentRequestDTO) {
		long classId = studentRequestDTO.getClassId();
		Classes classes = iClassRepository.findByIdAndStatusNot(classId, DELETE_STATUS);
		int gradeName = classes.getSchoolGrade().getGrade().getGradeName();
		String schoolCode = classes.getSchoolGrade().getSchool().getSchoolCode()
				+ classes.getSchoolGrade().getSchool().getSchoolCount();

		List<Classes> classesList = classes.getSchoolGrade().getClassList();
		List<StudentProfile> studentProfileList = new ArrayList<>();
		if (!classesList.isEmpty()) {
			for (Classes classes2 : classesList) {
				if (classes2.getStatus().equals("PENDING")) {
					classesList.remove(classes2);
				} else {
					StudentProfile studentProfile = iStudentProfileRepository
							.findFirstByClassesIdAndStatusLikeOrderByStudentCountDesc(classes2.getId(), "ACTIVE");

					if (studentProfile != null) {
						studentProfileList.add(studentProfile);
					}
				}
			}
		}
		StudentProfile studentProfile = null;
		long studentCount = 1;
		if (!studentProfileList.isEmpty()) {
			for (StudentProfile studentProfile2 : studentProfileList) {
				if (studentProfile2.getStudentCount() > studentCount) {
					studentCount = studentProfile2.getStudentCount();
				}
			}
			studentCount++;
		}

		String username = generateUsername(schoolCode, gradeName, studentCount);
		String firstName = studentRequestDTO.getFirtName();
		String lastName = studentRequestDTO.getLastName();
		Account account = new Account(username, DEFAULT_PASSWORD, firstName, lastName, STUDENT_ROLE, "ACTIVE");
		iAccountRepository.save(account);

		String DoB = studentRequestDTO.getDoB();
		String gender = studentRequestDTO.getGender();
		String parentName = studentRequestDTO.getParentName();
		String parentPhone = studentRequestDTO.getParentPhone();
		studentProfile = new StudentProfile(DoB, gender, parentName, parentPhone, account, classes);
		iStudentProfileRepository.save(studentProfile);

		return "CREATE SUCCESS !";
	}

	public String changeStatus(ListIdAndStatusDTO listIdAndStatusDTO) {
		List<Long> ids = listIdAndStatusDTO.getIds();
		String status = listIdAndStatusDTO.getStatus();
		for (long accountId : ids) {
			Account account = iAccountRepository.findByIdAndStatusNot(accountId, DELETE_STATUS);
			if (account == null) {
				throw new ResourceNotFoundException();
			}
			Classes defaultClass = iClassRepository.findById(0L).orElseThrow(() -> new ResourceNotFoundException());
			StudentProfile studentProfile = account.getStudentProfile();
			studentProfile.setStatus(status);
			studentProfile.setClasses(defaultClass);
			studentProfile.setStudentCount(defaultClass.getStudentProfileList().size() + 1);

			if (status.equals(DELETE_STATUS)) {
				String username = "DEL" + String.format("%05d", defaultClass.getStudentProfileList().size() + 1);
				account.setUsername(username);
			}
			if (status.equals(PENDING_STATUS)) {
				String username = "PEND" + String.format("%05d", defaultClass.getStudentProfileList().size() + 1);
				account.setUsername(username);
			}
			account.setStatus(status);
		}
		
		return "CHANGE SUCCESS!";
	}

	private String generateUsername(String schoolCode, int gradeName, long studentCount) {
		String username = schoolCode + String.format("%02d", gradeName) + String.format("%03d", studentCount);
		return username;
	}

}
