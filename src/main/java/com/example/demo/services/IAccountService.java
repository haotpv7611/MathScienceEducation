package com.example.demo.services;

import com.example.demo.dtos.AccountRequestDTO;
import com.example.demo.dtos.AccountResponseDTO;

public interface IAccountService {
	
	String createAccount(String username, String password, int role);
	
	String resetPassword(long studentId);
	
	String login(AccountRequestDTO accountRequestDTO);
	
	AccountResponseDTO getUserCredential(String token);
}
