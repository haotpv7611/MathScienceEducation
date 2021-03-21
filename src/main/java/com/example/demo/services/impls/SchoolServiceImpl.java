package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.SchoolDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.School;
import com.example.demo.repositories.ISchoolRepository;
import com.example.demo.services.ISchoolService;

@Service
public class SchoolServiceImpl implements ISchoolService {

	@Autowired
	private ISchoolRepository iSchoolRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public SchoolDTO createSchool(SchoolDTO schoolDTO) {

		if (schoolDTO.getSchoolName() != null && schoolDTO.getSchoolAddress() != null) {

			School school = modelMapper.map(schoolDTO, School.class);
			String schoolCode = generateSchoolCode(schoolDTO.getSchoolName());
			school.setSchoolCode(schoolCode);
			school.setSchoolCount(generateSchoolCount(schoolCode));
			school.setDisable(false);

			return modelMapper.map(iSchoolRepository.save(school), SchoolDTO.class);
		}

		return null;
	}

	@Override
	public SchoolDTO updateSchool(SchoolDTO schoolDTO) {

		School school = iSchoolRepository.findById(schoolDTO.getId())
				.orElseThrow(() -> new ResourceNotFoundException());
		if (schoolDTO.getSchoolName() != null) {
			school.setSchoolName(schoolDTO.getSchoolName());
			String schoolCode = generateSchoolCode(schoolDTO.getSchoolName());
			school.setSchoolCount(generateSchoolCount(schoolCode));
			school.setSchoolCode(schoolCode);
		}

		if (schoolDTO.getSchoolAddress() != null) {
			school.setSchoolAddress(schoolDTO.getSchoolAddress());
		}

		school = iSchoolRepository.save(school);

		return modelMapper.map(school, SchoolDTO.class);
	}

	@Override
	public String deleteSchool(long id) {

		School school = iSchoolRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());

		school.setDisable(true);
		iSchoolRepository.save(school);

		return "Delete successed!";

	}

	@Override
	public List<SchoolDTO> findAllSchool() {

		// 1. connect database through repository
		// 2. find all entities are not disable
		List<School> schoolList = iSchoolRepository.findAll();
		List<SchoolDTO> schoolDTOList = new ArrayList<>();

		if (schoolList != null) {
			for (School school : schoolList) {
				schoolDTOList.add(modelMapper.map(school, SchoolDTO.class));
			}
		}

		return schoolDTOList;
	}

	@Override
	public SchoolDTO findBySchoolId(long id) {
		School school = iSchoolRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());

		return modelMapper.map(school, SchoolDTO.class);
	}

	private String generateSchoolCode(String schoolName) {

		String[] schoolNameArray = schoolName.split(" ");
		String schooleCode = "";
		for (int i = 0; i < schoolNameArray.length; i++) {
			schooleCode += schoolNameArray[i].charAt(0);
		}

		return schooleCode.toUpperCase();
	}

	private String generateSchoolCount(String schoolCode) {

		Long count = iSchoolRepository.countBySchoolCode(schoolCode);
		String schoolCount = "";
		if (count >= 1) {
			schoolCount = String.valueOf(count + 1);
		}

		return schoolCount;
	}
}
