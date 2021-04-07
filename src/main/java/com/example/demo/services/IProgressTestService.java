package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ProgressTestDTO;

public interface IProgressTestService {
	List<ProgressTestDTO> findBySubjectId(long subjectId);

//	List<ProgressTestDTO> findBySubjectIdAndIsDisable(long subjectId);

	String createProgressTest(ProgressTestDTO progressTestDTO);

	String updateProgressTest(long id, ProgressTestDTO progressTestDTO);

//	String deleteProgressTest(List<Long> ids);
	
	void deleteOneProgressTest(long id);

	ProgressTestDTO findById(long id);
}
