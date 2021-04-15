package com.example.demo.services.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.example.demo.utils.Util;

@Service
public class SubjectServiceImpl implements ISubjectService {
	Logger logger = LoggerFactory.getLogger(SubjectServiceImpl.class);

	private final int SUBJECT_NAME_MAX_LENGTH = 20;
	private final int DESCRIPTION_MAX_LENGTH = 50;
	
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

	// done ok
	@Override
	public Object findById(long id) {
		SubjectResponseDTO subjectResponseDTO = null;
		try {
			Subject subject = iSubjectRepository.findByIdAndIsDisableFalse(id);
			if (subject == null) {
				throw new ResourceNotFoundException();
			}
			subjectResponseDTO = modelMapper.map(subject, SubjectResponseDTO.class);
		} catch (Exception e) {
			logger.error("FIND: subjectId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "FIND FAIL!";
		}

		return subjectResponseDTO;
	}

	// done ok
	@Override
	public List<SubjectResponseDTO> findSubjectByGradeId(int gradeId) {
		List<SubjectResponseDTO> subjectResponseDTOList = new ArrayList<>();
		try {
			// find all subjects and return
			List<Subject> subjectList = iSubjectRepository.findByGradeIdAndIsDisableFalse(gradeId);
			if (!subjectList.isEmpty()) {
				for (Subject subject : subjectList) {
					subjectResponseDTOList.add(modelMapper.map(subject, SubjectResponseDTO.class));
				}
			}
		} catch (Exception e) {
			logger.error("FIND: all subject by gradeId = " + gradeId + "! " + e.getMessage());

			return null;
		}

		return subjectResponseDTOList;
	}
	
	@Override
	public Map<Long, String> findAllSubject(){
		Map<Long, String> subjectMap = new HashMap<>();
		List<Subject> subjectList = iSubjectRepository.findByIsDisableFalse();
		for (Subject subject : subjectList) {
			subjectMap.put(subject.getId(), subject.getSubjectName());
		}
		
		return subjectMap;
	}

	// done ok
	@Override
	@Transactional
	public String createSubject(String subjectName, MultipartFile file, String description, int gradeId)
			throws SizeLimitExceededException, IOException {
		try {
			// validate data input
			String error = Util.validateRequiredString(subjectName, SUBJECT_NAME_MAX_LENGTH,
					"\nSubjectName is invalid!");
			error += Util.validateRequiredFile(file, "image", "\nFile is invalid!",
					"\nNot supported this file type for image!");
			error += Util.validateString(description, DESCRIPTION_MAX_LENGTH, "\nDescription is invalid!");
			if (!error.isEmpty()) {
				return error.trim();
			}
			iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());

			// find subjectName is existed in grade or not
			if (iSubjectRepository.findByGradeIdAndSubjectNameIgnoreCaseAndIsDisableFalse(gradeId,
					subjectName) != null) {
				return "EXISTED";
			}

			// save data and return
			Subject subject = new Subject(subjectName, gradeId, false, description);
			subject.setImageUrl(firebaseService.saveFile(file));
			iSubjectRepository.save(subject);
		} catch (Exception e) {
			logger.error("CREATE: subjectName = " + subjectName + " in gradeId =  " + gradeId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CREATE FAIL!";
		}

		return "CREATE SUCCESS!";
	}

	// done ok
	@Override
	@Transactional
	public String updateSubject(long id, String subjectName, MultipartFile file, String description, int gradeId)
			throws SizeLimitExceededException, IOException {
		try {
			// validate data input
			Subject subject = iSubjectRepository.findByIdAndIsDisableFalse(id);
			if (subject == null) {
				throw new ResourceNotFoundException();
			}
			iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());
			String error = Util.validateRequiredString(subjectName, SUBJECT_NAME_MAX_LENGTH,
					"\nSubjectName is invalid!");
			error += Util.validateFile(file, "image", "\nNot supported this file type for image!");
			error += Util.validateString(description, DESCRIPTION_MAX_LENGTH, "\nDescription is invalid!");
			if (!error.isEmpty()) {
				return error.trim();
			}

			// find subjectName is existed in grade or not
			if (!subject.getSubjectName().equals(subjectName)) {
				if (iSubjectRepository.findByGradeIdAndSubjectNameIgnoreCaseAndIsDisableFalse(gradeId,
						subjectName) != null) {

					return "EXISTED";
				}
			}

			// save data and return
			String subjectImageUrl = subject.getImageUrl();
			subject.setSubjectName(subjectName);
			if (file != null) {
				firebaseService.deleteFile(subjectImageUrl);
				subject.setImageUrl(firebaseService.saveFile(file));
			}
			if (description != null) {
				subject.setDescription(description);
			}
			iSubjectRepository.save(subject);
		} catch (Exception e) {
			logger.error("UPDATE: subjectId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "UPDATE FAIL!";
		}

		return "UPDATE SUCCESS!";
	}

	// done ok
	@Override
	@Transactional
	public String deleteSubject(long id) {
		try {
			// validate subjectId
			Subject subject = iSubjectRepository.findByIdAndIsDisableFalse(id);
			if (subject == null) {
				throw new ResourceNotFoundException();
			}

			List<Unit> listUnitList = iUnitRepository.findBySubjectIdAndIsDisableFalse(id);
			if (!listUnitList.isEmpty()) {
				for (Unit unit : listUnitList) {
					iUnitService.deleteUnit(unit.getId());
				}
			}
			List<ProgressTest> progresssTestList = iProgressTestRepository.findBySubjectIdAndIsDisableFalse(id);
			if (!progresssTestList.isEmpty()) {
				for (ProgressTest progressTest : progresssTestList) {
					iProgressTestService.deleteOneProgressTest(progressTest.getId());
				}
			}

			String subjectImageUrl = subject.getImageUrl();
			subject.setDisable(true);
			subject.setImageUrl("DELETED");
			iSubjectRepository.save(subject);
			if (!subjectImageUrl.isEmpty()) {
				firebaseService.deleteFile(subjectImageUrl);
			}
		} catch (Exception e) {
			logger.error("DELETE: subjectId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "DELETE FAIL!";
		}

		return "DELETE SUCCESS!";
	}

}
