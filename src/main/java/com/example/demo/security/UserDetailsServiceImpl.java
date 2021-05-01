package com.example.demo.security;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.models.Account;
import com.example.demo.repositories.IAccountRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	private final String ACTIVE_STATUS = "ACTIVE";

	@Autowired
	private IAccountRepository iAccountRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Account account = iAccountRepository.findByUsernameAndStatus(username, ACTIVE_STATUS);
		if (account == null) {
			throw new UsernameNotFoundException("User Not Found with -> username: " + username);
		}

		return UserPrinciple.build(account);
	}
}
