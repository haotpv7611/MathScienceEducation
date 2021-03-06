package com.example.demo.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.SubjectResponseDTO;

public interface ISubjectService {

	Object findById(long id);

	// show view all subject both roles
	public List<SubjectResponseDTO> findSubjectsByGradeId(int gradeId);

	Map<Long, String> findAllSubject();

	String createSubject(String subjectName, MultipartFile file, String description, int gradeId)
			throws SizeLimitExceededException, IOException;

	String updateSubject(long id, String subjectName, MultipartFile file, String description, int gradeId)
			throws SizeLimitExceededException, IOException;

	String deleteSubject(long id);

}
