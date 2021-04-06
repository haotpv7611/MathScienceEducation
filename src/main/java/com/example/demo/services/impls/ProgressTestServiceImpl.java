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
		Subject subject = iSubjectRepository.findByIdAndIsDisable(subjectId, false);
		if (subject == null) {
			throw new ResourceNotFoundException();
		}

		// 1. connect database through repository
		// 2. find all entities are not disable and have subjectId = ?
		List<ProgressTest> progressTestList = iProgressTestRepository.findBySubjectIdAndIsDisable(subjectId, false);

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
	public List<ProgressTestDTO> findBySubjectIdAndIsDisable(long subjectId) {
		List<ProgressTest> progressTestLists = iProgressTestRepository.findBySubjectId(subjectId);
		List<ProgressTestDTO> progressTestDTOLists = new ArrayList<>();
		if (!progressTestLists.isEmpty()) {
			for (ProgressTest progressTest : progressTestLists) {
				ProgressTestDTO progressTestDTO = modelMapper.map(progressTest, ProgressTestDTO.class);
				progressTestDTOLists.add(progressTestDTO);
			}
		}
		return progressTestDTOLists;
	}

	@Override
	public String createProgressTest(ProgressTestDTO progressTestDTO) {
		Subject subject = iSubjectRepository.findByIdAndIsDisable(progressTestDTO.getSubjectId(), false);
		if (subject == null) {
			return "Subject is not existed";
		}
		List<ProgressTestDTO> listProgressTestLists = findBySubjectIdAndIsDisable(progressTestDTO.getSubjectId());
		for (ProgressTestDTO progressTestDTO2 : listProgressTestLists) {
			if (progressTestDTO.getProgressTestName().equalsIgnoreCase(progressTestDTO2.getProgressTestName())) {
				return "Progress Tesst Name is existed !";
			}
		}
		ProgressTest progressTest = modelMapper.map(progressTestDTO, ProgressTest.class);
		progressTest.setDisable(false);
		iProgressTestRepository.save(progressTest);
		return "CREATE SUCCESSS !";
	}

	@Override
	public String updateProgressTest(ProgressTestDTO progressTestDTO) {
		ProgressTest progressTest = iProgressTestRepository.findById(progressTestDTO.getId())
				.orElseThrow(() -> new ResourceNotFoundException());
		if (progressTest.isDisable()) {
			throw new ResourceNotFoundException();
		}
		if (!progressTest.getProgressTestName().equalsIgnoreCase(progressTestDTO.getProgressTestName())) {
			List<ProgressTestDTO> listProgressTestLists = findBySubjectIdAndIsDisable(progressTestDTO.getSubjectId());
			for (ProgressTestDTO progressTestDTO2 : listProgressTestLists) {
				if (progressTestDTO.getProgressTestName().equalsIgnoreCase(progressTestDTO2.getProgressTestName())) {
					return "Progress Tesst Name is existed !";
				}
			}
		}
		progressTest.setProgressTestName(progressTestDTO.getProgressTestName());
		progressTest.setDescription(progressTestDTO.getDescription());
		progressTest.setUnitAfterId(progressTestDTO.getUnitAfterId());
		iProgressTestRepository.save(progressTest);
		return "UPDATE SUCCESS !";
	}

	@Override
	@Transactional
	public String deleteProgressTest(long id) {
		ProgressTest progressTest = iProgressTestRepository.findByIdAndIsDisable(id, false);

		if (progressTest == null) {
			throw new ResourceNotFoundException();
		}
		List<Exercise> exerciseList = iExerciseRepository.findByProgressTestIdAndIsDisableOrderByExerciseNameAsc(id,
				false);
		for (Exercise exercise : exerciseList) {
			iExerciseService.deleteExercise(exercise.getId());
		}
		progressTest.setDisable(true);
		iProgressTestRepository.save(progressTest);
		return "DELETE SUCCESS !";
	}

	@Override
	public ProgressTestDTO findById(long id) {
		ProgressTest progressTest = iProgressTestRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
		if (progressTest.isDisable()) {
			throw new ResourceNotFoundException();
		}
		ProgressTestDTO progressTestDTO = modelMapper.map(progressTest, ProgressTestDTO.class);
		return progressTestDTO;
	}
}
