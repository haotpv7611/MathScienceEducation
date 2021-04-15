package com.example.demo.services.impls;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.ClassResponseDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.SchoolGradeDTO;
import com.example.demo.dtos.StudentRequestDTO;
import com.example.demo.dtos.StudentResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Account;
import com.example.demo.models.Classes;
import com.example.demo.models.Grade;
import com.example.demo.models.School;
import com.example.demo.models.SchoolGrade;
import com.example.demo.models.StudentProfile;
import com.example.demo.repositories.IAccountRepository;
import com.example.demo.repositories.IClassRepository;
import com.example.demo.repositories.IGradeRepository;
import com.example.demo.repositories.ISchoolGradeRepository;
import com.example.demo.repositories.ISchoolRepository;
import com.example.demo.repositories.IStudentProfileRepository;
import com.example.demo.services.IClassService;
import com.example.demo.services.IStudentProfileService;

@Service
public class StudentProfileServiceImpl implements IStudentProfileService {
	Logger logger = LoggerFactory.getLogger(StudentProfileServiceImpl.class);

	private final String DELETE_STATUS = "DELETED";
	private final String PENDING_STATUS = "PENDING";
	private final String DEFAULT_PASSWORD = "abc123456";
	private final int STUDENT_ROLE = 3;
	private final int FIRST_CELL = 1;
	private final int LAST_CELL = 6;

	@Autowired
	ISchoolRepository iSchoolRepository;
	@Autowired
	IGradeRepository iGradeRepository;

	@Autowired
	ISchoolGradeRepository iSchoolGradeRepository;

	@Autowired
	IClassRepository iClassRepository;

	@Autowired
	IStudentProfileRepository iStudentProfileRepository;

	@Autowired
	IAccountRepository iAccountRepository;

	@Autowired
	IClassService iClassService;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public StudentResponseDTO findStudentById(long id) {
		StudentResponseDTO studentResponseDTO = new StudentResponseDTO();
		try {
			StudentProfile studentProfile = iStudentProfileRepository.findByIdAndStatusNot(id, DELETE_STATUS);
			if (studentProfile == null) {
				throw new ResourceNotFoundException();
			}

			Account account = studentProfile.getAccount();
			String className = studentProfile.getClasses().getClassName();
			String schoolName = studentProfile.getClasses().getSchoolGrade().getSchool().getSchoolName();

			studentResponseDTO = modelMapper.map(studentProfile, StudentResponseDTO.class);
			studentResponseDTO.setFullName(account.getFullName());
			studentResponseDTO.setClassName(className);
			studentResponseDTO.setSchoolName(schoolName);

		} catch (Exception e) {
			logger.error("FIND: student by studentId = " + id + "! " + e.getMessage());

			return null;
		}

		return studentResponseDTO;
	}

	@Override
	public List<StudentResponseDTO> findStudentByListId(List<Long> ids) {
		long schoolId = ids.get(0);
		int gradeId = Math.toIntExact(ids.get(1));
		long classId = ids.get(2);

		List<StudentResponseDTO> studentResponseDTOList = new ArrayList<>();

		if (schoolId != 0) {
			// check school existed
			School school = iSchoolRepository.findByIdAndStatusNot(schoolId, DELETE_STATUS);
			if (school == null) {
				throw new ResourceNotFoundException();
			}
			// if existed: get schoolName
			String schoolName = school.getSchoolName();

			SchoolGradeDTO schoolGradeDTO = null;
			List<ClassResponseDTO> classResponseDTOList = new ArrayList<>();

			// find if only have schoolId
			if (gradeId == 0 && classId == 0) {
				// get all grade linked
				List<SchoolGrade> schoolGradeList = iSchoolGradeRepository.findBySchoolIdAndStatusNot(schoolId,
						DELETE_STATUS);

				if (!schoolGradeList.isEmpty()) {
//					System.out.println("schoolGrade" + schoolGradeList.size());
					List<Classes> classesList = new ArrayList<>();
					for (SchoolGrade schoolGrade : schoolGradeList) {

						classesList.addAll(iClassRepository.findBySchoolGradeIdAndStatusNotOrderByStatusAscClassNameAsc(
								schoolGrade.getId(), DELETE_STATUS));

					}

					if (!classesList.isEmpty()) {
						for (Classes classes : classesList) {
							// get all student in class

							Grade grade = classes.getSchoolGrade().getGrade();

							int gradeName = grade.getGradeName();
							studentResponseDTOList
									.addAll(findStudentByClassedId(classes.getId(), schoolName, gradeName));

						}
					}

				}
			}
			// find if have schoolId and grade Id
			if (gradeId != 0) {
				Grade grade = iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());
				int gradeName = grade.getGradeName();
				if (classId == 0) {
					schoolGradeDTO = new SchoolGradeDTO(schoolId, gradeId);

					// get all class with schoolGradeId
					classResponseDTOList = iClassService.findBySchoolGradeId(schoolGradeDTO);
					if (!classResponseDTOList.isEmpty()) {
						for (ClassResponseDTO classResponseDTO : classResponseDTOList) {
							// get all student
							studentResponseDTOList
									.addAll(findStudentByClassedId(classResponseDTO.getId(), schoolName, gradeName));
						}
					}
				}
//				 find if have classesId
				if (classId != 0) {
					studentResponseDTOList.addAll(findStudentByClassedId(classId, schoolName, gradeName));
				}
			}

		}

		return studentResponseDTOList;
	}

	public List<StudentResponseDTO> findStudentByClassedId(long classesId, String schoolName, int gradeName) {
		Classes classes = iClassRepository.findByIdAndStatusNot(classesId, DELETE_STATUS);
		if (classes == null) {
			throw new ResourceNotFoundException();
		}
		String className = classes.getClassName();
		List<StudentResponseDTO> studentResponseDTOList = new ArrayList<>();
		List<StudentProfile> studentProfileList = iStudentProfileRepository.findByClassesIdAndStatusNot(classesId,
				DELETE_STATUS);
		for (StudentProfile studentProfile : studentProfileList) {
			Account account = studentProfile.getAccount();
			StudentResponseDTO studentResponseDTO = modelMapper.map(account, StudentResponseDTO.class);
			studentResponseDTO.setId(studentProfile.getId());
			studentResponseDTO.setSchoolName(schoolName);
			studentResponseDTO.setGradeName(gradeName);
			studentResponseDTO.setClassName(className);
			studentResponseDTO.setGender(studentProfile.getGender());
			studentResponseDTOList.add(studentResponseDTO);
		}

		return studentResponseDTOList;
	}

//classId --> schoolGradeId --> gradeName --> schoolCode
//schoolGradeId --> all className != pending --> all student active or inactive --> count max + 1
	@Override
	@Transactional
	public String createStudenProfile(StudentRequestDTO studentRequestDTO) {
		long classId = studentRequestDTO.getClassesId();
		Classes classes = iClassRepository.findByIdAndStatusNot(classId, DELETE_STATUS);
		int gradeName = classes.getSchoolGrade().getGrade().getGradeName();
		String schoolCode = classes.getSchoolGrade().getSchool().getSchoolCode()
				+ classes.getSchoolGrade().getSchool().getSchoolCount();
		
		long studentCount = countStudent(classes);
//		List<Classes> classesList = classes.getSchoolGrade().getClassList();
//		List<StudentProfile> studentProfileList = new ArrayList<>();
//		if (!classesList.isEmpty()) {
//			for (Classes classes2 : classesList) {
//				if (classes2.getStatus().equals("PENDING")) {
//					classesList.remove(classes2);
//				} else {
//					StudentProfile studentProfile = iStudentProfileRepository
//							.findFirstByClassesIdAndStatusLikeOrderByStudentCountDesc(classes2.getId(), "ACTIVE");
//
//					if (studentProfile != null) {
//						studentProfileList.add(studentProfile);
//					}
//				}
//			}
//		}
////		StudentProfile studentProfile = null;
//		long studentCount = 1;
//		if (!studentProfileList.isEmpty()) {
//			for (StudentProfile studentProfile : studentProfileList) {
//				if (studentProfile.getStudentCount() > studentCount) {
//					studentCount = studentProfile.getStudentCount();
//				}
//			}
//			studentCount++;
//		}
//		System.out.println(studentCount);
		String username = generateUsername(schoolCode, gradeName, studentCount);
		String fullName = studentRequestDTO.getFullName();
		Account account = new Account(username, DEFAULT_PASSWORD, fullName, STUDENT_ROLE, "ACTIVE");
		iAccountRepository.save(account);

		String DoB = studentRequestDTO.getDoB();
		String gender = studentRequestDTO.getGender();
		String parentName = studentRequestDTO.getParentName();
		String contact = studentRequestDTO.getContact();
		StudentProfile studentProfile = new StudentProfile(DoB, gender, parentName, contact, "ACTIVE", studentCount, account, classes);
		iStudentProfileRepository.save(studentProfile);

		return "CREATE SUCCESS!";
	}

	@Override
	@Transactional
	public String updateStudent(long id, StudentRequestDTO studentProfileRequestDTO) {
		StudentProfile studentProfile = iStudentProfileRepository.findByIdAndStatusNot(id, DELETE_STATUS);
		if (studentProfile == null) {
			throw new ResourceNotFoundException();
		}
		
		studentProfile.setDOB(studentProfileRequestDTO.getDoB());
		studentProfile.setGender(studentProfileRequestDTO.getGender());
		studentProfile.setParentName(studentProfileRequestDTO.getParentName());		
		studentProfile.setContact(studentProfileRequestDTO.getContact());
		iStudentProfileRepository.save(studentProfile);
		
		Account account = studentProfile.getAccount();
		account.setFullName(studentProfileRequestDTO.getFullName());
		iAccountRepository.save(account);
		return "UPDATE SUCCESS!";
	}

	@Override
	@Transactional
	public String changeStatusStudent(ListIdAndStatusDTO listIdAndStatusDTO) {
		List<Long> ids = listIdAndStatusDTO.getIds();
		String status = listIdAndStatusDTO.getStatus();
		for (long id : ids) {
			StudentProfile studentProfile = iStudentProfileRepository.findByIdAndStatusNot(id, DELETE_STATUS);
			if (studentProfile == null) {
				throw new ResourceNotFoundException();
			}
		}

		for (long id : ids) {
			StudentProfile studentProfile = iStudentProfileRepository.findByIdAndStatusNot(id, DELETE_STATUS);
			Account account = studentProfile.getAccount();

			if (status.equals(DELETE_STATUS)) {
				Classes deleteClass = iClassRepository.findById(0L).orElseThrow(() -> new ResourceNotFoundException());
				System.out.println(deleteClass.getClassName());
				studentProfile.setClasses(deleteClass);
				System.out.println(deleteClass.getStudentProfileList().size());
				studentProfile.setStudentCount(deleteClass.getStudentProfileList().size() + 1);

				String username = "DEL" + String.format("%05d", deleteClass.getStudentProfileList().size() + 1);
				account.setUsername(username);
			}
			if (status.equals(PENDING_STATUS)) {
				SchoolGrade schoolGrade = studentProfile.getClasses().getSchoolGrade();
				Classes pendingClass = iClassRepository.findBySchoolGradeIdAndClassName(schoolGrade.getId(),
						PENDING_STATUS);
				if (pendingClass == null) {
					pendingClass = new Classes();
					pendingClass.setClassName(PENDING_STATUS);
					pendingClass.setSchoolGrade(schoolGrade);
					pendingClass.setStatus(PENDING_STATUS);
					iClassRepository.save(pendingClass);
				}
				studentProfile.setClasses(pendingClass);
				studentProfile.setStudentCount(pendingClass.getStudentProfileList().size() + 1);

				String username = "PEND" + String.format("%05d", pendingClass.getStudentProfileList().size() + 1);
				account.setUsername(username);
			}
			studentProfile.setStatus(status);
			iStudentProfileRepository.save(studentProfile);

			System.out.println(account.getId());
			System.out.println(status);
			account.setStatus(status);
			iAccountRepository.save(account);
		}

		return "CHANGE SUCCESS!";
	}

	private String generateUsername(String schoolCode, int gradeName, long studentCount) {
		String username = schoolCode + String.format("%02d", gradeName) + String.format("%03d", studentCount);
		return username;
	}

	@Override
	@Transactional
	public String importStudent(MultipartFile file, long schoolId, int gradeId) throws IOException {
		// open file
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
				DELETE_STATUS);

		Iterator<Sheet> sheetIterator = workbook.sheetIterator();
		while (sheetIterator.hasNext()) {
			Sheet sheet = sheetIterator.next();
			Classes classes = new Classes(sheet.getSheetName(), "ACTIVE", schoolGrade);
			iClassRepository.save(classes);

			Iterator<Row> rowIterator = sheet.rowIterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				long accountId = 0;
				if (row.getCell(1) != null) {
					String studentCode = row.getCell(1).getStringCellValue();
					accountId = Long.parseLong(studentCode);
				}
				String fullName = row.getCell(2).getStringCellValue();
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
				String DoB = sdf.format(row.getCell(3).getDateCellValue());
				System.out.println(DoB);
				String gender = row.getCell(4).getStringCellValue();
				String parentName = row.getCell(5).getStringCellValue();
				String contact = row.getCell(6).getStringCellValue();

				StudentRequestDTO studentRequestDTO = new StudentRequestDTO(classes.getId(), fullName, DoB, gender,
						parentName, contact);

				if (accountId == 0) {
					createStudenProfile(studentRequestDTO);
				}else {
					Account account = iAccountRepository.findByIdAndStatusNot(accountId, "DELETED");
					if (account == null) {
						workbook.close();
						throw new ResourceNotFoundException();
					}					
					
					String schoolCode = classes.getSchoolGrade().getSchool().getSchoolCode()
							+ classes.getSchoolGrade().getSchool().getSchoolCount();
					int gradeName = classes.getSchoolGrade().getGrade().getGradeName();
					long studentCount = countStudent(classes);
					String username = generateUsername(schoolCode, gradeName, studentCount);
					account.setUsername(username);
					iAccountRepository.save(account);
					
					StudentProfile studentProfile = account.getStudentProfile();
					studentProfile.setClasses(classes);
					iStudentProfileRepository.save(studentProfile);
				}
			}

		}
		workbook.close();
		return null;
	}

	@Override
	public String validateStudentFile(MultipartFile file, long schoolId, int gradeId) throws IOException {
		// open file
		Workbook workbook = new XSSFWorkbook(file.getInputStream());

		// check schoolGrade existed
		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
				DELETE_STATUS);
		if (schoolGrade == null) {
			throw new ResourceNotFoundException();
		}

		// get all className of schoolGradeId
		List<Classes> classesList = iClassRepository.findBySchoolGradeIdAndStatusNot(schoolGrade.getId(),
				DELETE_STATUS);
		List<String> classNameList = new ArrayList<>();
		if (!classesList.isEmpty()) {
			for (Classes classes : classesList) {
				classNameList.add(classes.getClassName());
			}
		}

		// validate all sheet in excel file
		String error = "";
		Iterator<Sheet> sheetIterator = workbook.sheetIterator();
		while (sheetIterator.hasNext()) {
			Sheet sheet = sheetIterator.next();
			error = validateSheetData(sheet, classNameList);
		}
		workbook.close();

		if (!error.trim().isEmpty()) {
			return error.trim();
		}

		return "OK";

	}

	private String validateSheetData(Sheet sheet, List<String> classNameList) {
		DataFormatter dataFormatter = new DataFormatter();
		String error = "";
		// check className existed
		if (classNameList.contains(sheet.getSheetName())) {
			error += "\nClass with sheet name = " + sheet.getSheetName() + " EXISTED!";
		}
		Iterator<Row> rowIterator = sheet.rowIterator();

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row.getRowNum() == 0) {
				continue;
			}
			for (int i = FIRST_CELL; i < (LAST_CELL + 1); i++) {
				Cell cell = row.getCell(i);
				switch (i) {
				case 1:
					if (cell != null) {
						if (cell.getCellType() != CellType.STRING) {
							error += "\nCell: " + parseToCell(cell.getRowIndex() + 1, i) + " in sheet: "
									+ sheet.getSheetName() + " INVALID!";
						}
					}
					break;

				case 3:
					if (cell == null) {
						error += "\nCell: " + parseToCell(row.getRowNum() + 1, i) + " in sheet: " + sheet.getSheetName()
								+ " INVALID!";
					} else {
						if (cell.getCellType() == CellType.BLANK) {
							error += "\nCell: " + parseToCell(cell.getRowIndex() + 1, i) + " in sheet: "
									+ sheet.getSheetName() + " INVALID!";
						} else {
							if (cell.getCellType() != CellType.NUMERIC) {
								error += "\nCell: " + parseToCell(cell.getRowIndex() + 1, i) + " in sheet: "
										+ sheet.getSheetName() + " INVALID!";
							} else {
								String dateFormat = dataFormatter.formatCellValue(cell);
								String regex = "\\d{1,2}/\\d{1,2}/\\d{2}";
								if (!dateFormat.matches(regex)) {
									error += "\nCell: " + parseToCell(cell.getRowIndex() + 1, i) + " in sheet: "
											+ sheet.getSheetName() + " DATE WRONG FORMAT!";
								} else {
									Date date = new Date();
									if (cell.getDateCellValue().after(date)) {
										error += "\nCell: " + parseToCell(cell.getRowIndex() + 1, i) + " in sheet: "
												+ sheet.getSheetName() + " INVALID!";
									}
								}
							}
						}
					}
					break;

				case 5:
					if (cell != null) {
						if (cell.getCellType() != CellType.STRING) {
							error += "\nCell: " + parseToCell(cell.getRowIndex() + 1, i) + " in sheet: "
									+ sheet.getSheetName() + " INVALID!";
						}
					}
					break;

				case 2:
				case 4:
				case 6:
					if (cell == null) {
						error += "\nCell: " + parseToCell(row.getRowNum() + 1, i) + " in sheet: " + sheet.getSheetName()
								+ " INVALID!";
					} else {
						if (cell.getCellType() == CellType.BLANK) {
							error += "\nCell: " + parseToCell(cell.getRowIndex() + 1, i) + " in sheet: "
									+ sheet.getSheetName() + " INVALID!";
						} else {
							if (cell.getCellType() != CellType.STRING) {
								error += "\nCell: " + parseToCell(cell.getRowIndex() + 1, i) + " in sheet: "
										+ sheet.getSheetName() + " INVALID!";
							}
						}
					}
				}
			}
		}
		return error;
	}

	private String parseToCell(int rowIndex, int columnIndex) {
		String columnName = "";
		switch (columnIndex) {
		case 1:
			columnName = "B";
			break;
		case 2:
			columnName = "C";
			break;
		case 3:
			columnName = "D";
			break;
		case 4:
			columnName = "E";
			break;
		case 5:
			columnName = "F";
			break;
		case 6:
			columnName = "G";
			break;
		}

		return (columnName + rowIndex);
	}

	private long countStudent(Classes classes) {
		List<Classes> classesList = classes.getSchoolGrade().getClassList();
		List<StudentProfile> studentProfileList = new ArrayList<>();
		if (!classesList.isEmpty()) {
			for (Classes classes2 : classesList) {
				if (classes2.getStatus().equals("PENDING")) {
					classesList.remove(classes2);
				} else {
					StudentProfile studentProfile = iStudentProfileRepository
							.findFirstByClassesIdAndStatusLikeOrderByStudentCountDesc(classes2.getId(), "ACTIVE");

					if (studentProfile != null) {
						studentProfileList.add(studentProfile);
					}
				}
			}
		}
//		StudentProfile studentProfile = null;
		long studentCount = 1;
		if (!studentProfileList.isEmpty()) {
			for (StudentProfile studentProfile : studentProfileList) {
				if (studentProfile.getStudentCount() > studentCount) {
					studentCount = studentProfile.getStudentCount();
				}
			}
			studentCount++;
		}
		return studentCount;
	}
}
