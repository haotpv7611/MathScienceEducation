package com.example.demo.services.impls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Account;
import com.example.demo.models.StudentProfile;
import com.example.demo.repositories.IAccountRepository;
import com.example.demo.repositories.IStudentProfileRepository;
import com.example.demo.services.IAccountService;

@Service
public class AccountServiceImpl implements IAccountService {
	Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	private final String DELETE_STATUS = "DELETED";
	private final String DEFAULT_PASSWORD = "123456";

	@Autowired
	private IAccountRepository iAccountRepository;

	@Autowired
	private IStudentProfileRepository iStudentProfileRepository;

//	@Override
//	public String createAccount(String username, String password, String fullName) {
//		Account checkUsername = iAccountRepository.findByUsernameAndStatusNot(username, "DELETED");
//		if (checkUsername != null) {
//
//			return "EXISTED";
//		}
//		Account account = new Account();
//		account.setUsername(username);
//		account.setPassword(password);
//		account.setFullName(fullName);
//		account.setRoleId(3);
//		account.setStatus("ACTIVE");
//		iAccountRepository.save(account);
//		return null;
//	}

	@Override
	public String resetPassword(long studentId) {
		try {
			StudentProfile studentProfile = iStudentProfileRepository.findByIdAndStatusNot(studentId, DELETE_STATUS);
			if (studentProfile == null) {
				throw new ResourceNotFoundException();
			}
			Account account = studentProfile.getAccount();
			account.setPassword(DEFAULT_PASSWORD);
			iAccountRepository.save(account);

		} catch (Exception e) {
			logger.error("Reset pw for student with studentId = " + studentId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "RESET FAIL!";
		}

		return "RESET SUCCESS!";
	}

	public long login(String username, String password) {
		Account account = iAccountRepository.findByUsernameAndPasswordAndStatus(username, password, "ACTIVE");
		System.out.println(account);
		if (account != null) {
			System.out.println(account.getId());
			return account.getId();
		} else {

			return 0;
		}
	}
}
