package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.models.StudentProfile;

public interface IStudentProfileService {
	
	List<StudentProfile> findBySchoolId(ListIdAndStatusDTO listIdAndStatusDTO);
	
	
}
