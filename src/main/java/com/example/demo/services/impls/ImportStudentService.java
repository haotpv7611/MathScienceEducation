package com.example.demo.services.impls;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.ClassResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Account;
import com.example.demo.models.Classes;
import com.example.demo.models.SchoolGrade;
import com.example.demo.models.StudentProfile;
import com.example.demo.repositories.IAccountRepository;
import com.example.demo.repositories.IClassRepository;
import com.example.demo.repositories.ISchoolGradeRepository;
import com.example.demo.repositories.IStudentProfileRepository;
import com.example.demo.services.IClassService;
import com.example.demo.services.IGradeService;
import com.example.demo.services.ISchoolGradeService;
import com.example.demo.services.ISchoolService;

@Service
public class ImportStudentService {
	@Autowired
	ISchoolGradeService iSchoolGradeService;

	@Autowired
	IGradeService iGradeService;

	@Autowired
	ISchoolService iSchoolService;

	@Autowired
	IClassService iClassService;

	@Autowired
	IClassRepository iClassRepository;

	@Autowired
	ISchoolGradeRepository iSchoolGradeRepository;

	@Autowired
	IAccountRepository iAccountRepository;
	@Autowired
	IStudentProfileRepository iStudentProfileRepository;

	public void importStudent(MultipartFile file, long gradeId, long schoolId) throws IOException, ParseException {
		SchoolGrade schoolGrade = iSchoolGradeRepository.findByGradeIdAndSchoolIdAndStatusNot(gradeId, schoolId, "DELETED");
		
		if (schoolGrade == null) {
			throw new ResourceNotFoundException();
		}
		System.out.println("SGId: " + schoolGrade.getId());
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		int numberOfSheets = workbook.getNumberOfSheets();

		String checkClass = checkClassExisted(file, gradeId, schoolId);
		if (!checkClass.equals("OK!")) {

		}

		String schoolCode = schoolGrade.getSchool().getSchoolCode() + schoolGrade.getSchool().getSchoolCount();
		System.out.println("71: " + schoolCode);

		for (int i = 0; i < 1; i++) {
			Sheet sheet = workbook.getSheetAt(i);
//			System.out.println(sheet.getSheetName());
			String className = workbook.getSheetName(0);

			List<Row> rowList = readData(file, gradeId, schoolId, sheet);
			for (int j = 1; j < 3; j++) {
				String firstName = rowList.get(j).getCell(2).getStringCellValue();
				String lastName = rowList.get(j).getCell(3).getStringCellValue();

				DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
				Date DOBDate = rowList.get(j).getCell(4).getDateCellValue();
				String DOB = df.format(DOBDate);
				String gender = rowList.get(j).getCell(5).getStringCellValue();
				String parentName = rowList.get(j).getCell(6).getStringCellValue();
				String parentPhone = rowList.get(j).getCell(7).getStringCellValue();
				List<Classes> classList = schoolGrade.getClassList();
				System.out.println("class size: " + classList.size());
				int countStudent = 0;
				for (Classes class1 : classList) {
					if (class1.getStudentProfileList() != null)
						countStudent += class1.getStudentProfileList().size();
//					else
//						countStudent = 1;

				}
				System.out.println("96: " + countStudent);
				Classes classes = new Classes();
				classes.setClassName(className);
				classes.setStatus("ACTIVE");
				iClassRepository.save(classes);
				
				String gradeName = String.format("%02d", schoolGrade.getGrade().getGradeName());
				System.out.println(gradeName);
				String totalStudent = String.format("%03d", (countStudent + 1)).substring(0, 3);
				System.out.println(totalStudent);
				String username = schoolCode + gradeName + totalStudent;
				Account account = new Account(username, "123456", firstName, lastName, 3, "ACTIVE");
				Account newAccount = iAccountRepository.save(account);
				
				System.out.println("104: " + account.getId());

				StudentProfile studentProfile = new StudentProfile(DOB, gender, parentName, parentPhone,
						newAccount, classes);
				studentProfile.setStatus("ACTIVE");
				iStudentProfileRepository.save(studentProfile);

			}

		}

	}

	public List<Row> readData(MultipartFile file, long gradeId, long schoolId, Sheet sheet) throws IOException {
//		Workbook workbook = new XSSFWorkbook(file.getInputStream());
//		int numberOfSheets = workbook.getNumberOfSheets();
//
//		List<ClassResponseDTO> classList = iClassService.findBySchoolGradeId(gradeId, schoolId);
//		List<String> className = new ArrayList<>();
//		for (ClassResponseDTO classResponseDTO : classList) {
//			className.add(classResponseDTO.getClassName());
//		}
//
//		for (int i = 0; i < numberOfSheets; i++) {
//			Sheet sheet = workbook.getSheetAt(i);
		System.out.println("SheetName: " + sheet.getSheetName());
		List<Row> rowList = new ArrayList<>();
		int rowNumber = sheet.getPhysicalNumberOfRows();

		for (int i = 0; i < rowNumber; i++) {
			Row row = sheet.getRow(i);
			rowList.add(row);
		}
		System.out.println("rowsize" + rowList.size());
		return rowList;
	}

	public void validateExcelData(Sheet sheet) throws IOException {
		int rowNumber = sheet.getPhysicalNumberOfRows();
	}

	public String checkClassExisted(MultipartFile file, long gradeId, long schoolId) throws IOException {
		String error = "";
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		int numberOfSheets = workbook.getNumberOfSheets();

//		List<ClassResponseDTO> classList = iClassService.findBySchoolGradeId(gradeId, schoolId);
//		List<String> className = new ArrayList<>();
//		for (ClassResponseDTO classResponseDTO : classList) {
//			className.add(classResponseDTO.getClassName());
//		}

//		for (int i = 0; i < numberOfSheets; i++) {
//			Sheet sheet = workbook.getSheetAt(i);
//			if (className.contains(sheet.getSheetName())) {
//				error += "\nDUPLICATED " + sheet.getSheetName() + "!";
//			}
//		}

		workbook.close();

		if (!error.isEmpty()) {
			return error.trim();
		}

		return "OK!";
	}
}
