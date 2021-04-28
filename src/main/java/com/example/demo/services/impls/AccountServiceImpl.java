package com.example.demo.services.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Account;
import com.example.demo.repositories.IAccountRepository;
import com.example.demo.services.IAccountService;

@Service
public class AccountServiceImpl implements IAccountService {

	@Autowired
	private IAccountRepository iAccountRepository;

	@Override
	public String createAccount(String username, String password, String fullName) {
		Account checkUsername = iAccountRepository.findByUsernameAndStatusNot(username, "DELETED");
		if (checkUsername != null) {

			return "EXISTED";
		}
		Account account = new Account();
		account.setUsername(username);
		account.setPassword(password);
		account.setFullName(fullName);
		account.setRoleId(3);
		account.setStatus("ACTIVE");
		iAccountRepository.save(account);
		return null;
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
