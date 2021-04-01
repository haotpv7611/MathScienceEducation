package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.ListIdAndStatusDTO;
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

	@Override
	public List<StudentProfile> findBySchoolId(ListIdAndStatusDTO listIdAndStatusDTO) {
		long schoolId = listIdAndStatusDTO.getIds().get(0);
		long gradeId = listIdAndStatusDTO.getIds().get(1);
		long classId = listIdAndStatusDTO.getIds().get(2);

		List<StudentProfile> studentProfileList = new ArrayList<>();

		if (gradeId == 0 && classId == 0) {
			List<SchoolGrade> schoolGradeList = iSchoolGradeRepository.findBySchoolIdAndStatusNot(schoolId,
					DELETE_STATUS);
			if (!schoolGradeList.isEmpty()) {
				for (SchoolGrade schoolGrade : schoolGradeList) {
					List<Classes> classesList = new ArrayList<>();
					classesList.addAll(iClassRepository.findBySchoolGradeIdAndStatusNotOrderByStatusAscClassNameAsc(
							schoolGrade.getId(), DELETE_STATUS));
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

				List<Classes> classesList = iClassRepository
						.findBySchoolGradeIdAndStatusNotOrderByStatusAscClassNameAsc(schoolGrade.getId(),
								DELETE_STATUS);
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

		return null;
	}

	private void addStudentProfile(Classes classes, List<StudentProfile> studentProfileList) {
		studentProfileList.addAll(iStudentProfileRepository.findByClassIdAndStatusNot(classes.getId(), DELETE_STATUS));
	}
}
