package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.AccountRequestDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.services.IAccountService;
import com.example.demo.services.impls.AccountServiceImpl;

@CrossOrigin
@RestController
//@RequestMapping("/account")
public class AccountController {
	@Autowired
	private IAccountService iAccountService;

	@Autowired
	private AccountServiceImpl accountServiceImpl;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
		String response = accountServiceImpl.login(username, password);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/password/reset")
	// @PreAuthorize("hasRole('admin')")
	public ResponseEntity<String> resetPassword(@PathVariable long studentId) {
		String response = iAccountService.resetPassword(studentId);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/account/{id}")
	public ResponseEntity<Object> findAccountById(@PathVariable long id) {
		Object response = iAccountService.findAccountById(id);
		if (response.equals("NOT FOUND!")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.equals("FIND FAIL!")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/account/staff")
	public ResponseEntity<Object> findAllStaffAccount() {
		Object response = iAccountService.findAllStaffAccount();
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/account/create")
	public ResponseEntity<String> createAccount(@RequestBody AccountRequestDTO accountRequestDTO) {
		String response = iAccountService.createAccount(accountRequestDTO);
		if (response.equals("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("EXISTED")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PutMapping("/account/{id}")
	public ResponseEntity<String> updateAccount(@PathVariable long id,
			@RequestBody AccountRequestDTO accountRequestDTO) {
		String response = iAccountService.updateAccount(id, accountRequestDTO);
		if (response.equals("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PutMapping("/account/")
	public ResponseEntity<String> changeStatusAccount(@RequestBody ListIdAndStatusDTO listIdAndStatusDTO) {
		String response = iAccountService.changeStatusAccount(listIdAndStatusDTO);
		if (response.equals("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/credential")
	public ResponseEntity<Object> getUserCredential(@RequestParam String token) {
		Object response = iAccountService.getUserCredential(token);
		if (response.equals("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.equals("GET CREDENTIAL FAIL!")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

}
