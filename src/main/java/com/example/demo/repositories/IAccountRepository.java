package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Account;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {

	// find Account by username and isDisable = false
	// return Account
	Account findByIdAndStatus(long id, String status);

	Account findByUsername(String username);
	
	List<Account> findByRoleId(int roleId);

	Account findByUsernameAndStatus(String username, String status);
	
	Account findByUsernameAndStatusNot(String username, String status);

	Account findByIdAndStatusNot(long id, String status);

	Account findByUsernameAndPasswordAndStatus(String username, String password, String status);
}
