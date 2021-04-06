package com.example.demo.services;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.SubjectResponseDTO;

public interface ISubjectService {

	public List<SubjectResponseDTO> findSubjectByGradeId(long gradeId);

	String createSubject(String subjectName, MultipartFile file, String description, long gradeId)
			throws SizeLimitExceededException, IOException;

	String updateSubject(long id, String subjectName, MultipartFile file, String description, long gradeId)
			throws SizeLimitExceededException, IOException;

	String deleteSubject(long id);

	SubjectResponseDTO findById(long id);

}
