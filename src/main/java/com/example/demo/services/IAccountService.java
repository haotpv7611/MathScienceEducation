package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.AccountRequestDTO;
import com.example.demo.dtos.AccountResponseDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;

public interface IAccountService {

	Object findAccountById(long id);

	List<AccountResponseDTO> findAllStaffAccount();

	String createAccount(AccountRequestDTO accountRequestDTO);

	String updateAccount(long id, AccountRequestDTO accountRequestDTO);

	String changeStatusAccount(ListIdAndStatusDTO listIdAndStatusDTO);

	String resetPassword(long studentId);
	
	String checkContact(String username, String contact);
	
	String changeStudentPasswordByUsername(String username, String password);
	
	String changeStudentPasswordByAccountId(long accountId, String oldPassword, String newPassword);

	String login(String username, String password);

	Object getUserCredential(String token);
}
