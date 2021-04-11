package com.example.demo.services.impls;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Account;
import com.example.demo.repositories.IAccountRepository;
import com.example.demo.services.IAccountService;
import com.example.demo.services.IClassService;

@Service
public class AccountServiceImpl implements IAccountService{

	@Autowired
	private IClassService iClassService;
	
	@Autowired
	private IAccountRepository iAccountRepository;

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

	@Override
	public String createAccount(String username, String password, String firstName, String lastName) {
		Account checkUsername = iAccountRepository.findByUsernameAndStatusNot(username, "DELETED");
		if (checkUsername != null) {
			
			return "EXISTED";
		}		
		Account account = new Account();
		account.setUsername(username);
		account.setPassword(password);
		account.setFirstName(firstName);
		account.setLastName(lastName);
		account.setRoleId(3);
		account.setStatus("ACTIVE");
		iAccountRepository.save(account);
		return null;
	}
	
	public void changeStatusOneAccount(long id) {
		try {
			Account account = iAccountRepository.findByIdAndStatusNot(id, "DELETED");
			if (account == null) {
				throw new ResourceNotFoundException();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public long login(String username, String password) {
		Account account = iAccountRepository.findByUsernameAndPasswordAndStatus(username, password, "ACTIVE");
		if (account != null) {
			
			return account.getId();
		}else {
			
			return 0;
		}
		
	}
}
