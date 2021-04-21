package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
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
import com.example.demo.models.Grade;
import com.example.demo.models.SchoolGrade;
import com.example.demo.repositories.IClassRepository;
import com.example.demo.repositories.ISchoolGradeRepository;
import com.example.demo.services.IClassService;

@Service
public class ClassServiceImpl implements IClassService {
	private final String DELETED_STATUS = "DELETED";

	@Autowired
	ISchoolGradeRepository iSchoolGradeRepository;

	@Autowired
	IClassRepository iClassRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public List<ClassResponseDTO> findBySchoolGradeId(SchoolGradeDTO schoolGradeDTO) {
		int gradeId = schoolGradeDTO.getGradeId();
		long schoolId = schoolGradeDTO.getSchoolId();
		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
				"DELETED");
		if (schoolGrade == null) {
			throw new ResourceNotFoundException();
		}
		List<ClassResponseDTO> classResponseDTOList = new ArrayList<>();
		List<Classes> classList = iClassRepository
				.findBySchoolGradeIdAndStatusNotOrderByStatusAscClassNameAsc(schoolGrade.getId(), "DELETED");
		if (!classList.isEmpty()) {
			for (Classes classes : classList) {
				classResponseDTOList.add(modelMapper.map(classes, ClassResponseDTO.class));
			}
		}

		return classResponseDTOList;
	}

	@Override
	public List<GradeClassDTO> findGradeClassBySchoolId(long schoolId) {
		List<GradeClassDTO> gradeClassDTOList = new ArrayList<>();

		try {
			List<SchoolGrade> schoolGradeList = iSchoolGradeRepository.findBySchoolIdAndStatusNot(schoolId,
					DELETED_STATUS);
			System.out.println("schoolGrade = " + schoolGradeList.size());
			if (!schoolGradeList.isEmpty()) {
				List<Grade> gradeList = new ArrayList<>();
				for (SchoolGrade schoolGrade : schoolGradeList) {
					gradeList.add(schoolGrade.getGrade());
				}
				System.out.println("Grade = " + gradeList.size());

				if (!gradeList.isEmpty()) {
					for (Grade grade : gradeList) {
						List<ClassChangeDTO> classChangeDTOList = new ArrayList<>();
						List<Classes> classesList = iClassRepository
								.findBySchoolGradeIdAndStatusNotOrderByStatusAscClassNameAsc(grade.getId(),
										DELETED_STATUS);

						System.out.println("Class = " + classesList.size());
						if (!classesList.isEmpty()) {
							for (Classes classes : classesList) {
								ClassChangeDTO classChangeDTO = new ClassChangeDTO(classes.getId(),
										classes.getClassName());
								classChangeDTOList.add(classChangeDTO);
							}
						}
						System.out.println("ClassDTO = " + classChangeDTOList.size());
						GradeClassDTO gradeClassDTO = new GradeClassDTO(grade.getId(), grade.getGradeName(),
								classChangeDTOList);
						gradeClassDTOList.add(gradeClassDTO);
					}
				}
				
				System.out.println("total = " + gradeClassDTOList.size());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return gradeClassDTOList;
	}

	@Override
	public Map<Long, String> findAllClass() {
		Map<Long, String> classesMap = new HashMap<>();
		List<Classes> classesList = iClassRepository.findByStatusNot(DELETED_STATUS);
		for (Classes classes : classesList) {
			classesMap.put(classes.getId(), classes.getClassName());
		}

		return classesMap;
	}

	@Override
	public String createClass(ClassRequestDTO classRequestDTO) {
		int gradeId = classRequestDTO.getGradeId();
		long schoolId = classRequestDTO.getSchoolId();
		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
				"DELETED");
		if (schoolGrade == null) {
			throw new ResourceNotFoundException();
		}
		List<Classes> classesList = iClassRepository.findBySchoolGradeIdAndStatusNot(schoolGrade.getId(),
				DELETED_STATUS);

		String className = classRequestDTO.getClassName();
		if (!classesList.isEmpty()) {
			for (Classes classes : classesList) {
				if (!classes.getClassName(). equalsIgnoreCase(className)) {
					
					return "EXISTED";
				}
			}
		}

		Classes classes = new Classes();
		classes.setSchoolGrade(schoolGrade);
		classes.setClassName(className);
		classes.setStatus("ACTIVE");
		iClassRepository.save(classes);

		return "CREATE SUCCESS!";
	}

	@Override
	public String updateClass(long id, ClassRequestDTO classRequestDTO) {
		Classes classes = iClassRepository.findByIdAndStatusNot(id, DELETED_STATUS);
		if (classes == null) {
			throw new ResourceNotFoundException();
		}

		SchoolGrade schoolGrade = classes.getSchoolGrade();
		if (!classes.getClassName().equalsIgnoreCase(classRequestDTO.getClassName())) {
			if (iClassRepository.findBySchoolGradeIdAndClassNameIgnoreCaseAndStatusNot(schoolGrade.getId(),
					classRequestDTO.getClassName(), DELETED_STATUS) != null)

				return "EXISTED";
		}

		classes.setClassName(classRequestDTO.getClassName());
		classes.setStatus("ACTIVE");
		iClassRepository.save(classes);

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
			Classes classes = iClassRepository.findByIdAndStatusNot(id, "DELETED");
			if (classes == null) {
				throw new ResourceNotFoundException();
			}

			// 4. update entity with isDisable = true
			classes.setStatus(status);
			iClassRepository.save(classes);
		}

		return "CHANGE SUCCESS!";
	}

}
