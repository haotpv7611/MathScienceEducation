package com.example.demo.services;

import java.util.List;
import java.util.Map;

import com.example.demo.dtos.ProgressTestResponseDTO;
import com.example.demo.dtos.ProgressTestRequestDTO;

public interface IProgressTestService {

	Object findById(long id);

	List<ProgressTestResponseDTO> findBySubjectId(long subjectId);

	Map<Long, String> findAllProgressTest();

	String createProgressTest(ProgressTestRequestDTO progressTestRequestDTO);

	String updateProgressTest(long id, ProgressTestRequestDTO progressTestRequestDTO);

	String deleteOneProgressTest(long id);

}
