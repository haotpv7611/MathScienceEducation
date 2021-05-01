package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.SchoolLevelResponseDTO;
import com.example.demo.models.SchoolLevel;
import com.example.demo.repositories.ISchoolLevelRepository;
import com.example.demo.services.ISchoolLevelService;

@Service
public class SchoolLevelServiceImpl implements ISchoolLevelService {
	Logger logger = LoggerFactory.getLogger(SchoolLevelServiceImpl.class);

	@Autowired
	private ISchoolLevelRepository iSchoolLevelRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<SchoolLevelResponseDTO> findAll() {
		List<SchoolLevelResponseDTO> schoolLevelResponseDTOList = new ArrayList<>();
		try {
			List<SchoolLevel> schoolLevelList = iSchoolLevelRepository.findAll();
			if (!schoolLevelList.isEmpty()) {
				for (SchoolLevel schoolLevel : schoolLevelList) {
					SchoolLevelResponseDTO schoolLevelResponseDTO = modelMapper.map(schoolLevel,
							SchoolLevelResponseDTO.class);
					schoolLevelResponseDTOList.add(schoolLevelResponseDTO);
				}
			}
		} catch (Exception e) {
			logger.error("FIND: all school Level! " + e.getMessage());

			return null;
		}
		return schoolLevelResponseDTOList;
	}

//	public String createSchoolLevel(String description) {
//		try {
//			// validate subjectId and check unitName existed
//			if (iSchoolLevelRepository.findByDescription(description) != null) {
//
//				return "EXISTED";
//			}
//
//			// save data and return
//			SchoolLevel schoolLevel = new SchoolLevel();
//			schoolLevel.setDescription(description);
//			iSchoolLevelRepository.save(schoolLevel);
//		} catch (Exception e) {
//			logger.error("CREATE: schoolLevel with description = " + description + "! " + e.getMessage());
//
//			return "CREATE FAIL!";
//		}
//
//		return "CREATE SUCCESS!";
//	}

}
