package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.GradeResponseDTO;

public interface IGradeService {

	List<GradeResponseDTO> findAllGrades();
}
