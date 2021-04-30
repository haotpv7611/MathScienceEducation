package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.ClassChangeDTO;
import com.example.demo.dtos.ClassRequestDTO;
import com.example.demo.dtos.ClassResponseDTO;
import com.example.demo.dtos.GradeClassDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.SchoolGradeDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Classes;
import com.example.demo.models.SchoolGrade;
import com.example.demo.models.StudentProfile;
import com.example.demo.repositories.IClassRepository;
import com.example.demo.repositories.ISchoolGradeRepository;
import com.example.demo.repositories.IStudentProfileRepository;
import com.example.demo.services.IClassService;
import com.example.demo.services.IStudentProfileService;

@Service
public class ClassServiceImpl implements IClassService {
	Logger logger = LoggerFactory.getLogger(ClassServiceImpl.class);
	private final String ACTIVE_STATUS = "ACTIVE";
	private final String PENDING_STATUS = "PENDING";
	private final String DELETED_STATUS = "DELETED";

	@Autowired
	private ISchoolGradeRepository iSchoolGradeRepository;

	@Autowired
	private IClassRepository iClassRepository;

	@Autowired
	private IStudentProfileRepository iStudentProfileRepository;

	@Autowired
	private IStudentProfileService iStudentProfileService;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<ClassResponseDTO> findBySchoolGradeId(SchoolGradeDTO schoolGradeDTO) {
		int gradeId = schoolGradeDTO.getGradeId();
		long schoolId = schoolGradeDTO.getSchoolId();
		List<ClassResponseDTO> classResponseDTOList = new ArrayList<>();
		try {
			SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
					"DELETED");
			if (schoolGrade != null) {
				List<Classes> classList = iClassRepository.findBySchoolGradeIdAndStatusNotOrderByStatusAscClassNameAsc(
						schoolGrade.getId(), DELETED_STATUS);
				if (!classList.isEmpty()) {
					for (Classes classes : classList) {
						ClassResponseDTO classResponseDTO = modelMapper.map(classes, ClassResponseDTO.class);
						classResponseDTOList.add(classResponseDTO);
					}
				}
			}
		} catch (Exception e) {
			logger.error(
					"FIND: all class by schoolId = " + schoolId + "and gradeId = " + gradeId + "! " + e.getMessage());

			return null;
		}

		return classResponseDTOList;
	}

	// get all grade linked by gradeName
	// get all class by gradeId
	@Override
	public List<GradeClassDTO> findGradeClassBySchoolId(long schoolId, int gradeId, long classesId) {
		List<GradeClassDTO> gradeClassDTOList = new ArrayList<>();
		try {
			if (classesId == 0) {
				SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
						DELETED_STATUS);
				if (schoolGrade != null) {
					findClassInSchoolGrade(schoolGrade, gradeClassDTOList);
				}
			} else {
				Classes classes = iClassRepository.findByIdAndStatusNot(classesId, DELETED_STATUS);
				if (classes != null) {
					String className = classes.getClassName();
					if (className.equalsIgnoreCase(PENDING_STATUS)) {
						List<SchoolGrade> schoolGradeList = iSchoolGradeRepository.findBySchoolIdAndStatusNot(schoolId,
								DELETED_STATUS);
						if (!schoolGradeList.isEmpty()) {
							for (SchoolGrade schoolGrade : schoolGradeList) {
//								List<ClassChangeDTO> classChangeDTOList = new ArrayList<>();
//								// lấy class active và inactive
//								List<Classes> classesList = new ArrayList<>();
//
//								if (iClassRepository.findBySchoolGradeIdAndStatusOrderByClassName(schoolGrade.getId(),
//										ACTIVE_STATUS).size() > 0) {
//									classesList.addAll(iClassRepository.findBySchoolGradeIdAndStatusOrderByClassName(
//											schoolGrade.getId(), ACTIVE_STATUS));
//								}
//
//								if (!classesList.isEmpty()) {
//									for (Classes classes : classesList) {
//										ClassChangeDTO classChangeDTO = new ClassChangeDTO(classes.getId(),
//												classes.getClassName());
//										classChangeDTOList.add(classChangeDTO);
//									}
//								}
//								GradeClassDTO gradeClassDTO = new GradeClassDTO(schoolGrade.getGrade().getId(),
//										schoolGrade.getGrade().getGradeName(), classChangeDTOList);
//								gradeClassDTOList.add(gradeClassDTO);
								findClassInSchoolGrade(schoolGrade, gradeClassDTOList);
							}

							if (!gradeClassDTOList.isEmpty()) {
								gradeClassDTOList.sort(Comparator.comparing(GradeClassDTO::getName));
							}
						}
					} else {
						SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId,
								schoolId, DELETED_STATUS);
						if (schoolGrade != null) {
							findClassInSchoolGrade(schoolGrade, gradeClassDTOList);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("FIND: all grade and class by schoolId = " + schoolId + "! " + e.getMessage());

			return null;
		}

		return gradeClassDTOList;
	}

	private List<GradeClassDTO> findClassInSchoolGrade(SchoolGrade schoolGrade, List<GradeClassDTO> gradeClassDTOList) {

		List<ClassChangeDTO> classChangeDTOList = new ArrayList<>();
		List<Classes> classesList = iClassRepository.findBySchoolGradeIdAndStatusOrderByClassName(schoolGrade.getId(),
				ACTIVE_STATUS);
		if (!classesList.isEmpty()) {
			for (Classes classes : classesList) {
				ClassChangeDTO classChangeDTO = new ClassChangeDTO(classes.getId(), classes.getClassName());
				classChangeDTOList.add(classChangeDTO);
			}
		}
		GradeClassDTO gradeClassDTO = new GradeClassDTO(schoolGrade.getGrade().getId(),
				schoolGrade.getGrade().getGradeName(), classChangeDTOList);
		gradeClassDTOList.add(gradeClassDTO);

		return gradeClassDTOList;

	}

	@Override
	public Map<Long, String> findAllClass() {
		Map<Long, String> classesMap = new HashMap<>();
		try {

			List<Classes> classesList = iClassRepository.findByStatusNot(DELETED_STATUS);
			for (Classes classes : classesList) {
				classesMap.put(classes.getId(), classes.getClassName());
			}
		} catch (Exception e) {
			logger.error("FIND: all class! " + e.getMessage());

			return null;
		}

		return classesMap;
	}

	@Override
	public String createClass(ClassRequestDTO classRequestDTO) {
		int gradeId = classRequestDTO.getGradeId();
		long schoolId = classRequestDTO.getSchoolId();
		String className = classRequestDTO.getClassName();
		try {

			SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
					DELETED_STATUS);
			if (schoolGrade == null) {
				throw new ResourceNotFoundException();
			}

			if (iClassRepository.findBySchoolGradeIdAndClassNameIgnoreCaseAndStatusNot(schoolGrade.getId(),
					classRequestDTO.getClassName(), DELETED_STATUS) != null) {

				return "EXISTED";
			}

			Classes classes = new Classes();
			classes.setSchoolGrade(schoolGrade);
			classes.setClassName(className);
			classes.setStatus(ACTIVE_STATUS);
			iClassRepository.save(classes);
		} catch (Exception e) {
			logger.error("CREATE: className = " + className + " in schoolId =  " + schoolId + "and gradeId = " + gradeId
					+ "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CREATE FAIL!";
		}

		return "CREATE SUCCESS!";
	}

	// check className existed
	@Override
	public String updateClass(long id, ClassRequestDTO classRequestDTO) {
		try {
			Classes classes = iClassRepository.findByIdAndStatusNot(id, DELETED_STATUS);
			if (classes == null) {
				throw new ResourceNotFoundException();
			}

			SchoolGrade schoolGrade = classes.getSchoolGrade();
			if (!classes.getClassName().equalsIgnoreCase(classRequestDTO.getClassName())) {
				if (iClassRepository.findBySchoolGradeIdAndClassNameIgnoreCaseAndStatusNot(schoolGrade.getId(),
						classRequestDTO.getClassName(), DELETED_STATUS) != null) {

					return "EXISTED";
				}
			}

			classes.setClassName(classRequestDTO.getClassName());
			iClassRepository.save(classes);
		} catch (Exception e) {
			logger.error("UPDATE: classesId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "UPDATE FAIL!";
		}

		return "UPDATE SUCCESS!";
	}

	@Override
	@Transactional
	public String changeStatusClass(ListIdAndStatusDTO listIdAndStatusDTO) {
		List<Long> ids = listIdAndStatusDTO.getIds();
		String status = listIdAndStatusDTO.getStatus();

		// 1. connect database through repository
		// 2. find entity by id
		// 3. if not existed throw exception
		for (long id : ids) {
			try {
				String response = changeStatusOneClass(id, status);
				if (!response.equalsIgnoreCase("OK")) {

					return response;
				}
			} catch (Exception e) {
				logger.error("Change status: list classId = " + ids.toString() + "! " + e.getMessage());
				throw e;
			}
		}

		return "CHANGE SUCCESS!";
	}

	// change status all student in class --> change status class
	// class pending only delete
	@Override
	@Transactional
	public String changeStatusOneClass(long id, String status) {
		try {
			Classes classes = iClassRepository.findByIdAndStatusNot(id, DELETED_STATUS);
			if (classes == null) {
				throw new ResourceNotFoundException();
			}

			if (status.equalsIgnoreCase(DELETED_STATUS)) {
				List<StudentProfile> studentProfileList = iStudentProfileRepository.findByClassesIdAndStatusNot(id,
						DELETED_STATUS);
				if (!studentProfileList.isEmpty()) {
					for (StudentProfile studentProfile : studentProfileList) {
						String response = iStudentProfileService.changeStatusOneStudent(studentProfile.getId(), status);
						if (!response.equalsIgnoreCase("OK")) {

							return response;
						}
					}
				}
				classes.setStatus(status);
			} else {
				if (!classes.getClassName().equalsIgnoreCase(PENDING_STATUS)) {
					List<StudentProfile> studentProfileList = iStudentProfileRepository.findByClassesIdAndStatusNot(id,
							DELETED_STATUS);
					if (!studentProfileList.isEmpty()) {
						for (StudentProfile studentProfile : studentProfileList) {
							iStudentProfileService.changeStatusOneStudent(studentProfile.getId(), status);
						}
					}
					classes.setStatus(status);
				}
			}
			iClassRepository.save(classes);
		} catch (Exception e) {
			logger.error("Change status: one classesId = " + id + "! " + e.getMessage());
			throw e;
		}

		return "CHANGE SUCCESS!";
	}
}
