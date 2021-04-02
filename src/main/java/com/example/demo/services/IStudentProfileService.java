package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.StudentRequestDTO;
import com.example.demo.dtos.StudentResponseDTO;

public interface IStudentProfileService {

	List<StudentResponseDTO> findStudentByListId(ListIdAndStatusDTO listIdAndStatusDTO);

	String createStudenProfile(StudentRequestDTO studentProfileRequestDTO);
}
