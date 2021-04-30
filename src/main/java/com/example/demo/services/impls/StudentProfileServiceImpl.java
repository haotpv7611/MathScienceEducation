package com.example.demo.services.impls;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetViews;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
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
import com.example.demo.models.Exercise;
import com.example.demo.models.ExerciseTaken;
import com.example.demo.models.Grade;
import com.example.demo.models.Lesson;
import com.example.demo.models.ProgressTest;
import com.example.demo.models.School;
import com.example.demo.models.SchoolGrade;
import com.example.demo.models.StudentProfile;
import com.example.demo.models.StudentRecord;
import com.example.demo.models.Subject;
import com.example.demo.models.Unit;
import com.example.demo.repositories.IAccountRepository;
import com.example.demo.repositories.IClassRepository;
import com.example.demo.repositories.IExerciseRepository;
import com.example.demo.repositories.IExerciseTakenRepository;
import com.example.demo.repositories.IGradeRepository;
import com.example.demo.repositories.ILessonRepository;
import com.example.demo.repositories.IProgressTestRepository;
import com.example.demo.repositories.ISchoolGradeRepository;
import com.example.demo.repositories.ISchoolRepository;
import com.example.demo.repositories.IStudentProfileRepository;
import com.example.demo.repositories.IStudentRecordRepository;
import com.example.demo.repositories.ISubjectRepository;
import com.example.demo.repositories.IUnitRepository;
import com.example.demo.services.IClassService;
import com.example.demo.services.IStudentProfileService;

@Service
public class StudentProfileServiceImpl implements IStudentProfileService {
	Logger logger = LoggerFactory.getLogger(StudentProfileServiceImpl.class);
	private final String ACTIVE_STATUS = "ACTIVE";
	private final String INACTIVE_STATUS = "INACTIVE";
	private final String DELETE_STATUS = "DELETED";
	private final String PENDING_STATUS = "PENDING";
	private final String DEFAULT_PASSWORD = "123456";
	private final int STUDENT_ROLE = 3;
	private final int FIRST_STUDENT_ROW = 7;
	private final int FIRST_COLUMN = 1;
	private final int LAST_COLUMN = 6;

	@Autowired
	private ISchoolRepository iSchoolRepository;

	@Autowired
	private IGradeRepository iGradeRepository;

	@Autowired
	private ISchoolGradeRepository iSchoolGradeRepository;

	@Autowired
	private IClassRepository iClassRepository;

	@Autowired
	private IStudentProfileRepository iStudentProfileRepository;

	@Autowired
	private IAccountRepository iAccountRepository;

	@Autowired
	private ISubjectRepository iSubjectRepository;

	@Autowired
	private IProgressTestRepository iProgressTestRepository;

	@Autowired
	private IUnitRepository iUnitRepository;

	@Autowired
	private ILessonRepository iLessonRepository;

	@Autowired
	private IExerciseRepository iExerciseRepository;

	@Autowired
	private IExerciseTakenRepository iExerciseTakenRepository;

	@Autowired
	private IStudentRecordRepository iStudentRecordRepository;

	@Autowired
	private IClassService iClassService;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Object findStudentById(long id) {
		StudentResponseDTO studentResponseDTO = null;
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
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "FIND FAIL!";
		}

		return studentResponseDTO;
	}

	@Override
	public StudentResponseDTO findStudentByAccountId(long accountId) {
		StudentResponseDTO studentResponseDTO = new StudentResponseDTO();
		try {
			Account account = iAccountRepository.findByIdAndStatus(accountId, ACTIVE_STATUS);
			if (account != null) {
				StudentProfile studentProfile = account.getStudentProfile();
				String className = studentProfile.getClasses().getClassName();
				String schoolName = studentProfile.getClasses().getSchoolGrade().getSchool().getSchoolName();
				String fullName = account.getFullName();
				String studentId = "MJ" + String.format("%06d", studentProfile.getId());

				studentResponseDTO.setClassName(className);
				studentResponseDTO.setSchoolName(schoolName);
				studentResponseDTO.setFullName(fullName);
				studentResponseDTO.setStudentId(studentId);
			}
		} catch (Exception e) {
			logger.error("FIND: student by accountId = " + accountId + "! " + e.getMessage());

			return null;
		}

		return studentResponseDTO;
	}

	// if school existed --> find all grade linked
	@Override
	public List<StudentResponseDTO> findStudentByListId(List<Long> ids) {
		long schoolId = ids.get(0);
		int gradeId = Math.toIntExact(ids.get(1));
		long classId = ids.get(2);

		List<StudentResponseDTO> studentResponseDTOList = new ArrayList<>();
		if (schoolId != 0) {
			try {
				// check school existed
				School school = iSchoolRepository.findByIdAndStatusNot(schoolId, DELETE_STATUS);
				String schoolName = "";
				// if existed: get schoolName
				if (school != null) {
					schoolName = school.getSchoolName();

					SchoolGradeDTO schoolGradeDTO = null;
					List<ClassResponseDTO> classResponseDTOList = new ArrayList<>();

					// find if only have schoolId
					if (gradeId == 0 && classId == 0) {
						// get all grade linked
						List<SchoolGrade> schoolGradeList = iSchoolGradeRepository.findBySchoolIdAndStatusNot(schoolId,
								DELETE_STATUS);

						if (!schoolGradeList.isEmpty()) {
							List<Classes> classesList = new ArrayList<>();
							for (SchoolGrade schoolGrade : schoolGradeList) {
								classesList.addAll(
										iClassRepository.findBySchoolGradeIdAndStatusNotOrderByStatusAscClassNameAsc(
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
						Grade grade = iGradeRepository.findById(gradeId)
								.orElseThrow(() -> new ResourceNotFoundException());
						int gradeName = grade.getGradeName();
						if (classId == 0) {
							schoolGradeDTO = new SchoolGradeDTO(schoolId, gradeId);

							// get all class with schoolGradeId
							classResponseDTOList = iClassService.findBySchoolGradeId(schoolGradeDTO);
							if (!classResponseDTOList.isEmpty()) {
								for (ClassResponseDTO classResponseDTO : classResponseDTOList) {
									// get all student
									studentResponseDTOList.addAll(
											findStudentByClassedId(classResponseDTO.getId(), schoolName, gradeName));
								}
							}
						}
						// find if have classesId
						if (classId != 0) {
							studentResponseDTOList.addAll(findStudentByClassedId(classId, schoolName, gradeName));
						}
					}
				}
			} catch (Exception e) {
				logger.error("FIND: student by schoolId = " + schoolId + ", gradeId = " + gradeId + " and classId = "
						+ classId + "! " + e.getMessage());

				return null;
			}
		}

		return studentResponseDTOList;
	}

	private List<StudentResponseDTO> findStudentByClassedId(long classesId, String schoolName, int gradeName) {
		List<StudentResponseDTO> studentResponseDTOList = new ArrayList<>();
		try {
			Classes classes = iClassRepository.findByIdAndStatusNot(classesId, DELETE_STATUS);
			if (classes != null) {
				String className = classes.getClassName();

				List<StudentProfile> studentProfileList = iStudentProfileRepository
						.findByClassesIdAndStatusNot(classesId, DELETE_STATUS);
				for (StudentProfile studentProfile : studentProfileList) {
					Account account = studentProfile.getAccount();
					StudentResponseDTO studentResponseDTO = modelMapper.map(account, StudentResponseDTO.class);
					studentResponseDTO.setId(studentProfile.getId());
					studentResponseDTO.setStudentId("MJ" + String.format("%06d", studentProfile.getId()));
					studentResponseDTO.setSchoolName(schoolName);
					studentResponseDTO.setGradeName(gradeName);
					studentResponseDTO.setClassName(className);
					studentResponseDTO.setGender(studentProfile.getGender());
					studentResponseDTOList.add(studentResponseDTO);
				}
			}
		} catch (Exception e) {
			logger.error("FIND: student by classesId = " + classesId + "! " + e.getMessage());

			return null;
		}

		return studentResponseDTOList;
	}

	// classId --> schoolGradeId --> gradeName --> schoolCode
	// schoolGradeId --> all className != pending --> all student active or inactive
	// --> count max + 1
	@Override
	@Transactional
	public String createStudenProfile(StudentRequestDTO studentRequestDTO) {
		long classId = studentRequestDTO.getClassesId();
		try {
			Classes classes = iClassRepository.findByIdAndStatusNot(classId, DELETE_STATUS);
			if (classes == null) {
				throw new ResourceNotFoundException();
			}
			if (classes.getStatus().equalsIgnoreCase(ACTIVE_STATUS)) {
				List<StudentProfile> studentProfileList = iStudentProfileRepository.findByClassesIdAndStatus(classId,
						ACTIVE_STATUS);
				studentProfileList.addAll(iStudentProfileRepository.findByClassesIdAndStatus(classId, INACTIVE_STATUS));

				if (studentProfileList.size() > 60) {

					return "EXCEED LIMIT";
				}

				String username = generateUsername(classes);
				if (iAccountRepository.findByUsername(username) != null) {

					return "EXISTED";
				}
				String fullName = studentRequestDTO.getFullName().trim().replaceAll("\\s+", " ");
				Account account = new Account(username, DEFAULT_PASSWORD, fullName, STUDENT_ROLE, ACTIVE_STATUS);
				iAccountRepository.save(account);

				String DoB = studentRequestDTO.getDoB();
				String gender = studentRequestDTO.getGender().trim().replaceAll("\\s+", " ");
				String parentName = studentRequestDTO.getParentName().trim().replaceAll("\\s+", " ");
				String contact = studentRequestDTO.getContact().trim().replaceAll("\\s+", " ");
				StudentProfile studentProfile = new StudentProfile(DoB, gender, parentName, contact, ACTIVE_STATUS,
						countStudent(classes), account, classes);
				iStudentProfileRepository.save(studentProfile);
			} else {

				return "CANNOT CREATE!";
			}
		} catch (Exception e) {
			logger.error("CREATE: student in classId =  " + classId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CREATE FAIL!";
		}

		return "CREATE SUCCESS!";
	}

	@Override
	@Transactional
	public String updateStudent(long id, StudentRequestDTO studentProfileRequestDTO) {
		try {
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
		} catch (Exception e) {
			logger.error("UPDATE: studentId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "UPDATE FAIL!";
		}

		return "UPDATE SUCCESS!";
	}

	@Override
	@Transactional
	public String changeStatusStudent(ListIdAndStatusDTO listIdAndStatusDTO) {
		List<Long> ids = listIdAndStatusDTO.getIds();
		String status = listIdAndStatusDTO.getStatus();
		for (long id : ids) {
			String response = changeStatusOneStudent(id, status);
			if (!response.equalsIgnoreCase("OK")) {

				return response;
			}
		}

		return "CHANGE SUCCESS!";
	}

	// active <--> inactive
	// active, pending --> deleted, inactive --X--> deleted
	// graduate: active, inactive --> pending, class --> inactive
	// active và inactive thì k thay đổi username
	// delete và pending thì thay đổi username và classesId
	@Override
	public String changeStatusOneStudent(long id, String status) {
		try {
			StudentProfile studentProfile = iStudentProfileRepository.findByIdAndStatusNot(id, DELETE_STATUS);
			if (studentProfile == null) {
				throw new ResourceNotFoundException();
			}

			Account account = studentProfile.getAccount();
			if (status.contains(ACTIVE_STATUS)) {
				account.setStatus(status);
				iAccountRepository.save(account);

				studentProfile.setStatus(status);
				iStudentProfileRepository.save(studentProfile);
			}
			if (status.equalsIgnoreCase(DELETE_STATUS)) {
				if (iStudentProfileRepository.findByIdAndStatus(id, ACTIVE_STATUS) != null) {

					return "CANNOT DELETE";
				}

				Classes deleteClass = iClassRepository.findById(0L).orElseThrow(() -> new ResourceNotFoundException());
				studentProfile.setClasses(deleteClass);
				studentProfile.setStudentCount(countStudent(deleteClass));

				String username = generateUsernameDELPEND(DELETE_STATUS, deleteClass);
				if (iAccountRepository.findByUsername(username) != null) {

					return "EXISTED";
				}
				account.setUsername(username);
				account.setStatus(status);
				iAccountRepository.save(account);

				studentProfile.setStatus(status);
				iStudentProfileRepository.save(studentProfile);
			}
			if (status.equalsIgnoreCase(PENDING_STATUS)) {
				SchoolGrade schoolGrade = studentProfile.getClasses().getSchoolGrade();
				Classes pendingClass = iClassRepository.findBySchoolGradeIdAndClassNameIgnoreCaseAndStatusNot(
						schoolGrade.getId(), PENDING_STATUS, DELETE_STATUS);
				if (pendingClass == null) {
					pendingClass = new Classes(PENDING_STATUS, PENDING_STATUS, schoolGrade);
					iClassRepository.save(pendingClass);
				}
				studentProfile.setClasses(pendingClass);
				studentProfile.setStudentCount(countStudent(pendingClass));

				String username = generateUsernameDELPEND(PENDING_STATUS, pendingClass);
				if (iAccountRepository.findByUsername(username) != null) {

					return "EXISTED";
				}
				account.setUsername(username);
				account.setStatus(status);
				iAccountRepository.save(account);

				studentProfile.setStatus(status);
				iStudentProfileRepository.save(studentProfile);
			}
		} catch (Exception e) {
			logger.error("Change status studentId = " + id + "with status = " + status + "! " + e.getMessage());
			throw e;
		}

		return "OK";
	}

	// chuyển giữa các lớp đang active, chuyển từ lớp pending sang lớp active
	// cùng khối, lớp cũ k phải lớp pending thì giữ nguyên account
	// khác khối hoặc pending thì đổi account
	@Override
	public Map<String, List<Long>> changeClassForStudent(List<Long> studentIdList, long classesId) {
		Map<String, List<Long>> map = new HashedMap<>();
		try {
			Classes newClasses = iClassRepository.findByIdAndStatusNot(classesId, DELETE_STATUS);
			if (newClasses == null) {
				throw new ResourceNotFoundException();
			}
			Grade newGrade = newClasses.getSchoolGrade().getGrade();

			List<Long> studentIdExisted = new ArrayList<>();
			for (long studentId : studentIdList) {
				StudentProfile studentProfile = iStudentProfileRepository.findByIdAndStatusNot(studentId,
						DELETE_STATUS);
				if (studentProfile == null) {
					throw new ResourceNotFoundException();
				}

				List<StudentProfile> studentProfileList = iStudentProfileRepository.findByClassesIdAndStatus(classesId,
						ACTIVE_STATUS);
				studentProfileList
						.addAll(iStudentProfileRepository.findByClassesIdAndStatus(classesId, INACTIVE_STATUS));
				if (studentProfileList.size() + studentIdList.size() > 60) {

					map.put("EXCEED LIMIT", null);
					return map;
				}

				Grade oldGrade = studentProfile.getClasses().getSchoolGrade().getGrade();
				if (oldGrade.equals(newGrade)) {
					if (studentProfile.getClasses().getClassName().equalsIgnoreCase("PENDING")) {
						String username = generateUsername(newClasses);
						if (iAccountRepository.findByUsername(username) != null) {
							map.put("EXISTED", null);
							return map;
						}
						Account account = studentProfile.getAccount();
						account.setUsername(username);
						account.setStatus(ACTIVE_STATUS);
						iAccountRepository.save(account);

						studentProfile.setStudentCount(countStudent(newClasses));
						studentProfile.setClasses(newClasses);
						studentProfile.setStatus(ACTIVE_STATUS);
						iStudentProfileRepository.save(studentProfile);
					} else {
						if (studentProfile.getClasses().getId() == classesId) {
							studentIdExisted.add(studentProfile.getId());
						} else {
							studentProfile.setClasses(newClasses);
							iStudentProfileRepository.save(studentProfile);
						}
					}

				} else {
					String username = generateUsername(newClasses);
					if (iAccountRepository.findByUsername(username) != null) {

						map.put("EXISTED", null);
						return map;
					}
					Account account = studentProfile.getAccount();
					account.setUsername(username);
					account.setStatus(ACTIVE_STATUS);
					iAccountRepository.save(account);

					studentProfile.setClasses(newClasses);
					studentProfile.setStudentCount(countStudent(newClasses));
					studentProfile.setStatus(ACTIVE_STATUS);
					iStudentProfileRepository.save(studentProfile);
				}
			}
			if (studentIdExisted.isEmpty()) {
				map.put("CHANGE SUCCESS!", null);
			} else {
				map.put("HAVE PROBLEM", studentIdExisted);
			}
		} catch (Exception e) {
			logger.error("Change studentListId =  " + studentIdList.toString() + " to classId = " + classesId + "! "
					+ e.getMessage());
			throw e;
		}

		return map;
	}

	@Override
	public Map<String, Workbook> validateStudentFile(MultipartFile file, long schoolId, int gradeId)
			throws ParseException {
		// open file
		Map<String, Workbook> response = new HashedMap<>();

		try {
			// check schoolGrade existed
			SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
					DELETE_STATUS);
			if (schoolGrade == null) {
				throw new ResourceNotFoundException();
			}
			School school = schoolGrade.getSchool();
			int gradeName = schoolGrade.getGrade().getGradeName();
			String schoolName = school.getSchoolName();
			String schoolCode = school.getSchoolCode() + school.getSchoolCount();
//			List<Classes> classesList = iClassRepository.findBySchoolGradeIdAndStatusNot(schoolGrade.getId(),
//					DELETE_STATUS);

			// validate all sheet in excel file
			Workbook workbook = new XSSFWorkbook(file.getInputStream());
			Iterator<Sheet> sheetIterator = workbook.sheetIterator();
			List<Cell> cellList = new ArrayList<>();
			while (sheetIterator.hasNext()) {
				Sheet sheet = sheetIterator.next();
				int countStudentImport = countStudentImport(sheet);
				Classes classes = iClassRepository.findBySchoolGradeIdAndClassNameIgnoreCaseAndStatusNot(
						schoolGrade.getId(), sheet.getSheetName(), DELETE_STATUS);
				List<StudentProfile> studentProfileList = new ArrayList<>();
				if (classes != null) {
					studentProfileList
							.addAll(iStudentProfileRepository.findByClassesIdAndStatus(classes.getId(), ACTIVE_STATUS));
					studentProfileList.addAll(
							iStudentProfileRepository.findByClassesIdAndStatus(classes.getId(), INACTIVE_STATUS));
				}

				if (countStudentImport + studentProfileList.size() > 60) {
					response.put("EXCEED LIMIT", null);
				}

				cellList = validateSheetData(sheet, schoolName, schoolCode, gradeName);
				if (!cellList.isEmpty()) {

					CellStyle cellStyle = formatErrorCell(workbook);
					for (Cell cell : cellList) {
						cell.setCellStyle(cellStyle);
					}

					// set view default sheet index = 1, view A1, cell active A1
					XSSFSheet tempSheet = (XSSFSheet) sheet;
					CTWorksheet ctWorksheet = tempSheet.getCTWorksheet();
					CTSheetViews ctSheetViews = ctWorksheet.getSheetViews();
					CTSheetView ctSheetView = ctSheetViews.getSheetViewArray(ctSheetViews.sizeOfSheetViewArray() - 1);
					ctSheetView.setTopLeftCell("A1");
					sheet.setActiveCell(new CellAddress("A1"));
					workbook.setActiveSheet(0);

				}
			}

			if (!cellList.isEmpty()) {
				response.put("ERROR", workbook);
			} else {
				response.put("OK", null);
//				workbook.close();
			}
			String fileName = file.getOriginalFilename();
			FileOutputStream fileOut = new FileOutputStream("E:\\" + "Error-" + fileName);
			workbook.write(fileOut);
			fileOut.close();

		} catch (Exception e) {
			logger.error(
					"Validate file with schoolId = " + schoolId + " and gradeId" + gradeId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				response.put("NOT FOUND", null);
			} else {

				response.put("FAIL", null);
			}
		}

		return response;
	}

	private int countStudentImport(Sheet sheet) {
		int countStudentImport = 0;

		Iterator<Row> rowIterator = sheet.rowIterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			int totalEmptyCell = 0;
			for (int i = FIRST_COLUMN; i < (LAST_COLUMN + 1); i++) {
				Cell cell = row.getCell(i);

				if (cell == null) {
					totalEmptyCell++;
				} else {
					if (cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty()) {
						totalEmptyCell++;
					}
				}
			}
			if (totalEmptyCell == 6) {
				continue;
			} else {
				countStudentImport++;
			}
		}
		return countStudentImport;
	}

	private List<Cell> validateSheetData(Sheet sheet, String schoolName, String schoolCode, int gradeName)
			throws ParseException {
		List<Cell> cellList = new ArrayList<>();
		try {

			Iterator<Row> rowIterator = sheet.rowIterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				// check schoolCode
				if (row.getRowNum() == 1) {
					Cell cell = row.getCell(3);
					if (cell == null) {
						cellList.add(cell);
					} else {
						if (cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty()) {
							cellList.add(cell);
						} else {
							if (cell.getCellType() != CellType.STRING) {
								cellList.add(cell);
							} else {
								if (!cell.getStringCellValue().equalsIgnoreCase(schoolName)) {
									cellList.add(cell);
								}
							}
						}
					}

				}

				// check schoolCode
				if (row.getRowNum() == 2) {
					Cell cell = row.getCell(3);
					if (cell == null) {
						cellList.add(cell);
					} else {
						if (cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty()) {
							cellList.add(cell);
						} else {
							if (cell.getCellType() != CellType.STRING) {
								cellList.add(cell);
							} else {
								if (!cell.getStringCellValue().equalsIgnoreCase(schoolCode)) {
									cellList.add(cell);
								}
							}
						}
					}

				}
				// check gradeName
				if (row.getRowNum() == 3) {
					Cell cell = row.getCell(3);
					if (cell == null) {
						cellList.add(cell);
					} else {
						if (cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty()) {
							cellList.add(cell);
						} else {
							if (cell.getCellType() == CellType.STRING) {
								if (!cell.getStringCellValue().equalsIgnoreCase(String.valueOf(gradeName))) {
									cellList.add(cell);
								}
							} else if (cell.getCellType() == CellType.NUMERIC) {
								if (cell.getNumericCellValue() != gradeName) {
									cellList.add(cell);
								}
							} else {
								cellList.add(cell);
							}
						}
					}
				}

				if (row.getRowNum() < FIRST_STUDENT_ROW) {
					continue;
				}

				// if all cell in row is empty: continue
				int totalEmptyCell = 0;
				for (int i = FIRST_COLUMN; i < (LAST_COLUMN + 1); i++) {

					Cell cell = row.getCell(i);

					if (cell == null) {
						totalEmptyCell++;
					} else {
						if (cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty()) {
							totalEmptyCell++;
						}
					}
				}
				if (totalEmptyCell == 6) {
					continue;
				}

				for (int i = FIRST_COLUMN; i < (LAST_COLUMN + 1); i++) {
					Cell cell = row.getCell(i);
					switch (i) {
					case 1:
						if (cell != null) {
							if (cell.getCellType() != CellType.BLANK || cell.toString().trim().isEmpty() == false) {
								String studentIdRegex = "^MJ\\d{6}$";
								if (cell.getCellType() == CellType.STRING) {
									boolean checkStudentIdFormat = cell.getStringCellValue().matches(studentIdRegex);
									if (checkStudentIdFormat == false) {
										cellList.add(cell);
									}
								} else {
									cellList.add(cell);
								}
							}
						}

						break;

					case 3:
						// cell null or blank or not date type is string and numberic or date > current
						// is invalid
						if (cell == null) {
							cellList.add(cell);
						} else {
							if (cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty()) {
								cellList.add(cell);
							} else {
								// nếu copy paste định dạng sẽ là string
								// hỗ trợ 2 format dd/MM/yyyy và dd-MM-yyyy
								// parse string to date và kiểm tra
								if (cell.getCellType() == CellType.STRING) {
									String dateFormatRegex1 = "\\d{1,2}/\\d{1,2}/\\d{4}";
									String dateFormatRegex2 = "\\d{1,2}-\\d{1,2}-\\d{4}";
									SimpleDateFormat simpleDateFormat = null;
									if (cell.toString().trim().matches(dateFormatRegex1)) {
										simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
									}
									if (cell.toString().trim().matches(dateFormatRegex2)) {
										simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
									}
									if (simpleDateFormat == null) {
										cellList.add(cell);
									} else {
										simpleDateFormat.setLenient(false);
										try {
											Date date = simpleDateFormat.parse(cell.toString().trim());
											if (date.after(new Date())) {
												cellList.add(cell);
											}
										} catch (ParseException pe) {
											cellList.add(cell);
										}
									}

								} else if (cell.getCellType() == CellType.NUMERIC) {
									if (HSSFDateUtil.isCellDateFormatted(cell) == false) {
										cellList.add(cell);
									}
									Date date = new Date();
									if (cell.getDateCellValue().after(date)) {
										cellList.add(cell);
									}

								} else {
									cellList.add(cell);
								}
							}
						}
						break;

					case 2:
					case 4:
					case 5:
						// cell null or blank or not String type or have number is invalid
						if (cell == null) {
							cellList.add(cell);
						} else {
							if (cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty()) {
								cellList.add(cell);
							} else {
								if (cell.getCellType() != CellType.STRING) {
									cellList.add(cell);
								} else {
									if (!cell.getStringCellValue().matches("^[\\p{L} .'-]+$")) {
										cellList.add(cell);
									}
								}
							}
						}
						break;
					case 6:
						// cell null or blank or not String type is invalid
						if (cell == null) {
							cellList.add(cell);
						} else {
							if (cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty()) {
								cellList.add(cell);
							} else {
								if (cell.getCellType() != CellType.STRING) {
									cellList.add(cell);
								}
							}
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.error("Validate file data input! " + e.getMessage());
			throw e;
		}

		return cellList;

	}

	@Override
	@Transactional
	public Map<String, Workbook> importStudent(MultipartFile file, long schoolId, int gradeId) {
		Map<String, Workbook> response = new HashedMap<>();

		// open file
		try {
			SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
					DELETE_STATUS);
			Workbook workbook = new XSSFWorkbook(file.getInputStream());
			Iterator<Sheet> sheetIterator = workbook.sheetIterator();
			List<Cell> cellList = new ArrayList<>();
			// create class by each sheetName
			while (sheetIterator.hasNext()) {
				Sheet sheet = sheetIterator.next();

				Classes classes = iClassRepository.findBySchoolGradeIdAndClassNameIgnoreCaseAndStatusNot(
						schoolGrade.getId(), sheet.getSheetName(), DELETE_STATUS);
				if (classes == null) {
					classes = new Classes(sheet.getSheetName(), ACTIVE_STATUS, schoolGrade);
					iClassRepository.save(classes);
				}
				Iterator<Row> rowIterator = sheet.rowIterator();

				while (rowIterator.hasNext()) {

					Row row = rowIterator.next();

					if (row.getRowNum() < FIRST_STUDENT_ROW) {
						continue;
					} else {
						// get accountId user input
						long studentId = 0;
						if (row.getCell(1) != null) {
							if (row.getCell(1).getCellType() != CellType.BLANK) {
								String studentCode = row.getCell(1).getStringCellValue();
								studentId = Long.parseLong(studentCode.substring(2, studentCode.length()));
							}
						}

						// if not existed --> create new
						// else update username and classesId
						if (studentId == 0) {
							String fullName = row.getCell(2).getStringCellValue();
							fullName = fullName.trim().replaceAll("\\s+", " ");
							String DoB = "";
							System.out.println(row.getCell(3).toString());
							if (row.getCell(3).getCellType() == CellType.STRING) {
								DoB = row.getCell(3).getStringCellValue();
							} else if (row.getCell(3).getCellType() == CellType.NUMERIC) {
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								DoB = sdf.format(row.getCell(3).getDateCellValue());
							}
							String gender = row.getCell(4).getStringCellValue();
							String parentName = row.getCell(5).getStringCellValue();
							parentName = parentName.trim().replaceAll("\\s+", " ");
							String contact = row.getCell(6).getStringCellValue();

							StudentRequestDTO studentRequestDTO = new StudentRequestDTO(classes.getId(), fullName, DoB,
									gender, parentName, contact);
							createStudenProfile(studentRequestDTO);
						} else {
							StudentProfile studentProfile = iStudentProfileRepository.findByIdAndStatusNot(studentId,
									DELETE_STATUS);
							if (studentProfile == null) {
								cellList.add(row.getCell(1));

								continue;
							} else {

								String username = generateUsername(classes);
								if (iAccountRepository.findByUsername(username) != null) {
									response.put("EXISTED", null);
									return response;
								}
								Account account = studentProfile.getAccount();
								account.setUsername(username);
								iAccountRepository.save(account);

								studentProfile.setClasses(classes);
								studentProfile.setStudentCount(countStudent(classes));
								iStudentProfileRepository.save(studentProfile);
							}
						}
					}
				}

				if (!cellList.isEmpty()) {
					CellStyle cellStyle = formatErrorCell(workbook);
					for (Cell cell : cellList) {
						cell.setCellStyle(cellStyle);
					}
				}
			}

			if (!cellList.isEmpty()) {
				response.put("ERROR", workbook);
			} else {
				response.put("IMPORT SUCCESS", null);
//				workbook.close();
			}
			String fileName = file.getOriginalFilename();
			FileOutputStream fileOut = new FileOutputStream("E:\\" + "Not existed StudentId-" + fileName);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			logger.error("Import file with schoolId = " + schoolId + " and gradeId " + gradeId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {
				response.put("NOT FOUND", null);
			} else {
				response.put("IMPORT FAIL", null);
			}
		}

		return response;
	}

	@Override
	public Map<String, Workbook> exportStudentAccount(long schoolId, int gradeId) {
		Map<String, Workbook> response = new HashedMap<>();
		try {
			School school = iSchoolRepository.findByIdAndStatusNot(schoolId, DELETE_STATUS);
			if (school == null) {
				throw new ResourceNotFoundException();
			}
			System.out.println(school.getId());
			Grade grade = iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());

			String schoolName = school.getSchoolName();
			String schoolCode = school.getSchoolCode() + school.getSchoolCount();
			int gradeName = grade.getGradeName();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			String exportDate = sdf.format(new Date());

			SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
					DELETE_STATUS);
			System.out.println(schoolGrade);
			if (schoolGrade != null) {
				List<Classes> classesList = iClassRepository
						.findBySchoolGradeIdAndStatusOrderByClassName(schoolGrade.getId(), ACTIVE_STATUS);

				if (!classesList.isEmpty()) {
					Workbook workbook = new XSSFWorkbook();
					for (Classes classes : classesList) {
						Sheet sheet = workbook.createSheet(classes.getClassName());
						sheet.setColumnWidth(0, 1500);
						sheet.setColumnWidth(1, 3500);
						sheet.setColumnWidth(2, 9000);
						sheet.setColumnWidth(3, 9000);
						sheet.setColumnWidth(4, 5000);

						for (int i = 0; i < 7; i++) {
							if (i == 5) {
								continue;
							}
							sheet.createRow(i);
						}

						// create Table Information
						createArrangeInfoCell(workbook, sheet, sheet.getRow(0), 2, 0, 0, 2, 4,
								"STUDENT ACCOUNT EXPORT TABLE", 0);
						createOneInfoCell(workbook, sheet.getRow(1), 2, "School Name:");
						createArrangeInfoCell(workbook, sheet, sheet.getRow(1), 3, 1, 1, 3, 4, schoolName, 0);
						createOneInfoCell(workbook, sheet.getRow(2), 2, "School Code:");
						createArrangeInfoCell(workbook, sheet, sheet.getRow(2), 3, 2, 2, 3, 4, schoolCode, 0);
						createOneInfoCell(workbook, sheet.getRow(3), 2, "Grade:");
						createArrangeInfoCell(workbook, sheet, sheet.getRow(3), 3, 3, 3, 3, 4, "", gradeName);
						createOneInfoCell(workbook, sheet.getRow(4), 2, "Export Date:");
						createArrangeInfoCell(workbook, sheet, sheet.getRow(4), 3, 4, 4, 3, 4, exportDate, 0);

						// create Header
						createOneHeaderCell(workbook, sheet.getRow(6), 0, "No.");
						createOneHeaderCell(workbook, sheet.getRow(6), 1, "StudentID");
						createOneHeaderCell(workbook, sheet.getRow(6), 2, "Fullname");
						createOneHeaderCell(workbook, sheet.getRow(6), 3, "Username");
						createOneHeaderCell(workbook, sheet.getRow(6), 4, "Password");

						List<StudentProfile> studentProfileList = iStudentProfileRepository
								.findByClassesIdAndStatus(classes.getId(), ACTIVE_STATUS);
						if (!studentProfileList.isEmpty()) {
							for (int i = 7; i < studentProfileList.size() + 7; i++) {
								sheet.createRow(i);
							}
							for (int i = 0; i < studentProfileList.size(); i++) {
								Cell noValueCell = createOneNormalCell(workbook, sheet.getRow(i + 7), 0,
										CellType.NUMERIC, HorizontalAlignment.CENTER);
								noValueCell.setCellValue(i + 1);

								Cell studentIdValueCell = createOneNormalCell(workbook, sheet.getRow(i + 7), 1,
										CellType.STRING, HorizontalAlignment.CENTER);
								studentIdValueCell
										.setCellValue("MJ" + String.format("%06d", studentProfileList.get(i).getId()));

								Cell fullNameValueCell = createOneNormalCell(workbook, sheet.getRow(i + 7), 2,
										CellType.STRING, HorizontalAlignment.LEFT);
								fullNameValueCell.setCellValue(studentProfileList.get(i).getAccount().getFullName());

								Cell usernameValueCell = createOneNormalCell(workbook, sheet.getRow(i + 7), 3,
										CellType.STRING, HorizontalAlignment.CENTER);
								usernameValueCell.setCellValue(studentProfileList.get(i).getAccount().getUsername());

								Cell passwordValueCell = createOneNormalCell(workbook, sheet.getRow(i + 7), 4,
										CellType.STRING, HorizontalAlignment.CENTER);
								passwordValueCell.setCellValue(studentProfileList.get(i).getAccount().getPassword());
							}
						}
						response.put("EXPORT SUCCESS!", workbook);
						FileOutputStream fileOut = new FileOutputStream(
								"E:\\" + schoolName + "-" + gradeName + "-Student Account.xlsx");
						workbook.write(fileOut);
						fileOut.close();
					}
				} else {
					System.out.println("class");
					response.put("EXPORT FAIL", null);
				}
			} else {
				System.out.println("schoolgrade");
				response.put("EXPORT FAIL", null);
			}
		} catch (Exception e) {
			logger.error("Export student account with schoolId = " + schoolId + " and gradeId " + gradeId + "! "
					+ e.getMessage());
			if (e instanceof ResourceNotFoundException) {
				response.put("NOT FOUND", null);
			} else {
				response.put("EXPORT FAIL", null);
			}
		}

		return response;
	}

	// tìm tất cả các lớp dựa trên schoolId và gradeId --> tất cả hs
	// tìm tất cả các unit, lesson, exercise, progressTest dựa trên subjectId
	// in ra điểm trung bình từng exericse của từng hs, nếu chưa làm in ra not yet
	@Override
	public Map<String, Workbook> exportScoreBySubjectId(long schoolId, int gradeId, long subjectId) {

		Map<String, Workbook> response = new HashedMap<>();
		try {

			// validate data input
			School school = iSchoolRepository.findByIdAndStatusNot(schoolId, DELETE_STATUS);
			if (school == null) {
				throw new ResourceNotFoundException();
			}
			Grade grade = iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());
			Subject subject = iSubjectRepository.findByIdAndIsDisableFalse(subjectId);
			if (subject == null) {
				throw new ResourceNotFoundException();
			}

			SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
					DELETE_STATUS);
			if (schoolGrade != null) {
				List<Classes> classesList = iClassRepository
						.findBySchoolGradeIdAndStatusNotOrderByStatusAscClassNameAsc(schoolGrade.getId(),
								DELETE_STATUS);
				if (!classesList.isEmpty()) {
					for (int i = 0; i < classesList.size(); i++) {
						if (classesList.get(i).getClassName().equals(PENDING_STATUS)) {
							classesList.remove(classesList.get(i));
							break;
						}
					}
				}

				// header value content
				String schoolName = school.getSchoolName();
				String schoolCode = school.getSchoolCode() + school.getSchoolCount();
				int gradeName = grade.getGradeName();
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				String exportDate = sdf.format(new Date());
				String subjectName = subject.getSubjectName();

				List<Unit> unitList = iUnitRepository.findBySubjectIdAndIsDisableFalseOrderByUnitNameAsc(subjectId);
				List<Exercise> exerciseList = new ArrayList<>();
				Map<String, Integer> unitMap = new LinkedHashMap<>();
				Map<Lesson, Integer> lessonMap = new LinkedHashMap<>();
				if (!unitList.isEmpty()) {
					for (Unit unit : unitList) {
						int totalExercise = 0;
						List<Lesson> lessonList = new ArrayList<>();
						lessonList.addAll(
								iLessonRepository.findByUnitIdAndIsDisableFalseOrderByLessonNameAsc(unit.getId()));
						if (!lessonList.isEmpty()) {
							for (Lesson lesson : lessonList) {
								int exerciseSize = iExerciseRepository
										.findByLessonIdAndStatusNotOrderByExerciseNameAsc(lesson.getId(), DELETE_STATUS)
										.size();

								if (exerciseSize > 0) {
									exerciseList.addAll(
											iExerciseRepository.findByLessonIdAndStatusNotOrderByExerciseNameAsc(
													lesson.getId(), DELETE_STATUS));
									totalExercise += exerciseSize;
									lessonMap.put(lesson, exerciseSize);
								}
							}

							if (totalExercise > 0) {
								unitMap.put("Unit " + unit.getUnitName(), totalExercise);
							}
						}
					}
				}

				Map<String, Integer> progressTestMap = new LinkedHashMap<>();
				List<Exercise> exercisePTList = new ArrayList<>();
				List<ProgressTest> progressTestList = iProgressTestRepository
						.findBySubjectIdAndIsDisableFalse(subjectId);
				if (!progressTestList.isEmpty()) {
					for (ProgressTest progressTest : progressTestList) {
						int exerciseSize = iExerciseRepository.findByProgressTestIdAndStatusNotOrderByExerciseNameAsc(
								progressTest.getId(), DELETE_STATUS).size();
						if (exerciseSize > 0) {
							exercisePTList
									.addAll(iExerciseRepository.findByProgressTestIdAndStatusNotOrderByExerciseNameAsc(
											progressTest.getId(), DELETE_STATUS));
							progressTestMap.put(progressTest.getProgressTestName(), exerciseSize);
						}
					}
				}

				Workbook workbook = new XSSFWorkbook();
				if (!classesList.isEmpty()) {
					for (Classes classes : classesList) {
						List<StudentProfile> studentProfileList = new ArrayList<>();

						if (!iStudentProfileRepository.findByClassesIdAndStatusNot(classes.getId(), DELETE_STATUS)
								.isEmpty()) {
							studentProfileList.addAll(iStudentProfileRepository
									.findByClassesIdAndStatusNot(classes.getId(), DELETE_STATUS));
						}

						Sheet sheet = workbook.createSheet(classes.getClassName());
						sheet.setColumnWidth(0, 1500);
						sheet.setColumnWidth(1, 3500);
						sheet.setColumnWidth(2, 9000);
						for (int i = 3; i < exerciseList.size() + exercisePTList.size() + 3; i++) {
							sheet.setColumnWidth(i, 3500);
						}

						for (int i = 0; i < studentProfileList.size() + 10; i++) {
							if (i == 6) {
								continue;
							}
							sheet.createRow(i);
						}

						createArrangeInfoCell(workbook, sheet, sheet.getRow(0), 2, 0, 0, 2, 5, "SCORE EXPORT TABLE", 0);

						// create Table Information
						createOneInfoCell(workbook, sheet.getRow(1), 2, "School Name:");
						createArrangeInfoCell(workbook, sheet, sheet.getRow(1), 3, 1, 1, 3, 5, schoolName, 0);
						createOneInfoCell(workbook, sheet.getRow(2), 2, "School Code:");
						createArrangeInfoCell(workbook, sheet, sheet.getRow(2), 3, 2, 2, 3, 5, schoolCode, 0);
						createOneInfoCell(workbook, sheet.getRow(3), 2, "Grade:");
						createArrangeInfoCell(workbook, sheet, sheet.getRow(3), 3, 3, 3, 3, 5, "", gradeName);
						createOneInfoCell(workbook, sheet.getRow(4), 2, "Export Date:");
						createArrangeInfoCell(workbook, sheet, sheet.getRow(4), 3, 4, 4, 3, 5, exportDate, 0);
						createOneInfoCell(workbook, sheet.getRow(5), 2, "Subject Name:");
						createArrangeInfoCell(workbook, sheet, sheet.getRow(5), 3, 5, 5, 3, 5, subjectName, 0);

						// create Header
						createArrangeHeaderCell(workbook, sheet, sheet.getRow(7), 0, 7, 9, 0, 0, "No.");
						createArrangeHeaderCell(workbook, sheet, sheet.getRow(7), 1, 7, 9, 1, 1, "StudentID");
						createArrangeHeaderCell(workbook, sheet, sheet.getRow(7), 2, 7, 9, 2, 2, "Fullname");

						// unit
						int beginColumn = 3;
						if (unitMap != null) {
							if (!unitMap.isEmpty()) {
								for (Entry<String, Integer> entry : unitMap.entrySet()) {
									if (entry.getValue() > 1) {
										createArrangeHeaderCell(workbook, sheet, sheet.getRow(7), beginColumn, 7, 7,
												beginColumn, beginColumn + entry.getValue() - 1, entry.getKey());
										beginColumn += entry.getValue();
									} else {
										createOneHeaderCell(workbook, sheet.getRow(7), beginColumn, entry.getKey());
										beginColumn += entry.getValue();
									}
								}
							}
						}

						// lesson
						beginColumn = 3;
						if (lessonMap != null) {
							if (!lessonMap.isEmpty()) {
								for (Map.Entry<Lesson, Integer> entry : lessonMap.entrySet()) {
									if (entry.getValue() > 1) {
										createArrangeHeaderCell(workbook, sheet, sheet.getRow(8), beginColumn, 8, 8,
												beginColumn, beginColumn + entry.getValue() - 1,
												"Lesson " + entry.getKey().getLessonName());
										beginColumn += entry.getValue();
									} else {
										createOneHeaderCell(workbook, sheet.getRow(8), beginColumn,
												"Lesson " + entry.getKey().getLessonName());
										beginColumn += entry.getValue();
									}
								}
							}
						}

						// exercise
						for (int i = 3; i < exerciseList.size() + 3; i++) {
							createOneHeaderCell(workbook, sheet.getRow(9), i,
									"Exercise " + exerciseList.get(i - 3).getExerciseName());
						}

						// progressTest
						int beginProgressTestColumn = exerciseList.size() + 3;

						// exercise
						for (int i = beginProgressTestColumn; i < exercisePTList.size()
								+ beginProgressTestColumn; i++) {
							createOneHeaderCell(workbook, sheet.getRow(9), i,
									"Exercise " + exerciseList.get(i - exerciseList.size() - 3).getExerciseName());
						}

						if (progressTestMap != null) {
							if (!progressTestMap.isEmpty()) {
								for (Map.Entry<String, Integer> entry : progressTestMap.entrySet()) {
									if (entry.getValue() >= 1) {
										createArrangeHeaderCell(workbook, sheet, sheet.getRow(7),
												beginProgressTestColumn, 7, 8, beginProgressTestColumn,
												beginProgressTestColumn + entry.getValue() - 1, entry.getKey());
										beginProgressTestColumn += entry.getValue();
									}
								}
							}
						}

						if (!studentProfileList.isEmpty()) {
							for (int i = 0; i < studentProfileList.size(); i++) {

								Cell noValueCell = createOneNormalCell(workbook, sheet.getRow(i + 10), 0,
										CellType.NUMERIC, HorizontalAlignment.CENTER);
								noValueCell.setCellValue(i + 1);

								Cell studentIdValueCell = createOneNormalCell(workbook, sheet.getRow(i + 10), 1,
										CellType.STRING, HorizontalAlignment.CENTER);
								studentIdValueCell
										.setCellValue("MJ" + String.format("%06d", studentProfileList.get(i).getId()));

								Cell fullNameValueCell = createOneNormalCell(workbook, sheet.getRow(i + 10), 2,
										CellType.STRING, HorizontalAlignment.LEFT);
								fullNameValueCell.setCellValue(studentProfileList.get(i).getAccount().getFullName());

								if (!exerciseList.isEmpty()) {
									for (int j = 3; j < exerciseList.size() + 3; j++) {
										List<ExerciseTaken> exerciseTakenList = iExerciseTakenRepository
												.findByExerciseIdAndAccountId(exerciseList.get(j - 3).getId(),
														studentProfileList.get(i).getAccount().getId());
										boolean isTaken = false;
										double score = 0;
										if (!exerciseTakenList.isEmpty()) {
//											double sumTotalScore = 0;
											for (ExerciseTaken exerciseTaken : exerciseTakenList) {
												if (exerciseTaken.getTotalScore() == 10) {
													System.out.println("MAX");
													score = 10;
													break;
												} else if (exerciseTaken.getTotalScore() > score) {
													score = exerciseTaken.getTotalScore();
												}
//												sumTotalScore += exerciseTaken.getTotalScore();
											}

//											score = sumTotalScore / exerciseTakenList.size();
//											score = Double.valueOf(new DecimalFormat("#.#").format(score));

											isTaken = true;
										}

										if (isTaken) {
											Cell exerciseValueCell = createOneNormalCell(workbook, sheet.getRow(i + 10),
													j, CellType.NUMERIC, HorizontalAlignment.CENTER);
											exerciseValueCell.setCellValue(score);
										} else {
											createOneWarningCell(workbook, sheet.getRow(i + 10), j, "Not yet!");
										}
									}
								}

								if (!exercisePTList.isEmpty()) {
									for (int j = exerciseList.size() + 3; j < exercisePTList.size()
											+ exerciseList.size() + 3; j++) {
										List<ExerciseTaken> exerciseTakenList = iExerciseTakenRepository
												.findByExerciseIdAndAccountId(
														exercisePTList.get(j - exerciseList.size() - 3).getId(),
														studentProfileList.get(i).getAccount().getId());
										boolean isTaken = false;
										double score = 0;
										if (!exerciseTakenList.isEmpty()) {
//											double sumTotalScore = 0;
											for (ExerciseTaken exerciseTaken : exerciseTakenList) {
												if (exerciseTaken.getTotalScore() == 10) {
													System.out.println("MAX");
													score = 10;
													break;
												} else if (exerciseTaken.getTotalScore() > score) {
													score = exerciseTaken.getTotalScore();
												}

//												sumTotalScore += exerciseTaken.getTotalScore();
											}

//											score = sumTotalScore / exerciseTakenList.size();
//											score = Double.valueOf(new DecimalFormat("#.#").format(score));

											isTaken = true;
										}

										if (isTaken) {
											Cell exerciseValueCell = createOneNormalCell(workbook, sheet.getRow(i + 10),
													j, CellType.NUMERIC, HorizontalAlignment.CENTER);
											exerciseValueCell.setCellValue(score);
										} else {
											createOneWarningCell(workbook, sheet.getRow(i + 10), j, "Not yet!");
										}
									}
								}
							}
						}
					}
				}
				response.put("EXPORT SUCCESS", workbook);

				FileOutputStream fileOut = new FileOutputStream(
						"E:\\" + schoolName + "-" + gradeName + "-ScoreExport.xlsx");
				workbook.write(fileOut);
				fileOut.close();
			}
		} catch (Exception e) {
			logger.error("Export score with schoolId = " + schoolId + " and gradeId " + gradeId + " subjectId = "
					+ subjectId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {
				response.put("NOT FOUND", null);
			} else {
				response.put("GRADUATE FAIL", null);
			}
		}

		return response;
	}

	// tìm tất cả các lớp dựa trên schoolId và gradeId --> tất cả hs
	// tìm tất cả các subject, unit và progressTest
	// in ra điểm trung bình của unit và progressTest của từng hs
	@Override
	@Transactional
	public Map<String, Workbook> exportFinalScore(long schoolId, int gradeId) throws IOException {
		Map<String, Workbook> response = new HashedMap<>();
		try {
			School school = iSchoolRepository.findByIdAndStatusNot(schoolId, DELETE_STATUS);
			if (school == null) {
				throw new ResourceNotFoundException();
			}
			Grade grade = iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());
			SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
					DELETE_STATUS);
			List<Classes> classesList = iClassRepository
					.findBySchoolGradeIdAndStatusNotOrderByStatusAscClassNameAsc(schoolGrade.getId(), DELETE_STATUS);
			if (!classesList.isEmpty()) {
				for (int i = 0; i < classesList.size(); i++) {
					if (classesList.get(i).getClassName().equals(PENDING_STATUS)) {
						classesList.remove(classesList.get(i));
						break;
					}
				}
			}

			Map<Subject, Integer> subjectMap = new LinkedHashMap<>();
			List<Subject> subjectList = iSubjectRepository.findByGradeIdAndIsDisableFalseOrderBySubjectName(gradeId);
			List<String> unitName = new ArrayList<>();

			if (!subjectList.isEmpty()) {
				for (Subject subject : subjectList) {
					int totalUnit = 0;
					List<Unit> unitList = iUnitRepository
							.findBySubjectIdAndIsDisableFalseOrderByUnitNameAsc(subject.getId());
					if (!unitList.isEmpty()) {
						for (Unit unit : unitList) {
							unitName.add("Unit " + unit.getUnitName());
						}
						totalUnit += unitList.size();
					}
					List<ProgressTest> progressTestList = iProgressTestRepository
							.findBySubjectIdAndIsDisableFalseOrderByProgressTestName(subject.getId());
					if (!progressTestList.isEmpty()) {
						for (ProgressTest progressTest : progressTestList) {
							unitName.add(progressTest.getProgressTestName());
						}
						totalUnit += progressTestList.size();
					}

					if (totalUnit > 0) {
						subjectMap.put(subject, totalUnit);
					}
				}
			}

			// header value content
			String schoolName = school.getSchoolName();
			String schoolCode = school.getSchoolCode() + school.getSchoolCount();
			int gradeName = grade.getGradeName();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			String exportDate = sdf.format(new Date());

			Workbook workbook = new XSSFWorkbook();
			if (!classesList.isEmpty()) {
				for (Classes classes : classesList) {
					classes.setStatus(INACTIVE_STATUS);
					iClassRepository.save(classes);

					List<StudentProfile> studentProfileList = new ArrayList<>();
					if (!iStudentProfileRepository.findByClassesIdAndStatusNot(classes.getId(), DELETE_STATUS)
							.isEmpty()) {
						studentProfileList.addAll(
								iStudentProfileRepository.findByClassesIdAndStatusNot(classes.getId(), DELETE_STATUS));
					}

					Sheet sheet = workbook.createSheet(classes.getClassName());
					sheet.setColumnWidth(0, 1500);
					sheet.setColumnWidth(1, 3700);
					sheet.setColumnWidth(2, 9000);
					sheet.setColumnWidth(3, 3700);
					sheet.setColumnWidth(4, 3700);
					sheet.setColumnWidth(5, 9000);
					sheet.setColumnWidth(6, 9000);
					for (int i = 7; i < unitName.size() + 7; i++) {
						sheet.setColumnWidth(i, 3500);
					}

					for (int i = 0; i < studentProfileList.size() + 10; i++) {
						if (i == 5) {
							continue;
						}
						sheet.createRow(i);
					}

					createArrangeInfoCell(workbook, sheet, sheet.getRow(0), 2, 0, 0, 2, 5,
							"FINAL SCORE GRADUATE EXPORT TABLE", 0);

					// create Table Information
					createOneInfoCell(workbook, sheet.getRow(1), 2, "School Name:");
					createArrangeInfoCell(workbook, sheet, sheet.getRow(1), 3, 1, 1, 3, 5, schoolName, 0);
					createOneInfoCell(workbook, sheet.getRow(2), 2, "School Code:");
					createArrangeInfoCell(workbook, sheet, sheet.getRow(2), 3, 2, 2, 3, 5, schoolCode, 0);
					createOneInfoCell(workbook, sheet.getRow(3), 2, "Grade:");
					createArrangeInfoCell(workbook, sheet, sheet.getRow(3), 3, 3, 3, 3, 5, "", gradeName);
					createOneInfoCell(workbook, sheet.getRow(4), 2, "Export Date:");
					createArrangeInfoCell(workbook, sheet, sheet.getRow(4), 3, 4, 4, 3, 5, exportDate, 0);

					// create Header
					createArrangeHeaderCell(workbook, sheet, sheet.getRow(6), 0, 6, 7, 0, 0, "No.");
					createArrangeHeaderCell(workbook, sheet, sheet.getRow(6), 1, 6, 7, 1, 1, "StudentID");
					createArrangeHeaderCell(workbook, sheet, sheet.getRow(6), 2, 6, 7, 2, 2, "Fullname");
					createArrangeHeaderCell(workbook, sheet, sheet.getRow(6), 3, 6, 7, 3, 3, "DoB");
					createArrangeHeaderCell(workbook, sheet, sheet.getRow(6), 4, 6, 7, 4, 4, "Gender");
					createArrangeHeaderCell(workbook, sheet, sheet.getRow(6), 5, 6, 7, 5, 5, "Parent Name");
					createArrangeHeaderCell(workbook, sheet, sheet.getRow(6), 6, 6, 7, 6, 6, "Contact");

					int subjectBeginColumn = 7;

					if (!subjectMap.isEmpty()) {
						for (Entry<Subject, Integer> entry : subjectMap.entrySet()) {
							if (entry.getValue() > 1) {
								createArrangeHeaderCell(workbook, sheet, sheet.getRow(6), subjectBeginColumn, 6, 6,
										subjectBeginColumn, subjectBeginColumn + entry.getValue() - 1,
										entry.getKey().getSubjectName());
								subjectBeginColumn += entry.getValue();
							} else {
								createOneHeaderCell(workbook, sheet.getRow(6), subjectBeginColumn,
										entry.getKey().getSubjectName());
								subjectBeginColumn += entry.getValue();
							}
						}
					}

					// unit
					int unitColumnBegin = 7;
					if (!unitName.isEmpty()) {
						for (int i = unitColumnBegin; i < unitName.size() + unitColumnBegin; i++) {
							createOneHeaderCell(workbook, sheet.getRow(7), i, unitName.get(i - unitColumnBegin));
						}
					}

					if (!studentProfileList.isEmpty()) {
						for (int i = 0; i < studentProfileList.size(); i++) {
							Cell noValueCell = createOneNormalCell(workbook, sheet.getRow(i + 8), 0, CellType.NUMERIC,
									HorizontalAlignment.CENTER);
							noValueCell.setCellValue(i + 1);

							Cell studentIdValueCell = createOneNormalCell(workbook, sheet.getRow(i + 8), 1,
									CellType.STRING, HorizontalAlignment.CENTER);
							studentIdValueCell
									.setCellValue("MJ" + String.format("%06d", studentProfileList.get(i).getId()));

							Cell fullNameValueCell = createOneNormalCell(workbook, sheet.getRow(i + 8), 2,
									CellType.STRING, HorizontalAlignment.LEFT);
							fullNameValueCell.setCellValue(studentProfileList.get(i).getAccount().getFullName());

							Cell DoBValueCell = createOneNormalCell(workbook, sheet.getRow(i + 8), 3, CellType.STRING,
									HorizontalAlignment.CENTER);
							DoBValueCell.setCellValue(studentProfileList.get(i).getDOB());
							Cell genderValueCell = createOneNormalCell(workbook, sheet.getRow(i + 8), 4,
									CellType.STRING, HorizontalAlignment.CENTER);
							genderValueCell.setCellValue(studentProfileList.get(i).getGender());
							Cell parentNameValueCell = createOneNormalCell(workbook, sheet.getRow(i + 8), 5,
									CellType.STRING, HorizontalAlignment.LEFT);
							parentNameValueCell.setCellValue(studentProfileList.get(i).getParentName());
							Cell contactValueCell = createOneNormalCell(workbook, sheet.getRow(i + 8), 6,
									CellType.STRING, HorizontalAlignment.LEFT);
							contactValueCell.setCellValue(studentProfileList.get(i).getContact());
							int scoreColumnBegin = 7;
							if (!subjectMap.isEmpty()) {
								for (Entry<Subject, Integer> entry : subjectMap.entrySet()) {
									List<Unit> unitList = iUnitRepository
											.findBySubjectIdAndIsDisableFalseOrderByUnitNameAsc(entry.getKey().getId());
									if (!unitList.isEmpty()) {
										for (int j = scoreColumnBegin; j < unitList.size() + scoreColumnBegin; j++) {
											StudentRecord studentRecord = iStudentRecordRepository
													.findByUnitIdAndAccountId(
															unitList.get(j - scoreColumnBegin).getId(),
															studentProfileList.get(i).getAccount().getId());

											double score = 0;
											if (studentRecord != null) {
												String listExerciseTakenScore = studentRecord
														.getListExerciseTakenScore();
												int countExerciseTakenRemain = checkExerciseTakenRemain(
														listExerciseTakenScore,
														unitList.get(j - scoreColumnBegin).getId(), false);

												if (countExerciseTakenRemain == 0) {
													if (studentRecord != null) {
														score = studentRecord.getAverageScore();
													}
												} else {
													score = calculateAverageScore(listExerciseTakenScore,
															countExerciseTakenRemain);
												}
												score = Double.valueOf(new DecimalFormat("#.#").format(score));
											}
											Cell exerciseValueCell = createOneNormalCell(workbook, sheet.getRow(i + 8),
													j, CellType.NUMERIC, HorizontalAlignment.CENTER);
											exerciseValueCell.setCellValue(score);
										}

										scoreColumnBegin += unitList.size();

										List<ProgressTest> progressTestList = iProgressTestRepository
												.findBySubjectIdAndIsDisableFalseOrderByProgressTestName(
														entry.getKey().getId());
										if (!progressTestList.isEmpty()) {
											for (int j = scoreColumnBegin; j < progressTestList.size()
													+ scoreColumnBegin; j++) {
												StudentRecord studentRecord = iStudentRecordRepository
														.findByProgressTestIdAndAccountId(
																progressTestList.get(j - scoreColumnBegin).getId(),
																studentProfileList.get(i).getAccount().getId());

												double score = 0;
												if (studentRecord != null) {
													String listExerciseTakenScore = studentRecord
															.getListExerciseTakenScore();
													int countExerciseTakenRemain = checkExerciseTakenRemain(
															listExerciseTakenScore,
															unitList.get(j - scoreColumnBegin).getId(), true);

													if (countExerciseTakenRemain == 0) {
														if (studentRecord != null) {
															score = studentRecord.getAverageScore();
														}
													} else {
														score = calculateAverageScore(listExerciseTakenScore,
																countExerciseTakenRemain);
													}
													score = Double.valueOf(new DecimalFormat("#.#").format(score));
												}

//												double score = 0;
//												if (studentRecord != null) {
//													score = studentRecord.getAverageScore();
//												}
												Cell exerciseValueCell = createOneNormalCell(workbook,
														sheet.getRow(i + 8), j, CellType.NUMERIC,
														HorizontalAlignment.CENTER);
												exerciseValueCell.setCellValue(score);
											}
											scoreColumnBegin += progressTestList.size();

										}
									}
								}
							}

							changeStatusOneStudent(studentProfileList.get(i).getId(), PENDING_STATUS);
						}
					}
				}

				response.put("GRADUATE SUCCESS", workbook);
				FileOutputStream fileOut = new FileOutputStream(
						"E:\\" + schoolName + "-" + gradeName + "-FinalScoreExport.xlsx");
				workbook.write(fileOut);
				fileOut.close();
			}
		} catch (Exception e) {
			logger.error("Export final score with schoolId = " + schoolId + " and gradeId" + gradeId + "! "
					+ e.getMessage());
			if (e instanceof ResourceNotFoundException) {
				response.put("NOT FOUND", null);
			} else {
				response.put("GRADUATE FAIL", null);
			}
		}

		return response;
	}

	private int checkExerciseTakenRemain(String listExerciseTakenScore, long id, boolean isProgressTest) {
		int countExerciseTakenRemain = 0;
		try {
			List<Exercise> exerciseList = new ArrayList<>();
			if (isProgressTest) {
				if (iExerciseRepository.findByProgressTestIdAndStatusNot(id, DELETE_STATUS).size() > 0) {
					exerciseList.addAll(iExerciseRepository.findByProgressTestIdAndStatusNot(id, DELETE_STATUS));
				}
			} else {
				List<Lesson> lessonList = iLessonRepository.findByUnitIdAndIsDisableFalse(id);
				if (!lessonList.isEmpty()) {

					// quét lại toàn bộ exercise
					for (Lesson lesson : lessonList) {
						if (iExerciseRepository.findByLessonIdAndStatusNot(lesson.getId(), DELETE_STATUS).size() > 0) {
							exerciseList.addAll(
									iExerciseRepository.findByLessonIdAndStatusNot(lesson.getId(), DELETE_STATUS));
						}
					}

				}
			}

			//// quét lại toàn bộ exercise trong student record
			// nếu số lượng bằng nhau thì trả về 0
			// nếu số lượng khác nhau thì đếm số exercise còn thiếu trong student record
			String[] exerciseIdScoreList = listExerciseTakenScore.split(" ");
			if (exerciseIdScoreList.length != exerciseList.size()) {
				List<Long> exerciseIdList = new ArrayList<>();
				for (String exerciseIdScore : exerciseIdScoreList) {
					exerciseIdList.add(Long.valueOf(exerciseIdScore.split(":")[0]));
				}

				if (!exerciseIdList.isEmpty()) {
					for (Exercise exercise : exerciseList) {
						if (!exerciseIdList.contains(exercise.getId())) {
							countExerciseTakenRemain++;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Check exercise remain with " + (isProgressTest ? "progressTestId = " : "unitId = ") + id
					+ "! " + e.getMessage());
			throw e;
		}

		return countExerciseTakenRemain;
	}

	private double calculateAverageScore(String listExerciseTakenScore, int countExerciseTakenRemain) {
		String[] exerciseIdScoreList = listExerciseTakenScore.split(" ");
		double totalScore = 0;
		if (exerciseIdScoreList.length > 0) {
			for (String exerciseIdScore : exerciseIdScoreList) {
				totalScore += Double.parseDouble(exerciseIdScore.split(":")[1]);
			}
		}
		double average = totalScore / (exerciseIdScoreList.length + countExerciseTakenRemain);
		return average;
	}

	@Override
	public void writeFileOS(HttpServletResponse httpServletResponse, Workbook workbook) throws IOException {
		ServletOutputStream outputStream = httpServletResponse.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	private Cell createOneInfoCell(Workbook workbook, Row row, int column, String cellValue) {
		Cell cell = row.createCell(column, CellType.STRING);
		CellStyle cellStyle = workbook.createCellStyle();
		formatStyle(cellStyle, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, BorderStyle.THIN);
		cellStyle.setIndention((short) 1);

		Font font = workbook.createFont();
		formatFont(font, IndexedColors.BLACK.getIndex(), true, 14);

		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(cellValue);

		return cell;
	}

	private Cell createArrangeInfoCell(Workbook workbook, Sheet sheet, Row row, int column, int rowBegin, int rowEnd,
			int columnBegin, int columnEnd, String cellValue, int intValue) {
		Cell cell = row.createCell(column, CellType.STRING);
		CellRangeAddress cellRangeAddress = new CellRangeAddress(rowBegin, rowEnd, columnBegin, columnEnd);
		sheet.addMergedRegion(cellRangeAddress);
		CellStyle cellStyle = workbook.createCellStyle();
		formatStyle(cellStyle, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN);

		RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);

		Font font = workbook.createFont();
		formatFont(font, IndexedColors.RED.getIndex(), true, 14);

		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(cellValue);
		if (intValue > 0) {
			cell.setCellValue(intValue);
		}

		return cell;
	}

	private Cell createOneHeaderCell(Workbook workbook, Row row, int column, String cellValue) {
		Cell cell = row.createCell(column, CellType.STRING);
		CellStyle cellStyle = workbook.createCellStyle();
		formatStyle(cellStyle, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN);

		Font font = workbook.createFont();
		formatFont(font, IndexedColors.BLACK.getIndex(), true, 13);

		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(cellValue);

		return cell;
	}

	private Cell createArrangeHeaderCell(Workbook workbook, Sheet sheet, Row row, int column, int rowBegin, int rowEnd,
			int columnBegin, int columnEnd, String cellValue) {
		Cell cell = row.createCell(column, CellType.STRING);
		CellRangeAddress cellRangeAddress = new CellRangeAddress(rowBegin, rowEnd, columnBegin, columnEnd);
		sheet.addMergedRegion(cellRangeAddress);
		CellStyle cellStyle = workbook.createCellStyle();
		formatStyle(cellStyle, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN);
		RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);

		Font font = workbook.createFont();
		formatFont(font, IndexedColors.BLACK.getIndex(), true, 13);

		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(cellValue);

		return cell;
	}

	private Cell createOneNormalCell(Workbook workbook, Row row, int column, CellType cellType,
			HorizontalAlignment horizontalAlignment) {
		Cell cell = row.createCell(column, cellType);
		CellStyle cellStyle = workbook.createCellStyle();
		formatStyle(cellStyle, horizontalAlignment, VerticalAlignment.CENTER, BorderStyle.THIN);

		Font font = workbook.createFont();
		formatFont(font, IndexedColors.BLACK.getIndex(), false, 13);

		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);

		return cell;
	}

	private Cell createOneWarningCell(Workbook workbook, Row row, int column, String cellValue) {
		Cell cell = row.createCell(column, CellType.STRING);
		CellStyle cellStyle = workbook.createCellStyle();
		formatStyle(cellStyle, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN);
		cellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Font font = workbook.createFont();
		formatFont(font, IndexedColors.BLACK.getIndex(), true, 13);

		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(cellValue);

		return cell;
	}

	private void formatStyle(CellStyle cellStyle, HorizontalAlignment horizontalAlignment,
			VerticalAlignment verticalAlignment, BorderStyle borderStyle) {
		cellStyle.setAlignment(horizontalAlignment);
		cellStyle.setVerticalAlignment(verticalAlignment);
		cellStyle.setBorderTop(borderStyle);
		cellStyle.setBorderBottom(borderStyle);
		cellStyle.setBorderLeft(borderStyle);
		cellStyle.setBorderRight(borderStyle);
	}

	private void formatFont(Font font, short color, boolean bold, int fontSize) {
		font.setColor(color);
		font.setBold(bold);
		font.setFontName("Times New Roman");
		font.setFontHeightInPoints((short) fontSize);
	}

	private CellStyle formatErrorCell(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		formatStyle(cellStyle, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN);
		cellStyle.setFillForegroundColor(IndexedColors.RED1.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Font font = workbook.createFont();
		formatFont(font, IndexedColors.BLACK.getIndex(), true, 13);
		font.setStrikeout(true);

		cellStyle.setFont(font);

		return cellStyle;
	}

	private String generateUsername(Classes classes) {
		int gradeName = classes.getSchoolGrade().getGrade().getGradeName();
		String schoolCode = classes.getSchoolGrade().getSchool().getSchoolCode()
				+ classes.getSchoolGrade().getSchool().getSchoolCount();

		long studentCount = countStudent(classes);
		String username = schoolCode.toLowerCase() + String.format("%02d", gradeName)
				+ String.format("%04d", studentCount);
		System.out.println(username);
		return username;
	}

	private String generateUsernameDELPEND(String status, Classes classes) {
		String username = "";
		long studentCount = countStudent(classes);
		if (status.equalsIgnoreCase(DELETE_STATUS)) {
			username = "DEL" + String.format("%09d", studentCount);
		}
		if (status.equalsIgnoreCase(PENDING_STATUS)) {
			int gradeName = classes.getSchoolGrade().getGrade().getGradeName();
			String schoolCode = classes.getSchoolGrade().getSchool().getSchoolCode()
					+ classes.getSchoolGrade().getSchool().getSchoolCount();
			username = schoolCode + "_PEND_" + String.format("%02d", gradeName) + String.format("%09d", studentCount);
		}
		return username;
	}

	@Override
	public String generateFileNameExport(long schoolId, int gradeId, long subjectId) {
		String fileName = "";
		try {
			School school = iSchoolRepository.findByIdAndStatusNot(schoolId, DELETE_STATUS);
			Grade grade = iGradeRepository.findById(gradeId).orElseThrow(() -> new ResourceNotFoundException());
			String subjectName = "";
			if (subjectId > 0) {
				Subject subject = iSubjectRepository.findByIdAndIsDisableFalse(subjectId);
				subjectName = subject.getSubjectName() + "-";
			}

			String schoolCode = school.getSchoolCode() + school.getSchoolCount();
			int gradeName = grade.getGradeName();

			fileName = schoolCode + "-Grade" + gradeName + "-" + subjectName;
		} catch (Exception e) {
			logger.error("Generate file name with schoolId =  " + schoolId + " gradeId = " + gradeId
					+ " and subjectId = " + subjectId + "! " + e.getMessage());
			throw e;
		}

		return fileName;
	}

	private long countStudent(Classes classes) {
		String className = classes.getClassName();
		long studentCount = 1;
		try {
			if (!className.equalsIgnoreCase(PENDING_STATUS) && classes.getId() != 0) {
				List<Classes> classesList = iClassRepository
						.findBySchoolGradeIdAndStatus(classes.getSchoolGrade().getId(), ACTIVE_STATUS);
				classesList.addAll(iClassRepository.findBySchoolGradeIdAndStatus(classes.getSchoolGrade().getId(),
						INACTIVE_STATUS));
				if (!classesList.isEmpty()) {
					List<StudentProfile> studentProfileList = new ArrayList<>();
					for (Classes classes2 : classesList) {
						StudentProfile studentProfile = iStudentProfileRepository
								.findFirstByClassesIdAndStatusContainingOrderByStudentCountDesc(classes2.getId(),
										ACTIVE_STATUS);
						if (studentProfile != null) {
							System.out.println(classes2.getClassName() + " - " + studentProfile.getId());
							studentProfileList.add(studentProfile);
						}
					}
					if (!studentProfileList.isEmpty()) {
						for (StudentProfile studentProfile : studentProfileList) {
							System.out.println(studentProfile.getId() + " - " + studentProfile.getStudentCount());
							if (studentProfile.getStudentCount() > studentCount) {

								studentCount = studentProfile.getStudentCount();
							}
						}
						studentCount++;
					}
				}
			} else {
				StudentProfile studentProfile = iStudentProfileRepository
						.findFirstByClassesIdAndStatusContainingOrderByStudentCountDesc(classes.getId(),
								classes.getStatus());

				if (studentProfile != null) {
					studentCount = studentProfile.getStudentCount() + 1;
				}
			}
		} catch (Exception e) {
			logger.error("Count student at classId =  " + classes.getId() + "! " + e.getMessage());
			throw e;
		}

		return studentCount;
	}

}
