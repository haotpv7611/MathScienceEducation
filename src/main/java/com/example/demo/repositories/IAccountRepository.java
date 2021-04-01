package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Account;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {
	
	//find Account by username and isDisable = false
	//return Account
	Account findByUsernameAndIsDisable(String username, boolean isDisable);
	Account findByIdAndIsDisable(long id, boolean isDisable);
//	boolean loginForTest(String username, String password);

}
