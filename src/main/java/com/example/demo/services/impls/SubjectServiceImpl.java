package com.example.demo.services.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.SubjectResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.ProgressTest;
import com.example.demo.models.Subject;
import com.example.demo.models.Unit;
import com.example.demo.repositories.IGradeRepository;
import com.example.demo.repositories.IProgressTestRepository;
import com.example.demo.repositories.ISubjectRepository;
import com.example.demo.repositories.IUnitRepository;
import com.example.demo.services.IProgressTestService;
import com.example.demo.services.ISubjectService;
import com.example.demo.services.IUnitService;

@Service
public class SubjectServiceImpl implements ISubjectService {

	private final int DESCRIPTION_MAX_LENGTH = 50;
	private final int SUBJECTNAME_MAX_LENGTH = 20;
	@Autowired
	ISubjectRepository iSubjectRepository;

	@Autowired
	IGradeRepository iGradeRepository;

	@Autowired
	IUnitRepository iUnitRepository;

	@Autowired
	IUnitService iUnitService;

	@Autowired
	IProgressTestRepository iProgressTestRepository;

	@Autowired
	IProgressTestService iProgressTestService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	FirebaseService firebaseService;

	// done
	@Override
	public List<SubjectResponseDTO> findSubjectByGradeId(long gradeId) {
		// validate data input
		iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());

		// find all subjects and return
		List<Subject> subjectList = iSubjectRepository.findByGradeIdAndIsDisableFalse(gradeId);
		List<SubjectResponseDTO> subjectResponseDTOList = new ArrayList<>();
		if (!subjectList.isEmpty()) {
			for (Subject subject : subjectList) {
				subjectResponseDTOList.add(modelMapper.map(subject, SubjectResponseDTO.class));
			}
		}

		return subjectResponseDTOList;
	}

	// done
	@Override
	public SubjectResponseDTO findById(long id) {
		Subject subject = iSubjectRepository.findByIdAndIsDisableFalse(id);
		if (subject == null) {
			throw new ResourceNotFoundException();
		}
		SubjectResponseDTO subjectResponseDTO = modelMapper.map(subject, SubjectResponseDTO.class);

		return subjectResponseDTO;
	}

	@Override
	@Transactional
	public String deleteSubject(long id) {
		Subject subject = iSubjectRepository.findByIdAndIsDisableFalse(id);
		if (subject == null) {
			throw new ResourceNotFoundException();
		}

		List<Unit> listUnits = iUnitRepository.findBySubjectIdAndIsDisableOrderByUnitNameAsc(id, false);
		if (!listUnits.isEmpty()) {
			for (Unit unit : listUnits) {
				iUnitService.deleteUnit(unit.getId());
			}
		}
		List<ProgressTest> progresssTestList = iProgressTestRepository.findBySubjectId(id);
		if (!progresssTestList.isEmpty()) {
			for (ProgressTest progressTest : progresssTestList) {
				iProgressTestService.deleteProgressTest(progressTest.getId());
			}
		}

		subject.setDisable(true);
		iSubjectRepository.save(subject);
		return "DELETE SUCCESS !";
	}

	// done
	@Override
	public String createSubject(String subjectName, MultipartFile file, String description, long gradeId)
			throws SizeLimitExceededException, IOException {
		// validate data input
		String error = validateRequiredString(subjectName, SUBJECTNAME_MAX_LENGTH, "\nSubjectName is invalid!");
		error += validateRequiredFile(file, "image", "\nFile is invalid!", "\nNot supported this file type for image!");
		error += validateString(description, DESCRIPTION_MAX_LENGTH, "\nDescription is invalid!");
		if (!error.isEmpty()) {
			return error.trim();
		}
		iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());

		// find subjectName is existed in grade or not
		if (iSubjectRepository.findByGradeIdAndSubjectNameAndIsDisableFalse(gradeId, subjectName) != null) {
			return "EXISTED";
		}

		// save data and return
		Subject subject = new Subject(subjectName, gradeId, false, description);
		subject.setImageUrl(firebaseService.saveFile(file));
		iSubjectRepository.save(subject);

		return "CREATE SUCCESS!";
	}

	// done
	@Override
	public String updateSubject(long id, String subjectName, MultipartFile file, String description, long gradeId)
			throws SizeLimitExceededException, IOException {
		// validate data input
		Subject subject = iSubjectRepository.findByIdAndIsDisableFalse(id);
		if (subject == null) {
			throw new ResourceNotFoundException();
		}
		iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());
		String error = validateRequiredString(subjectName, SUBJECTNAME_MAX_LENGTH, "\nSubjectName is invalid!");
		error += validateFile(file, "image", "\nFile is invalid!", "\nNot supported this file type for image!");
		error += validateString(description, DESCRIPTION_MAX_LENGTH, "\nDescription is invalid!");
		if (!error.isEmpty()) {
			return error.trim();
		}

		// find subjectName is existed in grade or not
		if (!subject.getSubjectName().equals(subjectName)) {
			if (iSubjectRepository.findByGradeIdAndSubjectNameAndIsDisableFalse(gradeId, subjectName) != null) {
				return "EXISTED";
			}
		}

		// save data and return
		subject.setSubjectName(subjectName);
		if (file != null) {
			subject.setImageUrl(firebaseService.saveFile(file));
		}
		if (description != null) {
			subject.setDescription(description);
		}
		iSubjectRepository.save(subject);

		return "UPDATE SUCCESS!";
	}

	private String validateString(String property, int length, String errorMessage) {
		String error = "";
		if (property != null) {
			if (property.length() > length) {
				error = errorMessage;
			}
		}

		return error;
	}

	private String validateRequiredString(String property, int length, String errorMessage) {
		String error = "";
		if (property == null) {
			error = errorMessage;
		} else {
			if (property.isEmpty() || property.length() > length) {
				error = errorMessage;
			}
		}

		return error;
	}

	private String validateFile(MultipartFile file, String contentType, String errorMessage, String errorMessage2) {
		String error = "";
		if (file != null) {
			if (!file.getContentType().contains(contentType)) {
				error += errorMessage2;
			}
		}

		return error;
	}

	private String validateRequiredFile(MultipartFile file, String contentType, String errorMessage,
			String errorMessage2) {
		String error = "";
		if (file == null) {
			error = errorMessage;
		} else {
			if (file.isEmpty()) {
				error = errorMessage;
			} else if (!file.getContentType().contains(contentType)) {
				error += errorMessage2;
			}
		}

		return error;
	}

}
