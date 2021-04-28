package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.IAccountService;
import com.example.demo.services.impls.AccountServiceImpl;

@CrossOrigin
@RestController
@RequestMapping("/account")
public class AccountController {
	@Autowired
	IAccountService iAccountService;

	@Autowired
	private AccountServiceImpl accountServiceImpl;

	@PostMapping("/login")
	public ResponseEntity<Long> login(@RequestParam String username, @RequestParam String password) {
		long response = accountServiceImpl.login(username, password);

		return ResponseEntity.ok(response);
	}
}
