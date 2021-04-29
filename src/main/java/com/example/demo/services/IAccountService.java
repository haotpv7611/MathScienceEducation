package com.example.demo.services;

public interface IAccountService {
	String createAccount(String username, String password, String fullName);
	
	String resetPassword(long studentId);
}
