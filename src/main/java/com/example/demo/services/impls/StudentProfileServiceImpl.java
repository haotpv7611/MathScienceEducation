package com.example.demo.services.impls;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellRange;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
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
import com.example.demo.models.Exercise;
import com.example.demo.models.Grade;
import com.example.demo.models.Lesson;
import com.example.demo.models.School;
import com.example.demo.models.SchoolGrade;
import com.example.demo.models.StudentProfile;
import com.example.demo.models.Subject;
import com.example.demo.models.Unit;
import com.example.demo.repositories.IAccountRepository;
import com.example.demo.repositories.IClassRepository;
import com.example.demo.repositories.IExerciseRepository;
import com.example.demo.repositories.IGradeRepository;
import com.example.demo.repositories.ILessonRepository;
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
	private final String DELETE_STATUS = "DELETED";
	private final String PENDING_STATUS = "PENDING";
	private final String DEFAULT_PASSWORD = "abc123456";
	private final int STUDENT_ROLE = 3;
	private final int FIRST_ROW = 7;
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
	ISubjectRepository iSubjectRepository;

	@Autowired
	private IUnitRepository iUnitRepository;

	@Autowired
	private ILessonRepository iLessonRepository;

	@Autowired
	private IExerciseRepository iExerciseRepository;

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
		if (classes == null) {
			throw new ResourceNotFoundException();
		}
		int gradeName = classes.getSchoolGrade().getGrade().getGradeName();
		String schoolCode = classes.getSchoolGrade().getSchool().getSchoolCode()
				+ classes.getSchoolGrade().getSchool().getSchoolCount();

		long studentCount = countStudent(classes);
		String username = generateUsername(schoolCode, gradeName, studentCount);
		String fullName = studentRequestDTO.getFullName();
		Account account = new Account(username, DEFAULT_PASSWORD, fullName, STUDENT_ROLE, ACTIVE_STATUS);
		iAccountRepository.save(account);

		String DoB = studentRequestDTO.getDoB();
		String gender = studentRequestDTO.getGender();
		String parentName = studentRequestDTO.getParentName();
		String contact = studentRequestDTO.getContact();
		StudentProfile studentProfile = new StudentProfile(DoB, gender, parentName, contact, ACTIVE_STATUS,
				studentCount, account, classes);
		iStudentProfileRepository.save(studentProfile);

		return "CREATE SUCCESS!";
	}

	@Override
	@Transactional
	public String importStudent(MultipartFile file, long schoolId, int gradeId) throws IOException {
		// open file
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId,
				DELETE_STATUS);

		Iterator<Sheet> sheetIterator = workbook.sheetIterator();
		// create class by each sheetName
		while (sheetIterator.hasNext()) {
			Sheet sheet = sheetIterator.next();
			Classes classes = new Classes(sheet.getSheetName(), ACTIVE_STATUS, schoolGrade);
			iClassRepository.save(classes);
			System.out.println(classes.getId());
			System.out.println(classes);

			Iterator<Row> rowIterator = sheet.rowIterator();
			while (rowIterator.hasNext()) {

				Row row = rowIterator.next();
				System.out.println(row.getRowNum());
				if (row.getRowNum() < FIRST_ROW) {
					continue;
				} else {
					// get accountId user input
					long accountId = 0;
					if (row.getCell(1) != null) {
						if (row.getCell(1).getCellType() != CellType.BLANK) {
							String studentCode = row.getCell(1).getStringCellValue();
							accountId = Long.parseLong(studentCode.substring(2, studentCode.length()));
							System.out.println("run here");
							System.out.println("accId: " + accountId);
						}
					}

					// if not existed --> create new
					// else update username and classesId
					if (accountId == 0) {
						System.out.println("error here");
						String fullName = row.getCell(2).getStringCellValue();
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-YYYY");
						String DoB = sdf.format(row.getCell(3).getDateCellValue());
						String gender = row.getCell(4).getStringCellValue();
						String parentName = row.getCell(5).getStringCellValue();
						String contact = row.getCell(6).getStringCellValue();

						StudentRequestDTO studentRequestDTO = new StudentRequestDTO(classes.getId(), fullName, DoB,
								gender, parentName, contact);
						createStudenProfile(studentRequestDTO);
					} else {
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
						studentProfile.setStudentCount(studentCount);
						iStudentProfileRepository.save(studentProfile);
					}
				}
			}

		}
		workbook.close();
		return "OK";
	}

	public void exportScore(long schoolId, int gradeId, long subjectId) throws IOException {
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

		// header
		String schoolName = school.getSchoolName();
		String schoolCode = school.getSchoolCode() + school.getSchoolCount();
		int gradeName = grade.getGradeName();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		String exportDate = sdf.format(new Date());
		String subjectName = subject.getSubjectName();

		List<Unit> unitList = iUnitRepository.findBySubjectIdAndIsDisableFalseOrderByUnitNameAsc(subjectId);
		List<Exercise> exerciseList = new ArrayList<>();
		Map<String, Integer> unitMap = new LinkedHashMap<>();
		Map<String, Integer> lessonMap = new LinkedHashMap<>();
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
							lessonMap.put("Lesson " + lesson.getLessonName(), exerciseSize);
						}
					}

					if (totalExercise > 0) {
						unitMap.put("Unit " + unit.getUnitName(), totalExercise);
					}
				}
			}
		}
		
		

		Workbook workbook = new XSSFWorkbook();
		if (!classesList.isEmpty()) {
			for (Classes classes : classesList) {
				List<StudentProfile> studentProfileList = new ArrayList<>();
				if (!classesList.isEmpty()) {
					studentProfileList
							.addAll(iStudentProfileRepository.findByClassesIdAndStatusNot(subjectId, subjectName));
				}

				Sheet sheet = workbook.createSheet(classes.getClassName());
				sheet.setColumnWidth(0, 1500);
				sheet.setColumnWidth(1, 3500);
				sheet.setColumnWidth(2, 9000);
				for (int i = 3; i < exerciseList.size() + 3; i++) {
					sheet.setColumnWidth(i, 3500);
				}

				System.out.println(studentProfileList.size() + 5);

				for (int i = 0; i < studentProfileList.size() + 10; i++) {
					if (i == 5 || i == 6) {
						continue;
					}
					sheet.createRow(i);
				}

				Cell schoolNameCell = createOneCell(workbook, sheet.getRow(0), 2, CellType.STRING,
						HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, BorderStyle.MEDIUM,
						IndexedColors.BLACK.getIndex(), true, (short) 14);
				schoolNameCell.setCellValue("School Name:");
				Cell schoolNameValueCell = createArrangeCell(workbook, sheet, 0, 0, 3, 5, sheet.getRow(0), 3,
						CellType.STRING, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.MEDIUM,
						IndexedColors.RED.getIndex(), true, (short) 16);
				schoolNameValueCell.setCellValue(schoolName);

				Cell schoolCodeCell = createOneCell(workbook, sheet.getRow(1), 2, CellType.STRING,
						HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, BorderStyle.MEDIUM,
						IndexedColors.BLACK.getIndex(), true, (short) 14);
				schoolCodeCell.setCellValue("School Code:");
				Cell schoolCodeValueCell = createArrangeCell(workbook, sheet, 1, 1, 3, 5, sheet.getRow(1), 3,
						CellType.STRING, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.MEDIUM,
						IndexedColors.RED.getIndex(), true, (short) 16);
				schoolCodeValueCell.setCellValue(schoolCode);

				Cell gradeCell = createOneCell(workbook, sheet.getRow(2), 2, CellType.STRING, HorizontalAlignment.RIGHT,
						VerticalAlignment.CENTER, BorderStyle.MEDIUM, IndexedColors.BLACK.getIndex(), true, (short) 14);
				gradeCell.setCellValue("Grade:");
				Cell gradeValueCell = createArrangeCell(workbook, sheet, 2, 2, 3, 5, sheet.getRow(2), 3,
						CellType.STRING, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.MEDIUM,
						IndexedColors.RED.getIndex(), true, (short) 16);
				gradeValueCell.setCellValue(gradeName);

				Cell exportDateCell = createOneCell(workbook, sheet.getRow(3), 2, CellType.STRING,
						HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, BorderStyle.MEDIUM,
						IndexedColors.BLACK.getIndex(), true, (short) 14);
				exportDateCell.setCellValue("Export Date:");
				Cell exportDateValueCell = createArrangeCell(workbook, sheet, 3, 3, 3, 5, sheet.getRow(3), 3,
						CellType.STRING, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.MEDIUM,
						IndexedColors.RED.getIndex(), true, (short) 16);
				exportDateValueCell.setCellValue(exportDate);

				Cell subjectNameCell = createOneCell(workbook, sheet.getRow(4), 2, CellType.STRING,
						HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, BorderStyle.MEDIUM,
						IndexedColors.BLACK.getIndex(), true, (short) 14);
				subjectNameCell.setCellValue("Subject Name:");
				Cell subjectNameValueCell = createArrangeCell(workbook, sheet, 4, 4, 3, 5, sheet.getRow(4), 3,
						CellType.STRING, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.MEDIUM,
						IndexedColors.RED.getIndex(), true, (short) 16);
				subjectNameValueCell.setCellValue(subjectName);

				Cell noCell = createArrangeCell(workbook, sheet, 7, 9, 0, 0, sheet.getRow(7), 0, CellType.STRING,
						HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN,
						IndexedColors.BLACK.getIndex(), true, (short) 13);
				noCell.setCellValue("No.");
				Cell studentId = createArrangeCell(workbook, sheet, 7, 9, 1, 1, sheet.getRow(7), 1, CellType.STRING,
						HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN,
						IndexedColors.BLACK.getIndex(), true, (short) 13);
				studentId.setCellValue("StudentId");
				Cell fullNameCell = createArrangeCell(workbook, sheet, 7, 9, 2, 2, sheet.getRow(7), 2, CellType.STRING,
						HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN,
						IndexedColors.BLACK.getIndex(), true, (short) 13);
				fullNameCell.setCellValue("Fullname");

				for (int i = 3; i < exerciseList.size() + 3; i++) {
					Cell cell = createOneCell(workbook, sheet.getRow(9), i, CellType.STRING, HorizontalAlignment.CENTER,
							VerticalAlignment.CENTER, BorderStyle.THIN, IndexedColors.BLACK.getIndex(), true,
							(short) 13);
					cell.setCellValue("Exercise " + exerciseList.get(i - 3).getExerciseName());
				}
				int beginColumn = 3;
				for (Entry<String, Integer> entry : lessonMap.entrySet()) {
					Cell cell = createArrangeCell(workbook, sheet, 8, 8, beginColumn,
							beginColumn + entry.getValue() - 1, sheet.getRow(8), beginColumn, CellType.STRING,
							HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN,
							IndexedColors.BLACK.getIndex(), true, (short) 13);
					beginColumn += entry.getValue();
					cell.setCellValue(entry.getKey());
				}
				beginColumn = 3;
				for (Entry<String, Integer> entry : unitMap.entrySet()) {
					Cell cell = createArrangeCell(workbook, sheet, 7, 7, beginColumn,
							beginColumn + entry.getValue() - 1, sheet.getRow(7), beginColumn, CellType.STRING,
							HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN,
							IndexedColors.BLACK.getIndex(), true, (short) 13);
					beginColumn += entry.getValue();
					cell.setCellValue(entry.getKey());
				}
				
				for (int i = 0; i < studentProfileList.size(); i++) {
					Cell noValueCell = createOneCell(workbook, sheet.getRow(i + 10), 0, CellType.STRING, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN, IndexedColors.BLACK.getIndex(), false,(short) 13);
					noValueCell.setCellValue(i + 1);
					Cell studentIdValueCell = createOneCell(workbook, sheet.getRow(i + 10), 1, CellType.STRING, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN, IndexedColors.BLACK.getIndex(), false,(short) 13);
					studentIdValueCell.setCellValue("MJ" + String.format("%06d",  studentProfileList.get(i).getId()));
					Cell fullNameValueCell = createOneCell(workbook, sheet.getRow(i + 10), 2, CellType.STRING, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, BorderStyle.THIN, IndexedColors.BLACK.getIndex(), false,(short) 13);
					fullNameValueCell.setCellValue(studentProfileList.get(i).getAccount().getFullName());
				}

			}
		}

		FileOutputStream fileOut = new FileOutputStream("E:\\" + schoolName + "-" + gradeName + "-ScoreExport.xlsx");
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();

	}

	private Cell createOneCell(Workbook workbook, Row row, int column, CellType cellType,
			HorizontalAlignment halignment, VerticalAlignment valignment, BorderStyle border, short color, boolean bold,
			short fontSize) {
		Cell cell = row.createCell(column, cellType);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(halignment);
		cellStyle.setVerticalAlignment(valignment);
		cellStyle.setBorderTop(border);
		cellStyle.setBorderBottom(border);
		cellStyle.setBorderLeft(border);
		cellStyle.setBorderRight(border);

		Font font = workbook.createFont();
		font.setColor(color);
		font.setBold(bold);
		font.setFontName("Times New Roman");
		font.setFontHeightInPoints(fontSize);
		cellStyle.setFont(font);

		cell.setCellStyle(cellStyle);
		return cell;
	}

	private Cell createArrangeCell(Workbook workbook, Sheet sheet, int rowBegin, int rowEnd, int columnBegin,
			int columnEnd, Row row, int column, CellType cellType, HorizontalAlignment halignment,
			VerticalAlignment valignment, BorderStyle border, short color, boolean bold, short fontSize) {
		Cell cell = row.createCell(column, cellType);
		CellRangeAddress cellRangeAddress = new CellRangeAddress(rowBegin, rowEnd, columnBegin, columnEnd);
		sheet.addMergedRegion(cellRangeAddress);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(halignment);
		cellStyle.setVerticalAlignment(valignment);
		cellStyle.setBorderTop(border);
		cellStyle.setBorderBottom(border);
		cellStyle.setBorderLeft(border);
		cellStyle.setBorderRight(border);
		RegionUtil.setBorderTop(border, cellRangeAddress, sheet);
		RegionUtil.setBorderBottom(border, cellRangeAddress, sheet);
		RegionUtil.setBorderLeft(border, cellRangeAddress, sheet);
		RegionUtil.setBorderRight(border, cellRangeAddress, sheet);

		Font font = workbook.createFont();
		font.setColor(color);
		font.setBold(bold);
		font.setFontName("Times New Roman");
		font.setFontHeightInPoints(fontSize);
		cellStyle.setFont(font);

		cell.setCellStyle(cellStyle);
		return cell;
	}

//	private CellStyle formatCell(Workbook workbook, CellStyle cellStyle, Font font, HorizontalAlignment alignment,
//			BorderStyle border, short color, boolean bold, short fontSize) {
//		cellStyle = workbook.createCellStyle();
//		font = workbook.createFont();
//		cellStyle.setAlignment(alignment);
//		cellStyle.setBorderTop(border);
//		cellStyle.setBorderBottom(border);
//		cellStyle.setBorderLeft(border);
//		cellStyle.setBorderTop(border);
//		font.setColor(color);
//		font.setBold(bold);
//		font.setFontName("Times New Roman");
//		font.setFontHeightInPoints(fontSize);
//		cellStyle.setFont(font);
//		return cellStyle;
//	}
//
//	private CellStyle formatRegionCell(Workbook workbook, CellStyle cellStyle, Font font, HorizontalAlignment alignment,
//			Sheet sheet, CellRangeAddress cellRangeAddress, BorderStyle border, short color, boolean bold,
//			short fontSize) {
////		CellRange<Cell> cellRange = cellRangeAddress;
//		cellStyle = workbook.createCellStyle();
//		font = workbook.createFont();
//		cellStyle.setAlignment(alignment);
//		cellStyle.setBorderTop(border);
//		cellStyle.setBorderBottom(border);
//		cellStyle.setBorderLeft(border);
//		cellStyle.setBorderTop(border);
//		RegionUtil.setBorderTop(border, cellRangeAddress, sheet);
//		RegionUtil.setBorderBottom(border, cellRangeAddress, sheet);
//		RegionUtil.setBorderLeft(border, cellRangeAddress, sheet);
//		RegionUtil.setBorderRight(border, cellRangeAddress, sheet);
//		font.setColor(color);
//		font.setBold(bold);
//		font.setFontName("Times New Roman");
//		font.setFontHeightInPoints(fontSize);
//		cellStyle.setFont(font);
//		return cellStyle;
//	}

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
	public void validateStudentFile(MultipartFile file, long schoolId, int gradeId) throws IOException {
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
//			error = validateSheetData(sheet, classNameList);
			List<Cell> cellList = validateSheetData(sheet, classNameList);
			if (!cellList.isEmpty()) {
				CellStyle style = workbook.createCellStyle();
				style.setFillForegroundColor(IndexedColors.RED.getIndex());
				style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				style.setAlignment(HorizontalAlignment.CENTER);
				Font font = workbook.createFont();
				font.setColor(IndexedColors.BLACK.getIndex());
				font.setBold(true);
				font.setStrikeout(true);
				style.setFont(font);

				for (Cell cell : cellList) {

					cell.setCellStyle(style);

				}
				String fileName = file.getOriginalFilename();
				FileOutputStream fileOut = new FileOutputStream("E:\\" + "Error-" + fileName);
				workbook.write(fileOut);
				fileOut.close();
				workbook.close();
			}
		}

//		workbook.close();

//		if (!error.trim().isEmpty()) {
//			return error.trim();
//		}

//		return "OK";

	}

	private List<Cell> validateSheetData(Sheet sheet, List<String> classNameList) {
		List<Cell> cellList = new ArrayList<>();
		DataFormatter dataFormatter = new DataFormatter();

//		String error = "";
		// check className existed
//		if (classNameList.contains(sheet.getSheetName())) {
//			error += "\nClass with sheet name = " + sheet.getSheetName() + " EXISTED!";
//		}
		Iterator<Row> rowIterator = sheet.rowIterator();

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row.getRowNum() < FIRST_ROW) {
				continue;
			}
			for (int i = FIRST_CELL; i < (LAST_CELL + 1); i++) {
				Cell cell = row.getCell(i);
				switch (i) {
				case 1:
					if (cell != null) {
						if (cell.getCellType() != CellType.STRING) {
							cellList.add(cell);
						}
					}
					break;

				case 3:
					if (cell == null) {
						cellList.add(cell);
					} else {
						if (cell.getCellType() == CellType.BLANK) {
							cellList.add(cell);
						} else {
							if (cell.getCellType() != CellType.NUMERIC) {
								cellList.add(cell);
							} else {
								String dateFormat = dataFormatter.formatCellValue(cell);
								System.out.println(dateFormat);
								String regex = "\\d{1,2}/\\d{1,2}/\\d{2}";
								String regex1 = "\\d{1,2}-[a-zA-Z]{3}-\\d{2}";
								if (!dateFormat.matches(regex) && !dateFormat.matches(regex1)) {
									cellList.add(cell);
									System.out.println("wrong regex");
								} else {
									Date date = new Date();
									if (cell.getDateCellValue().after(date)) {
										cellList.add(cell);
									}
								}
							}
						}
					}
					break;

				case 2:
				case 4:
				case 5:
				case 6:
					if (cell == null) {
						cellList.add(cell);
					} else {
						if (cell.getCellType() == CellType.BLANK) {
							cellList.add(cell);
						} else {
							if (cell.getCellType() != CellType.STRING) {
								cellList.add(cell);
							}
						}
					}
				}
			}
		}

		return cellList;
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
		List<Classes> classesList = classes.getSchoolGrade().getClassList();
		List<StudentProfile> studentProfileList = new ArrayList<>();
		if (!classesList.isEmpty()) {
			for (Classes classes2 : classesList) {
				if (classes2.getStatus().equals(PENDING_STATUS)) {
					classesList.remove(classes2);
				} else {
					StudentProfile studentProfile = iStudentProfileRepository
							.findFirstByClassesIdAndStatusLikeOrderByStudentCountDesc(classes2.getId(), ACTIVE_STATUS);

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
