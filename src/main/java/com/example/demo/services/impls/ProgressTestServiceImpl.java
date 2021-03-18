package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.ProgressTestDTO;
import com.example.demo.models.ProgressTest;
import com.example.demo.repositories.IProgressTestRepository;
import com.example.demo.services.IProgressTestService;

@Service
public class ProgressTestServiceImpl implements IProgressTestService {

	@Autowired
	private IProgressTestRepository iProgressTestRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<ProgressTestDTO> findBySubjectId(long subjectId) {

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
}
