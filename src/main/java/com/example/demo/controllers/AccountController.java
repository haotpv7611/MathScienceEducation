package com.example.demo.controllers;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.services.IAccountService;
import com.example.demo.services.impls.AccountServiceImpl;
import com.example.demo.services.impls.ImportStudentService;

@CrossOrigin
@RestController
@RequestMapping("/account")
public class AccountController {
	@Autowired
	IAccountService iAccountService;

	@Autowired
	AccountServiceImpl accountServiceImpl;

	@Autowired
	ImportStudentService importStudentService;

	@PostMapping
	public void getData(@RequestParam MultipartFile file, @RequestParam long gradeId, @RequestParam long schoolId)
			throws IOException, ParseException {
//		accountServiceImpl.readData(file, gradeId, schoolId);
		importStudentService.importStudent(file, gradeId, schoolId);
	}

	@PostMapping("/login")
	public ResponseEntity<Long> login(@RequestParam String username, @RequestParam String password) {
		long response = accountServiceImpl.login(username, password);
		
		return ResponseEntity.ok(response);
	}
}
