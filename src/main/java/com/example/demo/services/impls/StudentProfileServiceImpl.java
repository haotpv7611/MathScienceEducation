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
	private final String DEFAULT_PASSWORD = "abc123456";
	private final int STUDENT_ROLE = 3;
	private final int FIRST_STUDENT_ROW = 7;
	private final int FIRST_COLUMN = 1;
	private final int LAST_COLUMN = 6;

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
	ISubjectRepository iSubjectRepository;

	@Autowired
	IProgressTestRepository iProgressTestRepository;

	@Autowired
	private IUnitRepository iUnitRepository;

	@Autowired
	private ILessonRepository iLessonRepository;

	@Autowired
	private IExerciseRepository iExerciseRepository;

	@Autowired
	private IExerciseTakenRepository iExerciseTakenRepository;

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

			String username = generateUsername(classes);
			String fullName = studentRequestDTO.getFullName();
			Account account = new Account(username, DEFAULT_PASSWORD, fullName, STUDENT_ROLE, ACTIVE_STATUS);
			iAccountRepository.save(account);

			String DoB = studentRequestDTO.getDoB();
			String gender = studentRequestDTO.getGender();
			String parentName = studentRequestDTO.getParentName();
			String contact = studentRequestDTO.getContact();
			StudentProfile studentProfile = new StudentProfile(DoB, gender, parentName, contact, ACTIVE_STATUS,
					countStudent(classes), account, classes);
			iStudentProfileRepository.save(studentProfile);
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

			changeStatusOneStudent(id, status);
		}

		return "CHANGE SUCCESS!";
	}

	// active <--> inactive
	// active, inactive --> deleted
	// graduate: active, inactive --> pending
	// active và inactive thì k thay đổi username
	// delete và pending thì thay đổi username và classesId
	@Override
	public void changeStatusOneStudent(long id, String status) {
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
			Classes deleteClass = iClassRepository.findById(0L).orElseThrow(() -> new ResourceNotFoundException());
			studentProfile.setClasses(deleteClass);
			studentProfile.setStudentCount(countStudent(deleteClass));

			String username = generateUsernameDELPEND(DELETE_STATUS, deleteClass);
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
			account.setUsername(username);
			account.setStatus(status);
			iAccountRepository.save(account);

			studentProfile.setStatus(status);
			iStudentProfileRepository.save(studentProfile);
		}
	}

	// chuyển giữa các lớp đang active, chuyển từ lớp pending sang lớp active
	// cùng khối, lớp cũ k phải lớp pending thì giữ nguyên account
	// khác khối hoặc pending thì đổi account
	@Override
	public String changeClassForStudent(List<Long> studentIdList, long classesId) {
		try {
			Classes newClasses = iClassRepository.findByIdAndStatusNot(classesId, DELETE_STATUS);
			if (newClasses == null) {
				throw new ResourceNotFoundException();
			}
			Grade newGrade = newClasses.getSchoolGrade().getGrade();

			for (long studentId : studentIdList) {
				StudentProfile studentProfile = iStudentProfileRepository.findByIdAndStatusNot(studentId,
						DELETE_STATUS);
				if (studentProfile == null) {
					throw new ResourceNotFoundException();
				}

				Grade oldGrade = studentProfile.getClasses().getSchoolGrade().getGrade();
				if (oldGrade.equals(newGrade)) {
					if (studentProfile.getClasses().getClassName().equalsIgnoreCase("PENDING")) {
						String username = generateUsername(newClasses);
						Account account = studentProfile.getAccount();
						account.setUsername(username);
						account.setStatus(ACTIVE_STATUS);
						iAccountRepository.save(account);

						studentProfile.setStudentCount(countStudent(newClasses));
						studentProfile.setClasses(newClasses);
						studentProfile.setStatus(ACTIVE_STATUS);
						iStudentProfileRepository.save(studentProfile);
					} else {
						studentProfile.setClasses(newClasses);
						iStudentProfileRepository.save(studentProfile);
					}

				} else {
					String username = generateUsername(newClasses);
					Account account = studentProfile.getAccount();
					account.setUsername(username);
					iAccountRepository.save(account);

					studentProfile.setClasses(newClasses);
					studentProfile.setStudentCount(countStudent(newClasses));
					iStudentProfileRepository.save(studentProfile);
				}
			}
		} catch (Exception e) {
			logger.error("Change studentListId =  " + studentIdList.toString() + " to classId = " + classesId + "! "
					+ e.getMessage());
			throw e;
		}

		return "CHANGE SUCCESS!";
	}

	@Override
	public void validateStudentFile(MultipartFile file, long schoolId, int gradeId,
			HttpServletResponse httpServletResponse) throws IOException, ParseException {
		// open file
		Workbook workbook = new XSSFWorkbook(file.getInputStream());

		// check schoolGrade existed
		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
				DELETE_STATUS);
		if (schoolGrade == null) {
			throw new ResourceNotFoundException();
		}
		School school = schoolGrade.getSchool();
		int gradeName = schoolGrade.getGrade().getGradeName();
		String schoolCode = school.getSchoolCode() + school.getSchoolCount();
		List<Classes> classesList = iClassRepository.findBySchoolGradeIdAndStatusNot(schoolGrade.getId(),
				DELETE_STATUS);

		// validate all sheet in excel file
		Iterator<Sheet> sheetIterator = workbook.sheetIterator();
		while (sheetIterator.hasNext()) {
			Sheet sheet = sheetIterator.next();
			List<Cell> cellList = validateSheetData(sheet, classesList, schoolCode, gradeName);
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

				String fileName = file.getOriginalFilename();
				FileOutputStream fileOut = new FileOutputStream("E:\\" + "Error-" + fileName);
				workbook.write(fileOut);
				fileOut.close();

				ServletOutputStream outputStream = httpServletResponse.getOutputStream();
				workbook.write(outputStream);
				workbook.close();
				outputStream.close();
			} else {

			}
		}

		workbook.close();
	}

	private List<Cell> validateSheetData(Sheet sheet, List<Classes> classesList, String schoolCode, int gradeName)
			throws ParseException {
		List<Cell> cellList = new ArrayList<>();

		Iterator<Row> rowIterator = sheet.rowIterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

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

		return cellList;

	}

	@Override
	@Transactional
	public String importStudent(MultipartFile file, long schoolId, int gradeId, HttpServletResponse httpServletResponse)
			throws IOException {
		// open file
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
				DELETE_STATUS);

		Iterator<Sheet> sheetIterator = workbook.sheetIterator();
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
			List<Cell> cellList = new ArrayList<>();
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
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
						String DoB = sdf.format(row.getCell(3).getDateCellValue());
						String gender = row.getCell(4).getStringCellValue();
						String parentName = row.getCell(5).getStringCellValue();
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

				String fileName = file.getOriginalFilename();
				FileOutputStream fileOut = new FileOutputStream("E:\\" + "Not existed StudentId-" + fileName);
				workbook.write(fileOut);
				fileOut.close();

				ServletOutputStream outputStream = httpServletResponse.getOutputStream();
				workbook.write(outputStream);
				workbook.close();
				outputStream.close();

			} else {

			}
		}

		workbook.close();

		return "OK";
	}

	// tìm tất cả các lớp dựa trên schoolId và gradeId --> tất cả hs
	// tìm tất cả các unit, lesson, exercise, progressTest dựa trên subjectId
	// in ra điểm trung bình từng exericse của từng hs, nếu chưa làm in ra not yet
	@Override
	public void exportScoreBySubjectId(long schoolId, int gradeId, long subjectId,
			HttpServletResponse httpServletResponse) throws IOException {
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
		List<Classes> classesList = iClassRepository
				.findBySchoolGradeIdAndStatusNotOrderByStatusAscClassNameAsc(schoolGrade.getId(), DELETE_STATUS);
		for (int i = 0; i < classesList.size(); i++) {
			if (classesList.get(i).getClassName().equals(PENDING_STATUS)) {
				classesList.remove(classesList.get(i));
				break;
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
				lessonList.addAll(iLessonRepository.findByUnitIdAndIsDisableFalseOrderByLessonNameAsc(unit.getId()));
				if (!lessonList.isEmpty()) {
					for (Lesson lesson : lessonList) {
						int exerciseSize = iExerciseRepository
								.findByLessonIdAndStatusNotOrderByExerciseNameAsc(lesson.getId(), DELETE_STATUS).size();

						if (exerciseSize > 0) {
							exerciseList.addAll(iExerciseRepository
									.findByLessonIdAndStatusNotOrderByExerciseNameAsc(lesson.getId(), DELETE_STATUS));
							totalExercise += exerciseSize;
							lessonMap.put(lesson, exerciseSize);
						}
					}

					if (totalExercise > 0) {
						System.out.println("unit: " + unit.getUnitName() + " - " + totalExercise);
						unitMap.put("Unit " + unit.getUnitName(), totalExercise);
					}
				}
			}
		}

		Map<String, Integer> progressTestMap = new LinkedHashMap<>();
		List<Exercise> exercisePTList = new ArrayList<>();
		List<ProgressTest> progressTestList = iProgressTestRepository.findBySubjectIdAndIsDisableFalse(subjectId);
		if (!progressTestList.isEmpty()) {
			for (ProgressTest progressTest : progressTestList) {
				int exerciseSize = iExerciseRepository
						.findByProgressTestIdAndStatusNotOrderByExerciseNameAsc(progressTest.getId(), DELETE_STATUS)
						.size();
				if (exerciseSize > 0) {
					exercisePTList.addAll(iExerciseRepository.findByProgressTestIdAndStatusNotOrderByExerciseNameAsc(
							progressTest.getId(), DELETE_STATUS));
					progressTestMap.put(progressTest.getProgressTestName(), exerciseSize);
				}
			}
		}

		Workbook workbook = new XSSFWorkbook();
		if (!classesList.isEmpty()) {
			for (Classes classes : classesList) {
				List<StudentProfile> studentProfileList = new ArrayList<>();
				if (!classesList.isEmpty()) {
					if (!iStudentProfileRepository.findByClassesIdAndStatusNot(classes.getId(), DELETE_STATUS)
							.isEmpty()) {
						studentProfileList.addAll(
								iStudentProfileRepository.findByClassesIdAndStatusNot(classes.getId(), DELETE_STATUS));
					}
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
				for (int i = beginProgressTestColumn; i < exercisePTList.size() + beginProgressTestColumn; i++) {
					createOneHeaderCell(workbook, sheet.getRow(9), i,
							"Exercise " + exerciseList.get(i - exerciseList.size() - 3).getExerciseName());
				}

				if (progressTestMap != null) {
					if (!progressTestMap.isEmpty()) {
						for (Map.Entry<String, Integer> entry : progressTestMap.entrySet()) {
							System.out.println("name = " + entry.getKey() + " exercise = " + entry.getValue());
							System.out.println(beginProgressTestColumn);
							if (entry.getValue() >= 1) {
								createArrangeHeaderCell(workbook, sheet, sheet.getRow(7), beginProgressTestColumn, 7, 8,
										beginProgressTestColumn, beginProgressTestColumn + entry.getValue() - 1,
										entry.getKey());
								beginProgressTestColumn += entry.getValue();
							}
						}
					}
				}

				if (!studentProfileList.isEmpty()) {
					for (int i = 0; i < studentProfileList.size(); i++) {
						Cell noValueCell = createOneNormalCell(workbook, sheet.getRow(i + 10), 0, CellType.NUMERIC,
								HorizontalAlignment.CENTER);
						noValueCell.setCellValue(i + 1);

						Cell studentIdValueCell = createOneNormalCell(workbook, sheet.getRow(i + 10), 1,
								CellType.STRING, HorizontalAlignment.CENTER);
						studentIdValueCell
								.setCellValue("MJ" + String.format("%06d", studentProfileList.get(i).getId()));

						Cell fullNameValueCell = createOneNormalCell(workbook, sheet.getRow(i + 10), 2, CellType.STRING,
								HorizontalAlignment.LEFT);
						fullNameValueCell.setCellValue(studentProfileList.get(i).getAccount().getFullName());

						for (int j = 3; j < exerciseList.size() + 3; j++) {
							List<ExerciseTaken> exerciseTakenList = iExerciseTakenRepository
									.findByExerciseIdAndAccountId(exerciseList.get(j - 3).getId(),
											studentProfileList.get(i).getAccount().getId());
							boolean isTaken = false;
							double score = 0;
							if (!exerciseTakenList.isEmpty()) {
								double sumTotalScore = 0;
								for (ExerciseTaken exerciseTaken : exerciseTakenList) {
									sumTotalScore += exerciseTaken.getTotalScore();
								}

								score = sumTotalScore / exerciseTakenList.size();
								score = Double.valueOf(new DecimalFormat("#.#").format(score));

								isTaken = true;
							}

							if (isTaken) {
								Cell exerciseValueCell = createOneNormalCell(workbook, sheet.getRow(i + 10), j,
										CellType.NUMERIC, HorizontalAlignment.CENTER);
								exerciseValueCell.setCellValue(score);
							} else {
								createOneWarningCell(workbook, sheet.getRow(i + 10), j, "Not yet!");
							}
						}

						for (int j = exerciseList.size() + 3; j < exercisePTList.size() + exerciseList.size()
								+ 3; j++) {
							List<ExerciseTaken> exerciseTakenList = iExerciseTakenRepository
									.findByExerciseIdAndAccountId(exerciseList.get(j - exerciseList.size() - 3).getId(),
											studentProfileList.get(i).getAccount().getId());
							boolean isTaken = false;
							double score = 0;
							if (!exerciseTakenList.isEmpty()) {
								double sumTotalScore = 0;
								for (ExerciseTaken exerciseTaken : exerciseTakenList) {
									sumTotalScore += exerciseTaken.getTotalScore();
								}

								score = sumTotalScore / exerciseTakenList.size();
								score = Double.valueOf(new DecimalFormat("#.#").format(score));

								isTaken = true;
							}

							if (isTaken) {
								Cell exerciseValueCell = createOneNormalCell(workbook, sheet.getRow(i + 10), j,
										CellType.NUMERIC, HorizontalAlignment.CENTER);
								exerciseValueCell.setCellValue(score);
							} else {
								createOneWarningCell(workbook, sheet.getRow(i + 10), j, "Not yet!");
							}
						}
					}
				}
			}
		}

		FileOutputStream fileOut = new FileOutputStream("E:\\" + schoolName + "-" + gradeName + "-ScoreExport.xlsx");
		workbook.write(fileOut);
		fileOut.close();

		ServletOutputStream outputStream = httpServletResponse.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	// tìm tất cả các lớp dựa trên schoolId và gradeId --> tất cả hs
	// tìm tất cả các subject, unit và progressTest
	// in ra điểm trung bình của unit và progressTest của từng hs
	@Override
	public void exportFinalScore(long schoolId, int gradeId, HttpServletResponse httpServletResponse)
			throws IOException {
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
		List<Subject> subjectList = iSubjectRepository.findByGradeIdAndIsDisableFalse(gradeId);
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
						.findBySubjectIdAndIsDisableFalse(subject.getId());
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
				List<StudentProfile> studentProfileList = new ArrayList<>();
				if (!classesList.isEmpty()) {
					if (!iStudentProfileRepository.findByClassesIdAndStatusNot(classes.getId(), DELETE_STATUS)
							.isEmpty()) {
						studentProfileList.addAll(
								iStudentProfileRepository.findByClassesIdAndStatusNot(classes.getId(), DELETE_STATUS));
					}
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

						Cell studentIdValueCell = createOneNormalCell(workbook, sheet.getRow(i + 8), 1, CellType.STRING,
								HorizontalAlignment.CENTER);
						studentIdValueCell
								.setCellValue("MJ" + String.format("%06d", studentProfileList.get(i).getId()));

						Cell fullNameValueCell = createOneNormalCell(workbook, sheet.getRow(i + 8), 2, CellType.STRING,
								HorizontalAlignment.LEFT);
						fullNameValueCell.setCellValue(studentProfileList.get(i).getAccount().getFullName());

						Cell DoBValueCell = createOneNormalCell(workbook, sheet.getRow(i + 8), 3, CellType.STRING,
								HorizontalAlignment.CENTER);
						DoBValueCell.setCellValue(studentProfileList.get(i).getDOB());
						Cell genderValueCell = createOneNormalCell(workbook, sheet.getRow(i + 8), 4, CellType.STRING,
								HorizontalAlignment.CENTER);
						genderValueCell.setCellValue(studentProfileList.get(i).getGender());
						Cell parentNameValueCell = createOneNormalCell(workbook, sheet.getRow(i + 8), 5,
								CellType.STRING, HorizontalAlignment.LEFT);
						parentNameValueCell.setCellValue(studentProfileList.get(i).getParentName());
						Cell contactValueCell = createOneNormalCell(workbook, sheet.getRow(i + 8), 6, CellType.STRING,
								HorizontalAlignment.LEFT);
						contactValueCell.setCellValue(studentProfileList.get(i).getContact());
						int scoreColumnBegin = 7;
						if (!subjectMap.isEmpty()) {
							for (Entry<Subject, Integer> entry : subjectMap.entrySet()) {
								List<Unit> unitList = iUnitRepository
										.findBySubjectIdAndIsDisableFalse(entry.getKey().getId());
								if (!unitList.isEmpty()) {

									System.out.println(unitList.size() + " unit");
									for (Unit unit : unitList) {
										for (int j = scoreColumnBegin; j < unitList.size() + scoreColumnBegin; j++) {
											List<ExerciseTaken> exerciseTakenList = iExerciseTakenRepository
													.findByUnitIdAndAccountId(unit.getId(),
															studentProfileList.get(i).getAccount().getId());

											double score = 0;
											if (!exerciseTakenList.isEmpty()) {
												double sumTotalScore = 0;
												for (ExerciseTaken exerciseTaken : exerciseTakenList) {
													sumTotalScore += exerciseTaken.getTotalScore();
												}

												score = sumTotalScore / exerciseTakenList.size();
												score = Double.valueOf(new DecimalFormat("#.#").format(score));

//													isTaken = true;
											}

//												if (isTaken) {
											Cell exerciseValueCell = createOneNormalCell(workbook, sheet.getRow(i + 8),
													j, CellType.NUMERIC, HorizontalAlignment.CENTER);
											exerciseValueCell.setCellValue(score);
//												}
//											else {
//												createOneWarningCell(workbook, sheet.getRow(i + 10), j, "Not yet!");
//											}
										}

									}

									scoreColumnBegin += unitList.size();

									List<ProgressTest> progressTestList = iProgressTestRepository
											.findBySubjectIdAndIsDisableFalse(entry.getKey().getId());
									if (!progressTestList.isEmpty()) {
										for (ProgressTest progressTest : progressTestList) {
											for (int j = scoreColumnBegin; j < progressTestList.size()
													+ scoreColumnBegin; j++) {
												List<ExerciseTaken> exerciseTakenList = iExerciseTakenRepository
														.findByProgressTestIdAndAccountId(progressTest.getId(),
																studentProfileList.get(i).getAccount().getId());

												double score = 0;
												if (!exerciseTakenList.isEmpty()) {
													double sumTotalScore = 0;
													for (ExerciseTaken exerciseTaken : exerciseTakenList) {
														sumTotalScore += exerciseTaken.getTotalScore();
													}

													score = sumTotalScore / exerciseTakenList.size();
													score = Double.valueOf(new DecimalFormat("#.#").format(score));

//														isTaken = true;
												}

//													if (isTaken) {
												Cell exerciseValueCell = createOneNormalCell(workbook,
														sheet.getRow(i + 8), j, CellType.NUMERIC,
														HorizontalAlignment.CENTER);
												exerciseValueCell.setCellValue(score);
//													}
//												else {
//													createOneWarningCell(workbook, sheet.getRow(i + 10), j, "Not yet!");
//												}
											}
										}
										scoreColumnBegin += progressTestList.size();
									}
								}
							}
						}
					}
				}
//				
			}

		}

		FileOutputStream fileOut = new FileOutputStream(
				"E:\\" + schoolName + "-" + gradeName + "-FinalScoreExport.xlsx");
		workbook.write(fileOut);
		fileOut.close();
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

	private String getCellValue(Cell cell) {
		String cellValue = null;
		if (cell != null) {
			switch (cell.getCellType()) {

			case NUMERIC:
				System.out.println(cell.getNumericCellValue());
				cellValue = String.valueOf(cell.getNumericCellValue());
				break;
			case STRING:
				System.out.println(cell.getStringCellValue());
				cellValue = cell.getStringCellValue();
				break;
			default:
				break;
			}
		}
		return cellValue;
	}

	private String generateUsername(Classes classes) {
		int gradeName = classes.getSchoolGrade().getGrade().getGradeName();
		String schoolCode = classes.getSchoolGrade().getSchool().getSchoolCode()
				+ classes.getSchoolGrade().getSchool().getSchoolCount();

		long studentCount = countStudent(classes);
		String username = schoolCode + String.format("%02d", gradeName) + String.format("%03d", studentCount);
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
			// TODO: handle exception
		}

		return fileName;
	}

//	private String parseToCell(int rowIndex, int columnIndex) {
//		String columnName = "";
//		switch (columnIndex) {
//		case 1:
//			columnName = "B";
//			break;
//		case 2:
//			columnName = "C";
//			break;
//		case 3:
//			columnName = "D";
//			break;
//		case 4:
//			columnName = "E";
//			break;
//		case 5:
//			columnName = "F";
//			break;
//		case 6:
//			columnName = "G";
//			break;
//		}
//
//		return (columnName + rowIndex);
//	}

	private long countStudent(Classes classes) {
		String className = classes.getClassName();
		long studentCount = 1;

		if (!className.equalsIgnoreCase(PENDING_STATUS) && classes.getId() != 0) {
			List<Classes> classesList = iClassRepository.findBySchoolGradeIdAndStatus(classes.getSchoolGrade().getId(),
					ACTIVE_STATUS);
			classesList.addAll(
					iClassRepository.findBySchoolGradeIdAndStatus(classes.getSchoolGrade().getId(), INACTIVE_STATUS));
			if (!classesList.isEmpty()) {
				List<StudentProfile> studentProfileList = new ArrayList<>();
				for (Classes classes2 : classesList) {
					StudentProfile studentProfile = iStudentProfileRepository
							.findFirstByClassesIdAndStatusLikeOrderByStudentCountDesc(classes2.getId(), ACTIVE_STATUS);
					if (studentProfile != null) {
						studentProfileList.add(studentProfile);
					}
				}
				if (!studentProfileList.isEmpty()) {
					for (StudentProfile studentProfile : studentProfileList) {
						if (studentProfile.getStudentCount() > studentCount) {
							studentCount = studentProfile.getStudentCount();
						}
					}
					studentCount++;
				}
			}
		} else {
			StudentProfile studentProfile = iStudentProfileRepository
					.findFirstByClassesIdAndStatusLikeOrderByStudentCountDesc(classes.getId(), classes.getStatus());

			if (studentProfile != null) {
				studentCount = studentProfile.getStudentCount() + 1;
			}
		}

		return studentCount;
	}

}
