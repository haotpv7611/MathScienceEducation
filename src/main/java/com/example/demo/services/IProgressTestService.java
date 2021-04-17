package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ProgressTestResponseDTO;
import com.example.demo.dtos.ProgressTestRequestDTO;

public interface IProgressTestService {

	Object findById(long id);

	List<ProgressTestResponseDTO> findBySubjectId(long subjectId);

	String createProgressTest(ProgressTestRequestDTO progressTestRequestDTO);

	String updateProgressTest(long id, ProgressTestRequestDTO progressTestRequestDTO);

//	String deleteProgressTest(long id);

	void deleteOneProgressTest(long id);

}
