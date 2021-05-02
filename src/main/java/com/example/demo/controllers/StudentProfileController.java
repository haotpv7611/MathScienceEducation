package com.example.demo.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.dtos.StudentRequestDTO;
import com.example.demo.dtos.StudentResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.services.IStudentProfileService;

@CrossOrigin(exposedHeaders = "*", maxAge = 10*60*60*1000)
@RestController
public class StudentProfileController {
	@Autowired
	private IStudentProfileService iStudentProfileService;

	@GetMapping("/student/{id}")
	// @PreAuthorize("hasRole('admin')")
	public ResponseEntity<Object> findStudentById(@PathVariable long id) {
		Object response = iStudentProfileService.findStudentById(id);
		if (response.equals("NOT FOUND!")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.equals("FIND FAIL!")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/student/account/{accountId}")
	// @PreAuthorize("hasRole('student')")
	public ResponseEntity<StudentResponseDTO> findStudentByAccountId(@PathVariable long accountId) {
		StudentResponseDTO response = iStudentProfileService.findStudentByAccountId(accountId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/student/all")
	// @PreAuthorize("hasRole('admin')")
	public ResponseEntity<List<StudentResponseDTO>> findStudentByListId(@RequestBody List<Long> ids) {
		if (ids.size() != 3) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		List<StudentResponseDTO> response = iStudentProfileService.findStudentByListId(ids);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/student")
	// @PreAuthorize("hasRole('admin')")
	public ResponseEntity<String> createStudent(@Valid @RequestBody StudentRequestDTO studentRequestDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		String error = "";
		String fullName = studentRequestDTO.getFullName().trim().replaceAll("\\s+", " ");
		if (!fullName.matches("^[\\p{L} .'-]+$")) {
			error += "\nFullName is invalid!";
		}
		String parentName = studentRequestDTO.getParentName().trim().replaceAll("\\s+", " ");
		if (!parentName.matches("^[\\p{L} .'-]+$")) {
			error += "\nParentName is invalid!";
		}
		if (!error.isEmpty()) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}

		String response = iStudentProfileService.createStudenProfile(studentRequestDTO);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (response.contains("EXCEED") || response.contains("EXISTED")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/student/{id}")
	// @PreAuthorize("hasRole('admin')")
	public ResponseEntity<String> updateStudent(@PathVariable long id,
			@Valid @RequestBody StudentRequestDTO studentRequestDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		String error = "";
		String fullName = studentRequestDTO.getFullName().trim().replaceAll("\\s+", " ");
		if (!fullName.matches("^[\\p{L} .'-]+$")) {
			error += "\nFullName is invalid!";
		}
		String parentName = studentRequestDTO.getParentName().trim().replaceAll("\\s+", " ");
		if (!parentName.matches("^[\\p{L} .'-]+$")) {
			error += "\nParentName is invalid!";
		}
		if (!error.isEmpty()) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}

		String response = iStudentProfileService.updateStudent(id, studentRequestDTO);
		if (response.contains("NOT FOUND")) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if (response.contains("FAIL")) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (response.contains("EXISTED")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PutMapping("/student")
	// @PreAuthorize("hasRole('admin')")
	public ResponseEntity<String> changeStatusStudent(@Valid @RequestBody ListIdAndStatusDTO idAndStatusDTOList,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		String status = idAndStatusDTOList.getStatus();
		if (!status.equals("ACTIVE") && !status.equals("INACTIVE") && !status.equals("DELETED")
				&& !status.equals("PENDING")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("STATUS INVALID!");
		}
		try {
			String response = iStudentProfileService.changeStatusStudent(idAndStatusDTOList);
			if (response.contains("CANNOT") || response.contains("EXISTED")) {

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			if (e instanceof ResourceNotFoundException) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND!");
			}

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAIL!");
		}
	}

	@PutMapping("/student/changeClass")
	// @PreAuthorize("hasRole('admin')")
	public ResponseEntity<?> changeClassForStudent(@RequestParam List<Long> studentIdList,
			@RequestParam long classesId) {
		String error = "";
		if (studentIdList == null) {
			error += "\nStudentIds INVALID";
		} else {
			if (studentIdList.isEmpty()) {
				error += "\nStudentIds INVALID";
			}
		}

		if (!error.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}

		try {
			Map<String, List<Long>> response = iStudentProfileService.changeClassForStudent(studentIdList, classesId);
			for (Entry<String, List<Long>> entry : response.entrySet()) {
				if (entry.getKey().contains("EXISTED")) {

					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
				}
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			if (e instanceof ResourceNotFoundException) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND!");
			}

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAIL!");
		}

	}

	@PostMapping("/student/validate")
	// @PreAuthorize("hasRole('admin')")
	public void validateStudent(HttpServletResponse httpServletResponse, @RequestParam MultipartFile file,
			@RequestParam long schoolId, @RequestParam int gradeId) throws IOException, ParseException {
		Map<String, Workbook> response = iStudentProfileService.validateStudentFile(file, schoolId, gradeId);
		for (Entry<String, Workbook> entry : response.entrySet()) {
			if (entry.getKey().contains("FAIL")) {

				httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} else if (entry.getKey().contains("NOT FOUND")) {

				httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else if (entry.getKey().contains("OK")) {

				httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			} else if (entry.getKey().contains("EXCEED")) {

				httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				httpServletResponse.setContentType("application/octet-stream");
				String headerKey = "Content-Disposition";
				String headerValue = "attachment; filename="
						+ iStudentProfileService.generateFileNameExport(schoolId, gradeId, 0) + "Validate.xlsx";
				httpServletResponse.setHeader(headerKey, headerValue);

				iStudentProfileService.writeFileOS(httpServletResponse, entry.getValue());
			}
		}
	}

	@PostMapping("/student/import")
	// @PreAuthorize("hasRole('admin')")
//	@Timed
	@Transactional(timeout = 12000)
	public void importStudent(HttpServletResponse httpServletResponse, @RequestParam MultipartFile file,
			@RequestParam long schoolId, @RequestParam int gradeId) throws IOException {
		Map<String, Workbook> response = iStudentProfileService.importStudent(file, schoolId, gradeId);
		System.out.println("map size" + response.size());
		for (Entry<String, Workbook> entry : response.entrySet()) {
//			if (entry.getKey().contains("FAIL")) {
//
//				
//			} else 
			if (entry.getKey().contains("NOT FOUND")) {

				httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else if (entry.getKey().contains("SUCCESS")) {

				httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
			} else if (entry.getKey().contains("EXISTED")) {

				httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else if (entry.getKey().contains("ERROR")) {
				httpServletResponse.setContentType("application/octet-stream");
				String headerKey = "Content-Disposition";
				String headerValue = "attachment; filename="
						+ iStudentProfileService.generateFileNameExport(schoolId, gradeId, 0) + "ImportError.xlsx";
				httpServletResponse.setHeader(headerKey, headerValue);

				iStudentProfileService.writeFileOS(httpServletResponse, entry.getValue());
			} else {
				httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}

		}
	}

	@GetMapping("/student/export/account")
	// @PreAuthorize("hasRole('admin')")
	public void exportStudentAccount(HttpServletResponse httpServletResponse, @RequestParam long schoolId,
			@RequestParam int gradeId) throws IOException {
		Map<String, Workbook> response = iStudentProfileService.exportStudentAccount(schoolId, gradeId);
		for (Entry<String, Workbook> entry : response.entrySet()) {
			if (entry.getKey().contains("FAIL")) {

				httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} else if (entry.getKey().contains("NOT FOUND")) {

				httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				httpServletResponse.setContentType("application/octet-stream");
				String headerKey = "Content-Disposition";
				String headerValue = "attachment; filename="
						+ iStudentProfileService.generateFileNameExport(schoolId, gradeId, 0) + "Student Account.xlsx";
				httpServletResponse.setHeader(headerKey, headerValue);

				iStudentProfileService.writeFileOS(httpServletResponse, entry.getValue());
			}
		}
	}

	@GetMapping("/student/export")
	// @PreAuthorize("hasRole('admin')")
	public void exportScore(HttpServletResponse httpServletResponse, @RequestParam long schoolId,
			@RequestParam int gradeId, @RequestParam long subjectId) throws IOException {
		Map<String, Workbook> response = iStudentProfileService.exportScoreBySubjectId(schoolId, gradeId, subjectId);
		for (Entry<String, Workbook> entry : response.entrySet()) {
			if (entry.getKey().contains("FAIL")) {

				httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} else if (entry.getKey().contains("NOT FOUND")) {

				httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				httpServletResponse.setContentType("application/octet-stream");
				String headerKey = "Content-Disposition";
				String headerValue = "attachment; filename="
						+ iStudentProfileService.generateFileNameExport(schoolId, gradeId, subjectId) + "Score.xlsx";
				httpServletResponse.setHeader(headerKey, headerValue);

				iStudentProfileService.writeFileOS(httpServletResponse, entry.getValue());
			}
		}
	}

	@GetMapping("/student/export/scoreFinal")
	// @PreAuthorize("hasRole('admin')")
	public void exportFinalScore(HttpServletResponse httpServletResponse, @RequestParam long schoolId,
			@RequestParam int gradeId) throws IOException {
		Map<String, Workbook> response = iStudentProfileService.exportFinalScore(schoolId, gradeId);
		for (Entry<String, Workbook> entry : response.entrySet()) {
			if (entry.getKey().contains("FAIL")) {

				httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} else if (entry.getKey().contains("NOT FOUND")) {

				httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				httpServletResponse.setContentType("application/octet-stream");
				String headerKey = "Content-Disposition";
				String headerValue = "attachment; filename="
						+ iStudentProfileService.generateFileNameExport(schoolId, gradeId, 0) + "Graduate.xlsx";
				httpServletResponse.setHeader(headerKey, headerValue);

				iStudentProfileService.writeFileOS(httpServletResponse, entry.getValue());
			}
		}
	}

}
