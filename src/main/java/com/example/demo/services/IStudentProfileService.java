package com.example.demo.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.StudentRequestDTO;
import com.example.demo.dtos.StudentResponseDTO;

public interface IStudentProfileService {

	StudentResponseDTO findStudentById(long accountId);

	List<StudentResponseDTO> findStudentByListId(List<Long> ids);

	String createStudenProfile(StudentRequestDTO studentProfileRequestDTO);

	String updateStudent(long id, StudentRequestDTO studentProfileRequestDTO);

	String changeStatusStudent(ListIdAndStatusDTO listIdAndStatusDTO);

	String validateStudentFile(MultipartFile file, long schoolId, long gradeId) throws IOException;

	String importStudent(MultipartFile file, long schoolId, long gradeId) throws IOException;
}
