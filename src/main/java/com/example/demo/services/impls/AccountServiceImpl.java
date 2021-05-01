package com.example.demo.services.impls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.AccountRequestDTO;
import com.example.demo.dtos.AccountResponseDTO;
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
//	private final int ADMIN_ID = 1;
	private final String ACTIVE_STATUS = "ACTIVE";
	private final String DELETE_STATUS = "DELETED";
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

	@Override
	public String createAccount(String username, String password, int roleId) {
		Account checkUsername = iAccountRepository.findByUsernameAndStatusNot(username, "DELETED");
		if (checkUsername != null) {

			return "EXISTED";
		}

		Role role = iRoleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException());
		Account account = new Account();
		account.setUsername(username);
		account.setPassword(ADMIN_PASSWORD);
		account.setFullName(username);
		account.setRole(role);
		account.setStatus("ACTIVE");
		iAccountRepository.save(account);
		return null;
	}

	@Override
	public String resetPassword(long studentId) {
		try {
			StudentProfile studentProfile = iStudentProfileRepository.findByIdAndStatusNot(studentId, DELETE_STATUS);
			if (studentProfile == null) {
				throw new ResourceNotFoundException();
			}
			Account account = studentProfile.getAccount();
			account.setPassword(DEFAULT_PASSWORD);
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
	public String login(AccountRequestDTO accountRequestDTO) {
		String username = accountRequestDTO.getUsername();
		String password = accountRequestDTO.getPassword();

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtProvider.generateJwtToken(authentication);

		return jwt;
	}

	public AccountResponseDTO getUserCredential(String token) {

		String username = jwtProvider.getUserNameFromJwtToken(token);
		Account account = iAccountRepository.findByUsernameAndStatus(username, ACTIVE_STATUS);
		AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
		if (account != null) {
			accountResponseDTO.setAccountId(account.getId());
			String role = account.getRole().getDescription();
			accountResponseDTO.setDescription(role);
			if (role.equals("student")) {
				accountResponseDTO
						.setGradeId(account.getStudentProfile().getClasses().getSchoolGrade().getGrade().getId());
			} else {
				accountResponseDTO.setGradeId(0);
			}
		}
		return accountResponseDTO;
	}

}
