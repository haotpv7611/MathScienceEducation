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
import com.example.demo.services.IFirebaseService;
import com.example.demo.services.IProgressTestService;
import com.example.demo.services.ISubjectService;
import com.example.demo.services.IUnitService;

@Service
public class SubjectServiceImpl implements ISubjectService {
	Logger logger = LoggerFactory.getLogger(SubjectServiceImpl.class);

	@Autowired
	private ISubjectRepository iSubjectRepository;

	@Autowired
	private IGradeRepository iGradeRepository;

	@Autowired
	private IUnitRepository iUnitRepository;

	@Autowired
	private IUnitService iUnitService;

	@Autowired
	private IProgressTestRepository iProgressTestRepository;

	@Autowired
	private IProgressTestService iProgressTestService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IFirebaseService iFirebaseService;

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
			logger.error("Find subject by id = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "FIND FAIL!";
		}

		return subjectResponseDTO;
	}

	@Override
	public List<SubjectResponseDTO> findSubjectsByGradeId(int gradeId) {
		List<SubjectResponseDTO> subjectResponseDTOList = new ArrayList<>();
		try {
			// find all subjects and return
			List<Subject> subjectList = iSubjectRepository.findByGradeIdAndIsDisableFalse(gradeId);
			if (!subjectList.isEmpty()) {
				for (Subject subject : subjectList) {
					SubjectResponseDTO subjectResponseDTO = modelMapper.map(subject, SubjectResponseDTO.class);
					subjectResponseDTOList.add(subjectResponseDTO);
				}
			}
		} catch (Exception e) {
			logger.error("Find all subjects by gradeId = " + gradeId + "! " + e.getMessage());

			return null;
		}

		return subjectResponseDTOList;
	}

	@Override
	public Map<Long, String> findAllSubject() {
		Map<Long, String> subjectMap = new HashMap<>();
		try {
			List<Subject> subjectList = iSubjectRepository.findByIsDisableFalse();
			for (Subject subject : subjectList) {
				subjectMap.put(subject.getId(), subject.getSubjectName());
			}
		} catch (Exception e) {
			logger.error("Find all subjects! " + e.getMessage());

			return null;
		}

		return subjectMap;
	}

	// done ok
	@Override
	@Transactional
	public String createSubject(String subjectName, MultipartFile file, String description, int gradeId)
			throws SizeLimitExceededException, IOException {
		try {
			// check subjectName existed in grade
			iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());
			if (iSubjectRepository.findByGradeIdAndSubjectNameIgnoreCaseAndIsDisableFalse(gradeId,
					subjectName.trim()) != null) {

				return "EXISTED";
			}

			// save data and return
			Subject subject = new Subject(subjectName, gradeId, false, description);
			subject.setImageUrl(iFirebaseService.uploadFile(file));
			iSubjectRepository.save(subject);
		} catch (Exception e) {
			logger.error(
					"Create subject with name= " + subjectName + ", in gradeId =  " + gradeId + "! " + e.getMessage());
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

			// check subjectName is existed in grade
			if (!subject.getSubjectName().equals(subjectName)) {
				if (iSubjectRepository.findByGradeIdAndSubjectNameIgnoreCaseAndIsDisableFalse(gradeId,
						subjectName) != null) {

					return "EXISTED";
				}
			}

			// save data and return
			subject.setSubjectName(subjectName);
			String subjectImageUrl = subject.getImageUrl();
			if (file != null) {
				if (!file.isEmpty()) {
					subject.setImageUrl(iFirebaseService.uploadFile(file));
				}
				if (subjectImageUrl != null) {
					if (!subjectImageUrl.isEmpty()) {
						iFirebaseService.deleteFile(subjectImageUrl);
					}
				}
			}
			if (description != null) {
				subject.setDescription(description);
			}
			iSubjectRepository.save(subject);

		} catch (Exception e) {
			logger.error("Update subject with id = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "UPDATE FAIL!";
		}

		return "UPDATE SUCCESS!";
	}

	// delete progressTest --> unit --> delete subject
	@Override
	@Transactional
	public String deleteSubject(long id) {
		try {
			// validate subjectId
			Subject subject = iSubjectRepository.findByIdAndIsDisableFalse(id);
			if (subject == null) {
				throw new ResourceNotFoundException();
			}

			// delete all unit and progressTest
			List<ProgressTest> progresssTestList = iProgressTestRepository.findBySubjectIdAndIsDisableFalse(id);
			if (!progresssTestList.isEmpty()) {
				for (ProgressTest progressTest : progresssTestList) {
					iProgressTestService.deleteOneProgressTest(progressTest.getId());
				}
			}
			List<Unit> listUnitList = iUnitRepository.findBySubjectIdAndIsDisableFalse(id);
			if (!listUnitList.isEmpty()) {
				for (Unit unit : listUnitList) {
					iUnitService.deleteOneUnit(unit.getId());
				}
			}

			String subjectImageUrl = subject.getImageUrl();
			subject.setDisable(true);
			subject.setImageUrl("DELETED");
			iSubjectRepository.save(subject);
			iFirebaseService.deleteFile(subjectImageUrl);
		} catch (Exception e) {
			logger.error("Delete subject with id = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "DELETE FAIL!";
		}

		return "DELETE SUCCESS!";
	}

}
