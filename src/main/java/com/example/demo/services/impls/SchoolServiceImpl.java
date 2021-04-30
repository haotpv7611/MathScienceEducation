package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.IdAndStatusDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.SchoolRequestDTO;
import com.example.demo.dtos.SchoolResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.School;
import com.example.demo.models.SchoolGrade;
import com.example.demo.models.SchoolLevel;
import com.example.demo.repositories.ISchoolGradeRepository;
import com.example.demo.repositories.ISchoolLevelRepository;
import com.example.demo.repositories.ISchoolRepository;
import com.example.demo.services.ISchoolGradeService;
import com.example.demo.services.ISchoolService;

@Service
public class SchoolServiceImpl implements ISchoolService {
	Logger logger = LoggerFactory.getLogger(SchoolServiceImpl.class);
	private final String ACTIVE_STATUS = "ACTIVE";
	private final String DELETED_STATUS = "DELETED";

	@Autowired
	private ISchoolRepository iSchoolRepository;

	@Autowired
	private ISchoolLevelRepository iSchoolLevelRepository;

	@Autowired
	private ISchoolGradeRepository iSchoolGradeRepository;

	@Autowired
	private ISchoolGradeService iSchoolGradeService;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Object findSchoolById(long id) {
		SchoolResponseDTO schoolResponseDTO = null;
		try {
			School school = iSchoolRepository.findByIdAndStatusNot(id, DELETED_STATUS);
			if (school == null) {
				throw new ResourceNotFoundException();
			}
			schoolResponseDTO = modelMapper.map(school, SchoolResponseDTO.class);
			schoolResponseDTO.setSchoolLevel(school.getSchoolLevel().getDescription());
			schoolResponseDTO.setSchoolCode(school.getSchoolCode() + school.getSchoolCount());

		} catch (Exception e) {
			logger.error("FIND: school by id = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "FIND FAIL!";
		}

		return schoolResponseDTO;
	}

	@Override
	public List<SchoolResponseDTO> findAllSchool() {
		List<SchoolResponseDTO> schoolDTOList = new ArrayList<>();

		try {
			// 1. connect database through repository
			// 2. find all entities are not disable
			List<School> schoolList = iSchoolRepository.findByStatusNotOrderByStatusAscSchoolNameAsc(DELETED_STATUS);
			if (!schoolList.isEmpty()) {
				for (School school : schoolList) {
					SchoolResponseDTO schoolResponseDTO = modelMapper.map(school, SchoolResponseDTO.class);
					String schoolCount = String.valueOf(school.getSchoolCount());
					schoolResponseDTO.setSchoolCode(school.getSchoolCode() + schoolCount);
					schoolResponseDTO.setSchoolLevel(school.getSchoolLevel().getDescription());
					schoolDTOList.add(schoolResponseDTO);
				}
			}
		} catch (Exception e) {
			logger.error("FIND: all school! " + e.getMessage());

			return null;
		}

		return schoolDTOList;
	}

	@Override
	public List<SchoolResponseDTO> findSchoolUnlinkByGradeId(int gradeId) {
		List<SchoolResponseDTO> schoolResponseDTOList = new ArrayList<>();

		try {
			List<School> schoolList = iSchoolRepository.findByStatus(ACTIVE_STATUS);
			if (!schoolList.isEmpty()) {
				for (School school : schoolList) {
					if (iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, school.getId(),
							DELETED_STATUS) == null) {
						SchoolResponseDTO schoolResponseDTO = modelMapper.map(school, SchoolResponseDTO.class);
						schoolResponseDTO.setSchoolName(school.getSchoolName() + " - " + school.getSchoolCode()
								+ school.getSchoolCount() + " - " + school.getSchoolDistrict());
						schoolResponseDTO.setSchoolLevel(school.getSchoolLevel().getDescription());
						schoolResponseDTOList.add(schoolResponseDTO);
					}
				}
			}
		} catch (Exception e) {
			logger.error("FIND: all school status active ! " + e.getMessage());

			return null;
		}

		return schoolResponseDTOList;
	}

	@Override
	public String createSchool(SchoolRequestDTO schoolRequestDTO) {
		String schoolName = schoolRequestDTO.getSchoolName();
		String schoolDistrict = schoolRequestDTO.getSchoolDistrict();
		try {
			SchoolLevel schoolLevel = iSchoolLevelRepository.findByDescription(schoolRequestDTO.getSchoolLevel());
			if (schoolLevel == null) {
				throw new ResourceNotFoundException();
			}
			boolean isExisted = checkSchoolExisted(schoolRequestDTO.getSchoolLevel(), schoolName, schoolDistrict);
			if (isExisted) {

				return "EXISTED";
			}

			String schoolCode = generateSchoolCode(schoolRequestDTO.getSchoolName());
			int schoolCount = generateSchoolCount(schoolCode);
			School school = modelMapper.map(schoolRequestDTO, School.class);

			school.setSchoolCode(schoolCode);
			school.setSchoolCount(schoolCount);
			school.setSchoolLevel(schoolLevel);
			school.setStatus(ACTIVE_STATUS);
			iSchoolRepository.save(school);

		} catch (Exception e) {
			logger.error("CREATE: school with name = " + schoolName + ", in schoolLevel =  "
					+ schoolRequestDTO.getSchoolLevel() + " and District = " + schoolDistrict + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CREATE FAIL!";
		}

		return "CREATE SUCCESS!";
	}

	@Override
	public String updateSchool(long id, SchoolRequestDTO schoolRequestDTO) {
		String schoolDistrict = schoolRequestDTO.getSchoolDistrict();
		try {
			School school = iSchoolRepository.findByIdAndStatusNot(id, DELETED_STATUS);
			if (school == null) {
				throw new ResourceNotFoundException();
			}
			SchoolLevel schoolLevel = iSchoolLevelRepository.findByDescription(schoolRequestDTO.getSchoolLevel());
			if (schoolLevel == null) {
				throw new ResourceNotFoundException();
			}

			if (!school.getSchoolDistrict().equalsIgnoreCase(schoolDistrict)
					|| !school.getSchoolLevel().getDescription().equalsIgnoreCase(schoolRequestDTO.getSchoolLevel())) {
				boolean isExisted = checkSchoolExisted(schoolRequestDTO.getSchoolLevel(),
						schoolRequestDTO.getSchoolName(), schoolRequestDTO.getSchoolDistrict());
				if (isExisted) {

					return "EXISTED";
				}
			}
			school.setSchoolStreet(schoolRequestDTO.getSchoolStreet());
			school.setSchoolDistrict(schoolRequestDTO.getSchoolDistrict());
			school.setSchoolLevel(schoolLevel);
			school = iSchoolRepository.save(school);
		} catch (Exception e) {
			logger.error("UPDATE: schoolId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "UPDATE FAIL!";
		}

		return "UPDATE SUCCESS!";
	}

	@Override
	public String changeStatusSchool(IdAndStatusDTO idAndStatusDTO) {
		try {
			School school = iSchoolRepository.findByIdAndStatusNot(idAndStatusDTO.getId(), "DELETED");
			if (school == null) {
				throw new ResourceNotFoundException();
			}

			List<SchoolGrade> schoolGradeList = iSchoolGradeRepository.findBySchoolIdAndStatusNot(school.getId(),
					DELETED_STATUS);
			if (!schoolGradeList.isEmpty()) {
				for (SchoolGrade schoolGrade : schoolGradeList) {
					List<Long> ids = new ArrayList<>();
					ids.add(Long.valueOf(schoolGrade.getGrade().getId()));
					ids.add(school.getId());
					ListIdAndStatusDTO listIdAndStatusDTO = new ListIdAndStatusDTO();
					listIdAndStatusDTO.setIds(ids);
					listIdAndStatusDTO.setStatus(idAndStatusDTO.getStatus());

					String response = iSchoolGradeService.changeStatusGradeAndSchool(listIdAndStatusDTO);
					if (!response.equalsIgnoreCase("OK")) {

						return response;
					}
				}
			}
			school.setStatus(idAndStatusDTO.getStatus());
			iSchoolRepository.save(school);
		} catch (Exception e) {
			logger.error("Change status school with id = " + idAndStatusDTO.getId() + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "DELETE FAIL!";
		}

		return "CHANGE SUCCESS!";
	}

	private boolean checkSchoolExisted(String schoolLevel, String schoolName, String district) {
		schoolLevel = schoolLevel.trim().replaceAll("\\s+", " ");
		schoolName = schoolName.trim().replaceAll("\\s+", " ");
		district = district.trim().replaceAll("\\s+", " ");
		boolean isExisted = false;
		try {
			int schoolLevelId = iSchoolLevelRepository.findByDescription(schoolLevel).getId();
			List<School> schoolList = iSchoolRepository.findBySchoolNameAndSchoolDistrictAndSchoolLevelIdAndStatusNot(
					schoolName, district, schoolLevelId, DELETED_STATUS);
			if (!schoolList.isEmpty()) {
				isExisted = true;
			}
		} catch (Exception e) {
			throw e;
		}

		return isExisted;
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

		// if not have school with code, return count = 1, else count max += 1
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
