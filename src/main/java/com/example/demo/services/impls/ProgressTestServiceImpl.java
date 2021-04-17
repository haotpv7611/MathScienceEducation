package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.ProgressTestResponseDTO;
import com.example.demo.dtos.IdAndStatusDTO;
import com.example.demo.dtos.ProgressTestRequestDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Exercise;
import com.example.demo.models.ProgressTest;
import com.example.demo.models.Subject;
import com.example.demo.models.Unit;
import com.example.demo.repositories.IExerciseRepository;
import com.example.demo.repositories.IProgressTestRepository;
import com.example.demo.repositories.ISubjectRepository;
import com.example.demo.repositories.IUnitRepository;
import com.example.demo.services.IExerciseService;
import com.example.demo.services.IProgressTestService;

@Service
public class ProgressTestServiceImpl implements IProgressTestService {
	Logger logger = LoggerFactory.getLogger(ProgressTestServiceImpl.class);
	private final String DELETED_STATUS = "DELETED";

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

	@Autowired
	private IUnitRepository iUnitRepository;

	@Override
	public Object findById(long id) {
		ProgressTestResponseDTO progressTestResponseDTO = null;
		try {
			ProgressTest progressTest = iProgressTestRepository.findByIdAndIsDisableFalse(id);
			if (progressTest == null) {
				throw new ResourceNotFoundException();
			}
			progressTestResponseDTO = modelMapper.map(progressTest, ProgressTestResponseDTO.class);
		} catch (Exception e) {
			logger.error("Find progressTest by id = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "FIND FAIL!";
		}

		return progressTestResponseDTO;
	}

	@Override
	public List<ProgressTestResponseDTO> findBySubjectId(long subjectId) {
		List<ProgressTestResponseDTO> progressTestResponseDTOList = new ArrayList<>();
		try {
			List<ProgressTest> progressTestList = iProgressTestRepository.findBySubjectIdAndIsDisableFalse(subjectId);
			if (!progressTestList.isEmpty()) {
				for (ProgressTest progressTest : progressTestList) {
					long unitId = progressTest.getUnitAfterId();
					Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);

					ProgressTestResponseDTO progressTestDTO = modelMapper.map(progressTest,
							ProgressTestResponseDTO.class);
					progressTestDTO.setUnitAfterName(unit.getUnitName());
					progressTestResponseDTOList.add(progressTestDTO);
				}
			}
		} catch (Exception e) {
			logger.error("Find all progressTests by subjectId = " + subjectId + "! " + e.getMessage());

			return null;
		}

		return progressTestResponseDTOList;
	}

	@Override
	public String createProgressTest(ProgressTestRequestDTO progressTestRequestDTO) {
		long subjectId = progressTestRequestDTO.getSubjectId();
		long unitId = progressTestRequestDTO.getUnitAfterId();
		String progressTestName = progressTestRequestDTO.getProgressTestName();

		try {
			// validate subjectId and check progressTestName existed
			Subject subject = iSubjectRepository.findByIdAndIsDisableFalse(subjectId);
			if (subject == null) {
				throw new ResourceNotFoundException();
			}

			Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);
			if (unit == null) {
				throw new ResourceNotFoundException();
			}
			if (iProgressTestRepository.findBySubjectIdAndProgressTestNameIgnoreCaseAndIsDisableFalse(subjectId,
					progressTestName) != null) {

				return "EXISTED";
			}

			ProgressTest progressTest = modelMapper.map(progressTestRequestDTO, ProgressTest.class);
			progressTest.setDisable(false);
			iProgressTestRepository.save(progressTest);
		} catch (Exception e) {
			logger.error("Create progressTest with name= " + progressTestName + ", in subjectId =  " + subjectId
					+ " and unitAfterId =  " + unitId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CREATE FAIL!";
		}

		return "CREATE SUCCESSS!";
	}

	@Override
	public String updateProgressTest(long id, ProgressTestRequestDTO progressTestRequestDTO) {
		long subjectId = progressTestRequestDTO.getSubjectId();
		String progressTestName = progressTestRequestDTO.getProgressTestName();
		String description = progressTestRequestDTO.getDescription();
		long unitId = progressTestRequestDTO.getUnitAfterId();

		try {
			// validate progressTestId, subjectId and check getProgressTestName existed
			ProgressTest progressTest = iProgressTestRepository.findByIdAndIsDisableFalse(id);
			if (progressTest == null) {
				throw new ResourceNotFoundException();
			}
			Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);
			if (unit == null) {
				throw new ResourceNotFoundException();
			}

			if (!progressTest.getProgressTestName().equalsIgnoreCase(progressTestName)) {
				if (iProgressTestRepository.findBySubjectIdAndProgressTestNameIgnoreCaseAndIsDisableFalse(subjectId,
						progressTestName) != null) {

					return "EXISTED";
				}
			}
			progressTest.setProgressTestName(progressTestName);
			progressTest.setDescription(description);
			progressTest.setUnitAfterId(unitId);
			iProgressTestRepository.save(progressTest);
		} catch (Exception e) {
			logger.error(
					"Update progressTest with id = " + id + " and unitAfterId =  " + unitId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "UPDATE FAIL!";
		}

		return "UPDATE SUCCESS!";
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
			// delete all exercise
			List<Exercise> exerciseList = iExerciseRepository.findByProgressTestIdAndStatusNot(id, DELETED_STATUS);
			if (!exerciseList.isEmpty()) {
				for (Exercise exercise : exerciseList) {
					IdAndStatusDTO idAndStatusDTO = new IdAndStatusDTO(exercise.getId(), DELETED_STATUS);
					iExerciseService.changeOneExerciseStatus(idAndStatusDTO);
				}
			}
			// delete progressTest
			progressTest.setDisable(true);
			iProgressTestRepository.save(progressTest);
		} catch (Exception e) {
			logger.error("Delete progressTest with id = " + id + "! " + e.getMessage());
			throw e;
		}
	}

}
