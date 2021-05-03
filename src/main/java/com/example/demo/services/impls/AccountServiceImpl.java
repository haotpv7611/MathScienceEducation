package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.AccountCredDTO;
import com.example.demo.dtos.AccountRequestDTO;
import com.example.demo.dtos.AccountResponseDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Account;
import com.example.demo.models.Role;
import com.example.demo.models.StudentProfile;
import com.example.demo.repositories.IAccountRepository;
import com.example.demo.repositories.IRoleRepository;
import com.example.demo.repositories.IStudentProfileRepository;
import com.example.demo.security.JwtProvider;
import com.example.demo.services.IAccountService;

@Service
public class AccountServiceImpl implements IAccountService {
	Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	private final String ACTIVE_STATUS = "ACTIVE";
	private final String DELETE_STATUS = "DELETED";
	private final int ROLE_STAFF = 2;
	private final String STAFF_PASSWORD = "12345678";
	private final String DEFAULT_PASSWORD = "123456";

	@Autowired
	private IAccountRepository iAccountRepository;

	@Autowired
	private IStudentProfileRepository iStudentProfileRepository;

	@Autowired
	private IRoleRepository iRoleRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public String login(String username, String password) {

		String jwt = "";
		try {

			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			SecurityContextHolder.getContext().setAuthentication(authentication);

			jwt = jwtProvider.generateJwtToken(authentication);

		} catch (Exception e) {
			logger.error("Login! " + e.getMessage());

			return "LOGIN FAIL";
		}

		return jwt;
	}

	@Override
	public Object findAccountById(long id) {
		AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
		try {
			Account account = iAccountRepository.findByIdAndStatusNot(id, DELETE_STATUS);
			if (account == null) {
				throw new ResourceNotFoundException();
			}
			accountResponseDTO = modelMapper.map(account, AccountResponseDTO.class);
			accountResponseDTO.setPassword(null);
			accountResponseDTO.setRole(account.getRole().getDescription());

		} catch (Exception e) {
			logger.error("Find all staff account! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return null;
		}

		return accountResponseDTO;
	}

	@Override
	public List<AccountResponseDTO> findAllStaffAccount() {
		List<AccountResponseDTO> accountResponseDTOList = new ArrayList<>();
		try {
			List<Account> accountList = iAccountRepository.findByRoleIdAndStatusNot(ROLE_STAFF, DELETE_STATUS);
			if (!accountList.isEmpty()) {
				for (Account account : accountList) {
					AccountResponseDTO accountResponseDTO = modelMapper.map(account, AccountResponseDTO.class);
					accountResponseDTO.setRole(account.getRole().getDescription());
					accountResponseDTO.setPassword(null);
					accountResponseDTOList.add(accountResponseDTO);
				}
			}

		} catch (Exception e) {
			logger.error("Find all staff account! " + e.getMessage());

			return null;
		}

		return accountResponseDTOList;
	}

	@Override
	public String createAccount(AccountRequestDTO accountRequestDTO) {
		try {
			String username = accountRequestDTO.getUsername();
			String fullname = accountRequestDTO.getFullName();
			Account account = iAccountRepository.findByUsernameAndStatusNot(username, "DELETED");
			if (account != null) {

				return "EXISTED";
			}
			Role role = iRoleRepository.findById(ROLE_STAFF).orElseThrow(() -> new ResourceNotFoundException());

			account = new Account();
			account.setUsername(username);
			account.setPassword(passwordEncoder.encode(STAFF_PASSWORD));
			account.setFullName(fullname);
			account.setRole(role);
			account.setStatus("ACTIVE");
			iAccountRepository.save(account);
		} catch (Exception e) {
			logger.error("Create account! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CREATE FAIL";
		}

		return "CREATE SUCCESS!";
	}

	@Override
	public String updateAccount(long id, AccountRequestDTO accountRequestDTO) {
		try {
			String password = accountRequestDTO.getPassword();
			String fullName = accountRequestDTO.getFullName();
			Account account = iAccountRepository.findByIdAndStatusNot(id, "DELETED");
			if (account == null) {
				throw new ResourceNotFoundException();
			}
			if (password != null) {
				account.setPassword(passwordEncoder.encode(password));
			}
			account.setFullName(fullName);
			iAccountRepository.save(account);
		} catch (Exception e) {
			logger.error("Create account! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CREATE FAIL";
		}

		return "CREATE SUCCESS!";
	}

	@Override
	public String checkContact(String username, String contact) {
		try {
			Account account = iAccountRepository.findByUsernameAndStatus(username, ACTIVE_STATUS);
			if (account == null) {

				return "INVALID";
			}
			if (!account.getRole().getDescription().equals("student")) {

				return "INVALID";
			}

			StudentProfile studentProfile = account.getStudentProfile();
			if (!studentProfile.getContact().equals(contact)) {

				return "INVALID";
			}

		} catch (Exception e) {
			logger.error("Check contact username =  " + username + " and contact = " + contact + "! " + e.getMessage());

			return "CHANGE FAIL";
		}

		return "EXISTED";
	}

	@Override
	public String changeStudentPasswordByUsername(String username, String newPassword) {
		try {
			Account account = iAccountRepository.findByUsernameAndStatus(username, ACTIVE_STATUS);
			if (account == null) {

				return "INVALID";
			}
			if (!account.getRole().getDescription().equals("student")) {

				return "INVALID";
			}
			account.setPassword(passwordEncoder.encode(newPassword));
			iAccountRepository.save(account);

		} catch (Exception e) {
			logger.error("Change password username =  " + username + "! " + e.getMessage());

			return "CHANGE FAIL";
		}

		return "CHANGE SUCCESS";
	}

	@Override
	public String changeStudentPasswordByAccountId(long accountId, String oldPassword, String newPassword) {
		try {
			Account account = iAccountRepository.findByIdAndStatus(accountId, ACTIVE_STATUS);
			if (account == null) {

				return "INVALID";
			}

			if (!passwordEncoder.matches(oldPassword, account.getPassword())) {

				return "INVALID";
			}

			if (!account.getRole().getDescription().equals("student")) {

				return "INVALID";
			}

			account.setPassword(passwordEncoder.encode(newPassword));
			iAccountRepository.save(account);

		} catch (Exception e) {
			logger.error("Change password accountId =  " + accountId + "! " + e.getMessage());

			return "CHANGE FAIL";
		}

		return "CHANGE SUCCESS";
	}

	@Override
	public String changeStatusAccount(ListIdAndStatusDTO listIdAndStatusDTO) {
		try {
			List<Long> ids = listIdAndStatusDTO.getIds();
			String status = listIdAndStatusDTO.getStatus();
			for (long id : ids) {
				Account account = iAccountRepository.findByIdAndStatusNot(id, "DELETED");
				if (account == null) {
					throw new ResourceNotFoundException();
				}
				if (status.equalsIgnoreCase(DELETE_STATUS)) {
					if (account.getStatus().equalsIgnoreCase(ACTIVE_STATUS)) {

						return "CANNOT DELETE";
					}
				}

				account.setStatus(status);
				iAccountRepository.save(account);
			}
		} catch (Exception e) {
			logger.error("Change status account! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CHANGE FAIL";
		}

		return "CHANGE SUCCESS!";
	}

	@Override
	public String resetPassword(long studentId) {
		try {
			StudentProfile studentProfile = iStudentProfileRepository.findByIdAndStatusNot(studentId, DELETE_STATUS);
			if (studentProfile == null) {
				throw new ResourceNotFoundException();
			}
			Account account = studentProfile.getAccount();
			account.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
			iAccountRepository.save(account);

		} catch (Exception e) {
			logger.error("Reset pw for student with studentId = " + studentId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "RESET FAIL!";
		}

		return "RESET SUCCESS!";
	}

	@Override
	public Object getUserCredential(String token) {
		AccountCredDTO accountCredDTO = new AccountCredDTO();
		try {

			String username = jwtProvider.getUserNameFromJwtToken(token);
			Account account = iAccountRepository.findByUsernameAndStatus(username, ACTIVE_STATUS);

			if (account == null) {
				throw new ResourceNotFoundException();
			}
			accountCredDTO.setAccountId(account.getId());
			String role = account.getRole().getDescription();
			accountCredDTO.setDescription(role);
			if (role.equals("student")) {
				accountCredDTO.setGradeId(account.getStudentProfile().getClasses().getSchoolGrade().getGrade().getId());
			} else {
				accountCredDTO.setGradeId(0);
			}

		} catch (Exception e) {
			logger.error("Get credential! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "GET CREDENTIAL FAIL!";
		}

		return accountCredDTO;
	}

}
