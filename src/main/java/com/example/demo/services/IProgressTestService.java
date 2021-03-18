package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ProgressTestDTO;

public interface IProgressTestService {	
	List<ProgressTestDTO> findBySubjectId(long subjectId);
	
}
