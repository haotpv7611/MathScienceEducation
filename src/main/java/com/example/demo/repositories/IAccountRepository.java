package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Account;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {
	
	//find Account by username and isDisable = false
	//return Account
	Account findByIdAndStatus(long id, String status);
	Account findByUsernameAndStatusNot(String username, String status);
	Account findByIdAndStatusNot(long id, String status);
//	boolean loginForTest(String username, String password);

}
