package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ProgressTestDTO;
import com.example.demo.dtos.ProgressTestRequestDTO;

public interface IProgressTestService {
	List<ProgressTestDTO> findBySubjectId(long subjectId);

//	List<ProgressTestDTO> findBySubjectIdAndIsDisable(long subjectId);

	String createProgressTest(ProgressTestRequestDTO progressTestRequestDTO);

	String updateProgressTest(long id, ProgressTestRequestDTO progressTestRequestDTO);

	String deleteProgressTest(long id);
	
	void deleteOneProgressTest(long id);

	ProgressTestDTO findById(long id);
}
