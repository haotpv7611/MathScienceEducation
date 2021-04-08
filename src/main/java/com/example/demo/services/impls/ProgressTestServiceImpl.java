package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.ProgressTestDTO;
import com.example.demo.dtos.ProgressTestRequestDTO;
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
	Logger logger = LoggerFactory.getLogger(OptionQuestionServiceImpl.class);

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

	@Override
	public ProgressTestDTO findById(long id) {
		ProgressTest progressTest = iProgressTestRepository.findByIdAndIsDisableFalse(id);
		if (progressTest == null) {
			throw new ResourceNotFoundException();
		}
		ProgressTestDTO progressTestDTO = modelMapper.map(progressTest, ProgressTestDTO.class);

		return progressTestDTO;
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
	public String createProgressTest(ProgressTestRequestDTO progressTestRequestDTO) {
		long subjectId = progressTestRequestDTO.getSubjectId();
		String progressTestName = progressTestRequestDTO.getProgressTestName();

		try {
			// validate subjectId and check progressTestName existed
			Subject subject = iSubjectRepository.findByIdAndIsDisableFalse(subjectId);
			if (subject == null) {
				throw new ResourceNotFoundException();
			}
			if (iProgressTestRepository.findBySubjectIdAndProgressTestNameAndIsDisableFalse(subjectId,
					progressTestName) != null) {

				return "EXISTED";
			}

			ProgressTest progressTest = modelMapper.map(progressTestRequestDTO, ProgressTest.class);
			progressTest.setDisable(false);
			iProgressTestRepository.save(progressTest);
		} catch (Exception e) {
			logger.error(e.getMessage());

			return "CREATE FAIL!";
		}

		return "CREATE SUCCESSS!";
	}

	@Override
	public String updateProgressTest(long id, ProgressTestRequestDTO progressTestRequestDTO) {
		long subjectId = progressTestRequestDTO.getSubjectId();
		String progressTestName = progressTestRequestDTO.getProgressTestName();
		String description = progressTestRequestDTO.getDescription();
		long unitAfterId = progressTestRequestDTO.getUnitAfterId();

		try {
			// validate progressTestId, subjectId and check getProgressTestName existed
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
			}
			progressTest.setProgressTestName(progressTestName);
			progressTest.setDescription(description);
			progressTest.setUnitAfterId(unitAfterId);
			iProgressTestRepository.save(progressTest);
		} catch (Exception e) {
			logger.error(e.getMessage());

			return "UPDATE FAIL!";
		}

		return "UPDATE SUCCESS!";
	}

	@Override
	public String deleteProgressTest(long id) {
		try {
			deleteOneProgressTest(id);
		} catch (Exception e) {
			logger.error(e.getMessage());

			return "DELETE FAIL!";
		}

		return "DELETE SUCCESS!";
	}

	@Override
	@Transactional
	public void deleteOneProgressTest(long id) {
		try {
			// validate progressTestId
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
		} catch (Exception e) {
			throw e;
		}
	}

}
