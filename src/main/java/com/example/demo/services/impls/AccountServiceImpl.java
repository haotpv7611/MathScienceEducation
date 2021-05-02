package com.example.demo.services.impls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
			account.setPassword(ADMIN_PASSWORD);
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
	public Object getUserCredential(String token) {
		AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
		try {

			String username = jwtProvider.getUserNameFromJwtToken(token);
			Account account = iAccountRepository.findByUsernameAndStatus(username, ACTIVE_STATUS);

			if (account == null) {
				throw new ResourceNotFoundException();
			}
			accountResponseDTO.setAccountId(account.getId());
			String role = account.getRole().getDescription();
			accountResponseDTO.setDescription(role);
			if (role.equals("student")) {
				accountResponseDTO
						.setGradeId(account.getStudentProfile().getClasses().getSchoolGrade().getGrade().getId());
			} else {
				accountResponseDTO.setGradeId(0);
			}

		} catch (Exception e) {
			logger.error("Get credential! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "GET CREDENTIAL FAIL!";
		}
		
		return accountResponseDTO;
	}

}
