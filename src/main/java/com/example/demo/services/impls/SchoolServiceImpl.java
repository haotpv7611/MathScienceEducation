package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.IdAndStatusDTO;
import com.example.demo.dtos.SchoolRequestDTO;
import com.example.demo.dtos.SchoolResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.School;
import com.example.demo.models.SchoolGrade;
import com.example.demo.repositories.ISchoolGradeRepository;
import com.example.demo.repositories.ISchoolLevelRepository;
import com.example.demo.repositories.ISchoolRepository;
import com.example.demo.services.ISchoolService;

@Service
public class SchoolServiceImpl implements ISchoolService {

	@Autowired
	private ISchoolRepository iSchoolRepository;

	@Autowired
	private ISchoolGradeRepository iSchoolGradeRepository;

	@Autowired
	private ISchoolLevelRepository iSchoolLevelRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<SchoolResponseDTO> findSchoolLinkedByGradeId(long gradeId) {
		List<SchoolGrade> schoolGradeList = iSchoolGradeRepository.findByGradeIdAndStatusNot(gradeId, "DELETED");
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
		School school = iSchoolRepository.findByIdAndStatusNot(id, "DELETED");
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
	public String checkSchoolExisted(String schoolName, String district, String schoolLevel) {
		int schoolLevelId = iSchoolLevelRepository.findByDescription(schoolLevel).getId();

		List<School> schoolList = iSchoolRepository.findBySchoolNameAndSchoolDistrictAndSchoolLevelId(schoolName,
				district, schoolLevelId);

		if (!schoolList.isEmpty()) {
			return "EXISTED";
		}

		return "OK";
	}

	@Override
	public String createSchool(SchoolRequestDTO schoolRequestDTO) {
		schoolRequestDTO.setSchoolName(schoolRequestDTO.getSchoolName().trim());
		schoolRequestDTO.setSchoolStreet(schoolRequestDTO.getSchoolStreet().trim());
		schoolRequestDTO.setSchoolDistrict(schoolRequestDTO.getSchoolDistrict().trim());
		schoolRequestDTO.setSchoolName(schoolRequestDTO.getSchoolName().trim());

		String schoolCode = generateSchoolCode(schoolRequestDTO.getSchoolName());
		int schoolCount = generateSchoolCount(schoolCode);
		String schoolLevel = schoolRequestDTO.getSchoolLevel();
		School school = modelMapper.map(schoolRequestDTO, School.class);
		school.setSchoolCode(schoolCode);
		school.setSchoolCount(schoolCount);
		school.setSchoolLevel(iSchoolLevelRepository.findByDescription(schoolLevel));
		school.setStatus("ACTIVE");
		iSchoolRepository.save(school);

		return "CREATE SUCCESS!";
	}

	@Override
	public String updateSchool(long id, SchoolRequestDTO schoolRequestDTO) {
		School school = iSchoolRepository.findByIdAndStatusNot(id, "DELETED");
		if (school == null) {
			throw new ResourceNotFoundException();
		}
		school.setSchoolStreet(schoolRequestDTO.getSchoolStreet());
		school.setSchoolDistrict(schoolRequestDTO.getSchoolDistrict());
		String schoolLevel = schoolRequestDTO.getSchoolLevel();
		school.setSchoolLevel(iSchoolLevelRepository.findByDescription(schoolLevel));
		school = iSchoolRepository.save(school);

		return "UPDATE SUCCESS!";
	}

	@Override
	public String changeStatusSchool(IdAndStatusDTO idAndStatusDTO) {
		// validate data

		School school = iSchoolRepository.findByIdAndStatusNot(idAndStatusDTO.getId(), "DELETED");
		if (school == null) {
			throw new ResourceNotFoundException();
		}
		school.setStatus(idAndStatusDTO.getStatus());
		iSchoolRepository.save(school);

		return "CHANGE SUCCESS!";
	}

	@Override
	public List<SchoolResponseDTO> findAllSchool() {

		// 1. connect database through repository
		// 2. find all entities are not disable
		List<School> schoolList = iSchoolRepository.findByStatusNot("DELETED");
		List<SchoolResponseDTO> schoolDTOList = new ArrayList<>();

		if (schoolList != null) {
			for (School school : schoolList) {
				SchoolResponseDTO schoolResponseDTO = modelMapper.map(school, SchoolResponseDTO.class);
				schoolResponseDTO.setSchoolAddress(school.getSchoolStreet() + ", " + school.getSchoolDistrict());
				String schoolCount = String.valueOf(school.getSchoolCount());
				if (school.getSchoolCount() == 1) {
					schoolCount = "";
				}
				schoolResponseDTO.setSchoolCode(school.getSchoolCode() + schoolCount);
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

	private int generateSchoolCount(String schoolCode) {

		// connect db, find school by code with count max
		School school = iSchoolRepository.findFirstBySchoolCodeOrderBySchoolCountDesc(schoolCode);
		
		//if not have school with code, return count = 1, else count max += 1
		int schoolCount = 0;
		if (school != null) {
			schoolCount = school.getSchoolCount();
		}
		
		return (schoolCount + 1);
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
