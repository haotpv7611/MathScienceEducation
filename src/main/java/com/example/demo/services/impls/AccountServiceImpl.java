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

import com.example.demo.dtos.AccountRequestDTO;
import com.example.demo.dtos.AccountResponseDTO;
import com.example.demo.dtos.CredentialDTO;
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
	private final String ADMIN_PASSWORD = "12345678";
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
	public String login(AccountRequestDTO accountRequestDTO) {
		String username = accountRequestDTO.getUsername();
		String password = accountRequestDTO.getPassword();

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

	public Object findAccountById(long id) {
		AccountResponseDTO accountResponseDTOList = new AccountResponseDTO();
		try {
			Account account = iAccountRepository.findByIdAndStatusNot(id, DELETE_STATUS);
			if (account != null) {
				AccountResponseDTO accountResponseDTO = modelMapper.map(account, AccountResponseDTO.class);
				accountResponseDTO.setRole(account.getRole().getDescription());
			}

		} catch (Exception e) {
			logger.error("Find all staff account! " + e.getMessage());

			return null;
		}

		return accountResponseDTOList;
	}

	public List<AccountResponseDTO> findAllAccount() {
		List<AccountResponseDTO> accountResponseDTOList = new ArrayList<>();
		try {
			List<Account> accountList = iAccountRepository.findByRoldId(ROLE_STAFF);
			if (!accountList.isEmpty()) {
				for (Account account : accountList) {
					AccountResponseDTO accountResponseDTO = modelMapper.map(account, AccountResponseDTO.class);
					accountResponseDTO.setRole(account.getRole().getDescription());
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
	public String createAccount(String username, String password, int roleId) {
		try {
			Account checkUsername = iAccountRepository.findByUsernameAndStatusNot(username, "DELETED");
			if (checkUsername != null) {

				return "EXISTED";
			}

			Role role = iRoleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException());
			Account account = new Account();
			account.setUsername(username);
			account.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
			account.setFullName(username);
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

//	@Override
	public String changeStatusAccount(ListIdAndStatusDTO listIdAndStatusDTO) {
		try {
			List<Long> ids = listIdAndStatusDTO.getIds();
			String status = listIdAndStatusDTO.getStatus();
			for (long id : ids) {
				Account account = iAccountRepository.findByIdAndStatusNot(id, "DELETED");
				if (account == null) {
					throw new ResourceNotFoundException();
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

		return "CREATE SUCCESS!";
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
		CredentialDTO credentialDTO = new CredentialDTO();
		try {

			String username = jwtProvider.getUserNameFromJwtToken(token);
			Account account = iAccountRepository.findByUsernameAndStatus(username, ACTIVE_STATUS);

			if (account == null) {
				throw new ResourceNotFoundException();
			}
			credentialDTO.setAccountId(account.getId());
			String role = account.getRole().getDescription();
			credentialDTO.setDescription(role);
			if (role.equals("student")) {
				credentialDTO.setGradeId(account.getStudentProfile().getClasses().getSchoolGrade().getGrade().getId());
			} else {
				credentialDTO.setGradeId(0);
			}

		} catch (Exception e) {
			logger.error("Get credential! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "GET CREDENTIAL FAIL!";
		}

		return credentialDTO;
	}

}
