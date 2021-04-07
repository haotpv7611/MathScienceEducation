package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.ProgressTestDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Exercise;
import com.example.demo.models.ProgressTest;
import com.example.demo.models.Subject;
import com.example.demo.repositories.IExerciseRepository;
import com.example.demo.repositories.IProgressTestRepository;
import com.example.demo.repositories.ISubjectRepository;
import com.example.demo.services.IExerciseService;
import com.example.demo.services.IProgressTestService;

@Service
public class ProgressTestServiceImpl implements IProgressTestService {

	@Autowired
	private IProgressTestRepository iProgressTestRepository;

	@Autowired
	private ISubjectRepository iSubjectRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IExerciseService iExerciseService;

	@Autowired
	private IExerciseRepository iExerciseRepository;

	@Override
	public List<ProgressTestDTO> findBySubjectId(long subjectId) {
		// check data input
		Subject subject = iSubjectRepository.findByIdAndIsDisableFalse(subjectId);
		if (subject == null) {
			throw new ResourceNotFoundException();
		}

		// 1. connect database through repository
		// 2. find all entities are not disable and have subjectId = ?
		List<ProgressTest> progressTestList = iProgressTestRepository.findBySubjectIdAndIsDisableFalse(subjectId);
		List<ProgressTestDTO> progressTestDTOList = new ArrayList<>();

		// 3. convert all entities to dtos
		// 4. add all dtos to newsDTOList and return
		if (!progressTestList.isEmpty()) {
			for (ProgressTest progressTest : progressTestList) {
				ProgressTestDTO progressTestDTO = modelMapper.map(progressTest, ProgressTestDTO.class);
				progressTestDTOList.add(progressTestDTO);
			}
		}

		return progressTestDTOList;
	}

//	@Override
//	public List<ProgressTestDTO> findBySubjectIdAndIsDisable(long subjectId) {
//		List<ProgressTest> progressTestLists = iProgressTestRepository.findBySubjectId(subjectId);
//		List<ProgressTestDTO> progressTestDTOLists = new ArrayList<>();
//		if (!progressTestLists.isEmpty()) {
//			for (ProgressTest progressTest : progressTestLists) {
//				ProgressTestDTO progressTestDTO = modelMapper.map(progressTest, ProgressTestDTO.class);
//				progressTestDTOLists.add(progressTestDTO);
//			}
//		}
//		return progressTestDTOLists;
//	}

	@Override
	public String createProgressTest(ProgressTestDTO progressTestDTO) {
		long subjectId = progressTestDTO.getId();
		String progressTestName = progressTestDTO.getProgressTestName();
		Subject subject = iSubjectRepository.findByIdAndIsDisableFalse(subjectId);
		if (subject == null) {
			throw new ResourceNotFoundException();
		}

		if (iProgressTestRepository.findBySubjectIdAndProgressTestNameAndIsDisableFalse(subjectId,
				progressTestName) != null) {
			return "EXISTED";
		}

//		List<ProgressTestDTO> listProgressTestLists = findBySubjectIdAndIsDisable(progressTestDTO.getSubjectId());
//		for (ProgressTestDTO progressTestDTO2 : listProgressTestLists) {
//			if (progressTestDTO.getProgressTestName().equalsIgnoreCase(progressTestDTO2.getProgressTestName())) {
//				return "Progress Tesst Name is existed !";
//			}
//		}
		ProgressTest progressTest = modelMapper.map(progressTestDTO, ProgressTest.class);
		progressTest.setDisable(false);
		iProgressTestRepository.save(progressTest);

		return "CREATE SUCCESSS!";
	}

	@Override
	public String updateProgressTest(long id, ProgressTestDTO progressTestDTO) {
		long subjectId = progressTestDTO.getId();
		String progressTestName = progressTestDTO.getProgressTestName();
		String description = progressTestDTO.getDescription();
		long unitAfterId = progressTestDTO.getUnitAfterId();
		ProgressTest progressTest = iProgressTestRepository.findByIdAndIsDisableFalse(id);
		if (progressTest == null) {
			throw new ResourceNotFoundException();
		}
		Subject subject = iSubjectRepository.findByIdAndIsDisableFalse(subjectId);
		if (subject == null) {
			throw new ResourceNotFoundException();
		}

		if (!progressTest.getProgressTestName().equalsIgnoreCase(progressTestName)) {
			if (iProgressTestRepository.findBySubjectIdAndProgressTestNameAndIsDisableFalse(subjectId,
					progressTestName) != null) {
				return "EXISTED";
			}

//			List<ProgressTestDTO> listProgressTestLists = findBySubjectIdAndIsDisable(progressTestDTO.getSubjectId());
//			for (ProgressTestDTO progressTestDTO2 : listProgressTestLists) {
//				if (progressTestDTO.getProgressTestName().equalsIgnoreCase(progressTestDTO2.getProgressTestName())) {
//					return "Progress Tesst Name is existed !";
//				}
//			}
		}
		progressTest.setProgressTestName(progressTestName);
		progressTest.setDescription(description);
		progressTest.setUnitAfterId(unitAfterId);
		iProgressTestRepository.save(progressTest);

		return "UPDATE SUCCESS!";
	}

//	@Override
//	public String deleteProgressTest(List<Long> ids) {
//		for (long id : ids) {
//			deleteOneProgressTest(id);
//		}
//		return "DELETE SUCCESS !";
//	}

	@Override
	@Transactional
	public void deleteOneProgressTest(long id) {
		ProgressTest progressTest = iProgressTestRepository.findByIdAndIsDisableFalse(id);
		if (progressTest == null) {
			throw new ResourceNotFoundException();
		}
		List<Exercise> exerciseList = iExerciseRepository.findByProgressTestIdAndIsDisableFalse(id);
		if (!exerciseList.isEmpty()) {
			for (Exercise exercise : exerciseList) {
				iExerciseService.deleteOneExercise(exercise.getId());
			}
		}
		progressTest.setDisable(true);
		iProgressTestRepository.save(progressTest);
	}

	@Override
	public ProgressTestDTO findById(long id) {
		ProgressTest progressTest = iProgressTestRepository.findByIdAndIsDisableFalse(id);
		if (progressTest == null) {
			throw new ResourceNotFoundException();
		}
		ProgressTestDTO progressTestDTO = modelMapper.map(progressTest, ProgressTestDTO.class);

		return progressTestDTO;
	}
}
