package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.GradeDTO;

public interface IGradeService {

	List<GradeDTO> findAllGrade();
}
