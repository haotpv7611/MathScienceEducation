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

@CrossOrigin(exposedHeaders = "Content-Disposition")
@RestController
public class StudentProfileController {
	@Autowired
	IStudentProfileService iStudentProfileService;

	@GetMapping("/student/{id}")
	public ResponseEntity<StudentResponseDTO> findStudentById(@PathVariable long id) {
		StudentResponseDTO response = iStudentProfileService.findStudentById(id);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/student/account/{accountId}")
	public ResponseEntity<StudentResponseDTO> findStudentByAccountId(@PathVariable long accountId) {
		StudentResponseDTO response = iStudentProfileService.findStudentByAccountId(accountId);
		if (response == null) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/student/all")
	public ResponseEntity<List<StudentResponseDTO>> findStudentByListId(@RequestBody List<Long> ids) {
		if (ids.size() != 3) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		return ResponseEntity.ok(iStudentProfileService.findStudentByListId(ids));
	}

	@PostMapping("/student")
	public ResponseEntity<String> createStudent(@Valid @RequestBody StudentRequestDTO studentRequestDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(iStudentProfileService.createStudenProfile(studentRequestDTO));
	}

	@PutMapping("/student/{id}")
	public ResponseEntity<String> updateStudent(@PathVariable long id,
			@Valid @RequestBody StudentRequestDTO studentRequestDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		return ResponseEntity.status(HttpStatus.OK).body(iStudentProfileService.updateStudent(id, studentRequestDTO));
	}

	@PostMapping("/student/validate")
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
	public void importStudent(HttpServletResponse httpServletResponse, @RequestParam MultipartFile file,
			@RequestParam long schoolId, @RequestParam int gradeId) throws IOException {
		Map<String, Workbook> response = iStudentProfileService.importStudent(file, schoolId, gradeId);
		for (Entry<String, Workbook> entry : response.entrySet()) {
			if (entry.getKey().contains("FAIL")) {

				httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} else if (entry.getKey().contains("NOT FOUND")) {

				httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else if (entry.getKey().contains("SUCCESS")) {

				httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
			} else {
				httpServletResponse.setContentType("application/octet-stream");
				String headerKey = "Content-Disposition";
				String headerValue = "attachment; filename="
						+ iStudentProfileService.generateFileNameExport(schoolId, gradeId, 0) + "ImportError.xlsx";
				httpServletResponse.setHeader(headerKey, headerValue);

				iStudentProfileService.writeFileOS(httpServletResponse, entry.getValue());
			}
		}
	}

	@GetMapping("/student/export")
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

	@PutMapping("/student")
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
		String response = iStudentProfileService.changeStatusStudent(idAndStatusDTOList);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/student/changeClass")
	public ResponseEntity<String> changeClassForStudent(@RequestParam List<Long> studentIdList,
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
			String response = iStudentProfileService.changeClassForStudent(studentIdList, classesId);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			if (e instanceof ResourceNotFoundException) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND!");
			}

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAIL!");
		}

	}

}
