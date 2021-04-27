package com.example.demo.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.StudentRequestDTO;
import com.example.demo.dtos.StudentResponseDTO;

public interface IStudentProfileService {

	Object findStudentById(long id);

	StudentResponseDTO findStudentByAccountId(long accountId);

	List<StudentResponseDTO> findStudentByListId(List<Long> ids);

	String createStudenProfile(StudentRequestDTO studentProfileRequestDTO);

	String updateStudent(long id, StudentRequestDTO studentProfileRequestDTO);

	String changeStatusStudent(ListIdAndStatusDTO listIdAndStatusDTO);

	void changeStatusOneStudent(long id, String status);

	String changeClassForStudent(List<Long> studentIdList, long classesId);

	Map<String, Workbook> validateStudentFile(MultipartFile file, long schoolId, int gradeId) throws ParseException;

	Map<String, Workbook> importStudent(MultipartFile file, long schoolId, int gradeId);

	Map<String, Workbook> exportScoreBySubjectId(long schoolId, int gradeId, long subjectId);

	Map<String, Workbook> exportFinalScore(long schoolId, int gradeId) throws IOException;

	String generateFileNameExport(long schoolId, int gradeId, long subjectId);

	void writeFileOS(HttpServletResponse httpServletResponse, Workbook workbook) throws IOException;
}
