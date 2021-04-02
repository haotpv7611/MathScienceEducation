package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ProgressTestDTO;

public interface IProgressTestService {
	List<ProgressTestDTO> findBySubjectId(long subjectId);

	List<ProgressTestDTO> findBySubjectIdAndIsDisable(long subjectId);

	String createProgressTest(ProgressTestDTO progressTestDTO);

	String updateProgressTest(ProgressTestDTO progressTestDTO);

	String deleteProgressTest(long id);

	ProgressTestDTO findById(long id);
}
