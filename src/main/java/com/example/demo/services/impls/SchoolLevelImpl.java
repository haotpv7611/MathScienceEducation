package com.example.demo.services.impls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.SchoolLevel;
import com.example.demo.repositories.ISchoolLevelRepository;
import com.example.demo.services.ISchoolLevelService;

@Service
public class SchoolLevelImpl implements ISchoolLevelService {
	@Autowired
	ISchoolLevelRepository iSchoolLevelRepository;

	@Override
	public List<SchoolLevel> findAll() {
		
		return iSchoolLevelRepository.findAll();
	}
	
}
