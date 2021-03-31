package com.example.demo.services.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.SubjectDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Subject;
import com.example.demo.models.Unit;
import com.example.demo.repositories.ISubjectRepository;
import com.example.demo.repositories.IUnitRepository;
import com.example.demo.services.ISubjectService;
import com.example.demo.services.IUnitService;

@Service
public class SubjectServiceImpl implements ISubjectService {

	private final int DESCRIPTION_MAX_LENGTH = 50;
	private final int SUBJECTNAME_MAX_LENGTH = 20;
	@Autowired
	ISubjectRepository iSubjectRepository;

	@Autowired
	IUnitRepository iUnitRepository;

	@Autowired
	IUnitService iUnitService;

	@Autowired
	ModelMapper modelMapper;
	@Autowired
	FirebaseService firebaseService;

	@Override
	public List<SubjectDTO> findSubjectByGradeId(long gradeId) {
		List<Subject> subjectList = iSubjectRepository.findByGradeIdAndIsDisable(gradeId, false);
		List<SubjectDTO> subjectDTOList = new ArrayList<>();

		if (!subjectList.isEmpty()) {
			for (Subject subject : subjectList) {
				subjectDTOList.add(modelMapper.map(subject, SubjectDTO.class));
			}
		}

		return subjectDTOList;
	}

	@Override
	public Subject findByIdAndIsDisable(long id, boolean isDisable) {
		Subject subject = iSubjectRepository.findByIdAndIsDisable(id, false);
		if (subject == null) {
			throw new ResourceNotFoundException();
		}
		return subject;
	}

	@Override
	public String deleteSubject(long id) {
		Subject subject = iSubjectRepository.findByIdAndIsDisable(id, false);
		if (subject == null) {
			throw new ResourceNotFoundException();
		}
		subject.setDisable(true);
		List<Unit> listUnits = iUnitRepository.findBySubjectIdAndIsDisableOrderByUnitNameAsc(id, false);
		if (!listUnits.isEmpty()) {
			for (Unit unit : listUnits) {
				iUnitService.deleteUnit(unit.getId());
			}
		}
		iSubjectRepository.save(subject);
		return "DELETE SUCCESS !";
	}

	@Override
	public String createSubject(String subjectName, MultipartFile multipartFile, String description, long gradeId)
			throws SizeLimitExceededException, IOException {
		// validate
		String error = "";
		if (subjectName.isEmpty() && subjectName.length() > SUBJECTNAME_MAX_LENGTH) {
			error += "Subject Name is invalid !";
		}
		if (multipartFile.isEmpty()) {
			error += "\n File is invalid !";
		} else if (!multipartFile.getContentType().contains("image")) {
			error += "\n Not supported this file type for image!";
		}
		if (description.length() > DESCRIPTION_MAX_LENGTH) {
			error += "Description is invalid !";
		}
		// find subjectName is existed in grade or not
		List<Subject> listSubjects = iSubjectRepository.findByGradeIdAndIsDisable(gradeId, false);
		for (Subject subject : listSubjects) {
			if (subject.getSubjectName().equalsIgnoreCase(subjectName)) {
				return "\n Subject is existed !";
			}
		}
		if (!error.isEmpty()) {
			return error.trim();
		}

		Subject subject = new Subject();
		subject.setSubjectName(subjectName);
		subject.setImageUrl(firebaseService.saveFile(multipartFile));
		subject.setDescription(description);
		subject.setGradeId(gradeId);
		subject.setDisable(false);

		iSubjectRepository.save(subject);

		return "CRETAE SUCCESS !";
	}

	@Override
	public String updateSubject(long id, String subjectName, MultipartFile multipartFile, String description,
			long gradeId) throws SizeLimitExceededException, IOException {
		String error = "";
		// find subject is existed or not
		Subject subject = iSubjectRepository.findByIdAndIsDisable(id, false);
		if (subject == null) {
			throw new ResourceNotFoundException();
		}

		// validate
		if (subjectName.isEmpty() && subjectName.length() > SUBJECTNAME_MAX_LENGTH) {
			error += "Subject Name is invalid !";
		}
		if (multipartFile.isEmpty()) {
			error += "\n File is invalid !";
		} else if (!multipartFile.getContentType().contains("image")) {
			error += "\n Not supported this file type for image!";
		}
		if (description.length() > DESCRIPTION_MAX_LENGTH) {
			error += "Description is invalid !";
		}
		// find subjectName is existed in grade or not
		List<Subject> listSubjects = iSubjectRepository.findByGradeIdAndIsDisable(gradeId, false);
		for (Subject subject1 : listSubjects) {
			if (subjectName.equalsIgnoreCase(subject1.getSubjectName())) {
				return "\n Subject is existed !";
			}
		}
		if (!error.isEmpty()) {
			return error.trim();
		}

		subject.setSubjectName(subjectName);
		subject.setImageUrl(firebaseService.saveFile(multipartFile));
		subject.setDescription(description);

		iSubjectRepository.save(subject);

		return "UPDATE SUCCESS !";
	}

}
