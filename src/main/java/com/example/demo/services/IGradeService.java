package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.GradeDTO;

public interface IGradeService {

	List<GradeDTO> findByIsDisable(boolean isDisable);
	String createGrade(int gradeName);
	void deleteGrade(long id);
}
