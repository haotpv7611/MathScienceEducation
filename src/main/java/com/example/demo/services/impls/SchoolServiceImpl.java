package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.SchoolRequestDTO;
import com.example.demo.dtos.SchoolResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.School;
import com.example.demo.models.SchoolGrade;
import com.example.demo.repositories.ISchoolGradeRepository;
import com.example.demo.repositories.ISchoolRepository;
import com.example.demo.services.ISchoolService;

@Service
public class SchoolServiceImpl implements ISchoolService {

	@Autowired
	private ISchoolRepository iSchoolRepository;

	@Autowired
	private ISchoolGradeRepository iSchoolGradeRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<SchoolResponseDTO> findByGradeId(long gradeId) {
		List<SchoolGrade> schoolGradeList = iSchoolGradeRepository.findByGradeIdAndIsDisable(gradeId, false);
		List<SchoolResponseDTO> schoolResponseDTOList = new ArrayList<>();

		if (!schoolGradeList.isEmpty()) {
			for (SchoolGrade schoolGrade : schoolGradeList) {
				SchoolResponseDTO schoolResponseDTO = (modelMapper.map(schoolGrade.getSchool(),
						SchoolResponseDTO.class));
				schoolResponseDTO.setSchoolAddress(null);
				schoolResponseDTO.setSchoolLevel(null);
				schoolResponseDTOList.add(schoolResponseDTO);
			}
		}

		return schoolResponseDTOList;
	}

	@Override
	public SchoolResponseDTO findSchoolById(long id) {
		School school = iSchoolRepository.findByIdAndIsDisable(id, false);
		if (school == null) {
			throw new ResourceNotFoundException();
		}
		String schoolName = school.getSchoolName();
		String schoolAddress = school.getSchoolStreet() + ", " + school.getSchoolDistrict() + ", HCM City";
		String schoolLevel = school.getSchoolLevel().getDescription();
		SchoolResponseDTO schoolResponseDTO = new SchoolResponseDTO(schoolName, schoolAddress, schoolLevel);

		return schoolResponseDTO;
	}

	@Override
	public String createSchool(SchoolRequestDTO schoolRequestDTO) {
		schoolRequestDTO.setSchoolName(schoolRequestDTO.getSchoolName().trim());
		String schoolCode = generateSchoolCode(schoolRequestDTO.getSchoolName());
		String schoolCount = generateSchoolCount(schoolCode);
		School school = modelMapper.map(schoolRequestDTO, School.class);
		school.setSchoolCode(schoolCode);
		school.setSchoolCount(schoolCount);
		school.setDisable(false);
		iSchoolRepository.save(school);

		return "CREATE SUCCESS!";
	}

	@Override
	public String updateSchool(long id, SchoolRequestDTO schoolRequestDTO) {
		School school = iSchoolRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
		if (school.isDisable()) {
			throw new ResourceNotFoundException();
		}
		school.setSchoolStreet(schoolRequestDTO.getSchoolStreet());
		school.setSchoolDistrict(schoolRequestDTO.getSchoolDistrict());
		school = iSchoolRepository.save(school);

		return "UPDATE SUCCESS!";
	}

	@Override
	public String deleteSchool(long id) {

		School school = iSchoolRepository.findByIdAndIsDisable(id, false);
		if (school == null) {
			throw new ResourceNotFoundException();
		}
		school.setDisable(true);
		iSchoolRepository.save(school);

		return "DELETE SUCCESS!";
	}

	@Override
	public List<SchoolResponseDTO> findAllSchool() {

		// 1. connect database through repository
		// 2. find all entities are not disable
		List<School> schoolList = iSchoolRepository.findAll();
		List<SchoolResponseDTO> schoolDTOList = new ArrayList<>();

		if (schoolList != null) {
			for (School school : schoolList) {
				SchoolResponseDTO schoolResponseDTO = modelMapper.map(school, SchoolResponseDTO.class);
				schoolResponseDTO.setSchoolAddress(school.getSchoolStreet() + ", " + school.getSchoolDistrict());
				schoolResponseDTO.setSchoolCode(school.getSchoolCode() + school.getSchoolCount());
				schoolDTOList.add(schoolResponseDTO);
			}
		}

		return schoolDTOList;
	}

	private String generateSchoolCode(String schoolName) {
		String[] schoolNameArray = schoolName.split(" ");
		String schooleCode = "";
		for (int i = 0; i < schoolNameArray.length; i++) {
			char firstCharCode = schoolNameArray[i].toUpperCase().charAt(0);
			if (!Character.isDigit(firstCharCode)) {
				schooleCode += convertoEnglistCharacter(firstCharCode);
			}
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

	private char convertoEnglistCharacter(char schoolCode) {
		char schoolCodeConvert;
		switch (schoolCode) {
		case 'Ă':
		case 'Â':
			schoolCodeConvert = 'A';
			break;
		case 'Đ':
			schoolCodeConvert = 'D';
			break;
		case 'Ê':
			schoolCodeConvert = 'E';
			break;
		case 'Ô':
		case 'Ơ':
			schoolCodeConvert = 'O';
			break;
		case 'Ư':
			schoolCodeConvert = 'U';
			break;
		default:
			schoolCodeConvert = schoolCode;
			break;
		}

		return schoolCodeConvert;
	}

}
