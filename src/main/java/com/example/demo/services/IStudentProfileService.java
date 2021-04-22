package com.example.demo.services;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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

	void changeStatusOneStudent(long id, String status);

	String changeClassForStudent(List<Long> studentIdList, long classesId);

	void validateStudentFile(MultipartFile file, long schoolId, int gradeId, HttpServletResponse httpServletResponse)
			throws IOException;

	String importStudent(MultipartFile file, long schoolId, int gradeId, HttpServletResponse httpServletResponse)
			throws IOException;

	void exportScoreBySubjectId(long schoolId, int gradeId, long subjectId, HttpServletResponse httpServletResponse)
			throws IOException;

	String generateFileNameExport(long schoolId, int gradeId, long subjectId);
}
