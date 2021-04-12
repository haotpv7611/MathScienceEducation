package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.StudentRequestDTO;
import com.example.demo.dtos.StudentResponseDTO;

public interface IStudentProfileService {

	StudentResponseDTO findStudentByAccountId(long accountId);

	List<StudentResponseDTO> findStudentByListId(List<Long> ids);

	String createStudenProfile(StudentRequestDTO studentProfileRequestDTO);
	
	String changeStatusStudent(ListIdAndStatusDTO listIdAndStatusDTO);
}
