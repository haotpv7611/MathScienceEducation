package com.example.demo.services;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.SubjectDTO;
import com.example.demo.models.Subject;

public interface ISubjectService {

	public List<SubjectDTO> findSubjectByGradeId(long gradeId);
	Subject findByIdAndIsDisable(long id, boolean isDisable);
	String createSubject(String subjectName, MultipartFile multipartFile, String description, long gradeId)
			throws SizeLimitExceededException, IOException;
	String updateSubject(long id, String subjectName, MultipartFile multipartFile, String description, long gradeId)
			throws SizeLimitExceededException, IOException;
	String deleteSubject(long id);
	SubjectDTO findById(long id);
}
