package com.example.demo.services.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.ClassResponseDTO;
import com.example.demo.services.IClassService;

@Service
public class AccountServiceImpl {

	@Autowired
	IClassService iClassService;

	public void readData(MultipartFile file, long gradeId, long schoolId) throws IOException {
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		
		int numberOfSheets = workbook.getNumberOfSheets();
		System.out.println(numberOfSheets);
//		List<ClassResponseDTO> classList = iClassService.findBySchoolGradeId(gradeId, schoolId);
//		List<String> className = new ArrayList<>();
//		for (ClassResponseDTO classResponseDTO : classList) {
//			className.add(classResponseDTO.getClassName());
//		}
//
//		for (int i = 0; i < numberOfSheets; i++) {
//			Sheet sheet = workbook.getSheetAt(i);
//			System.out.println(sheet.getSheetName());
//			if (className.contains(sheet.getSheetName())) {
//				System.out.println("Duplicate " + sheet.getSheetName());
//			}
//			Iterator<Row> rowList = sheet.iterator();
//			
//			while(rowList.hasNext()) {
//				Row currentRow = rowList.next();
////				System.out.println(currentRow);
//				
//				Iterator<Cell> cellList = currentRow.iterator();
//				while (cellList.hasNext()) {
//					Cell cell = cellList.next();
//					System.out.println(cell);
//					
////					System.out.println(cell.getStringCellValue());
//					
//				}
//			}
			
			


//			
//			int numberOfRow = sheet.getPhysicalNumberOfRows();
//			System.out.println(numberOfRow);
//			
//			for (int j = 2; j < numberOfRow; j++) {
//				XSSFRow row = (XSSFRow) sheet.getRow(j);
//				row.g
//				System.out.println(row);
//				
//			}
//			
//			
//		}

		
		

	}
}
