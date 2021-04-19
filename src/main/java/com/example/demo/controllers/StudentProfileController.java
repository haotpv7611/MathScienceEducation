package com.example.demo.controllers;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
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
import com.example.demo.services.IStudentProfileService;

@CrossOrigin
@RestController
public class StudentProfileController {
	@Autowired
	IStudentProfileService iStudentProfileService;

	@GetMapping("/student/{id}")
	public ResponseEntity<StudentResponseDTO> findStudentById(@PathVariable long id) {
		StudentResponseDTO response = iStudentProfileService.findStudentById(id);

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
	
	@PostMapping("/student/validate")
	public ResponseEntity<String> validateStudent(@RequestParam MultipartFile file, @RequestParam long schoolId, @RequestParam int gradeId) throws IOException{
		iStudentProfileService.validateStudentFile(file, schoolId, gradeId);
		return ResponseEntity.status(HttpStatus.OK).body("OK");
	}
	
	

	@PostMapping("/student/import")
	public ResponseEntity<String> importStudent(@RequestParam MultipartFile file, @RequestParam long schoolId, @RequestParam int gradeId) throws IOException{
		String response = iStudentProfileService.importStudent(file, schoolId, gradeId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PostMapping("/student/export")
	public void exportScore(HttpServletResponse response, @RequestParam long schoolId, @RequestParam int gradeId, @RequestParam long subjectId) throws IOException{
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + iStudentProfileService.generateFileNameExport(schoolId, gradeId, subjectId);
        response.setHeader(headerKey, headerValue);
        iStudentProfileService.exportScore(schoolId, gradeId, subjectId, response);
        
        
//		Map<String, ByteArrayInputStream> map = iStudentProfileService.exportScore(schoolId, gradeId, subjectId);
//		String fileName = "";
//		ByteArrayInputStream export = null;
//		for (Entry<String, ByteArrayInputStream> entry : map.entrySet()) {
//			fileName = "attachment; filename=" + entry.getKey();
//			export = entry.getValue();
//		}
//		
//        headers.add("Content-Disposition", fileName);
		
		
//		return ResponseEntity.ok()
//				.headers(headers).body(new InputStreamResource(export));
	}
	
	
	

	@PutMapping("/student")
	public ResponseEntity<String> changeStatusClass(@Valid @RequestBody ListIdAndStatusDTO idAndStatusDTOList,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String error = "";
			for (ObjectError object : bindingResult.getAllErrors()) {
				error += "\n" + object.getDefaultMessage();
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.trim());
		}
		String status = idAndStatusDTOList.getStatus();
		if (!status.equals("ACTIVE") && !status.equals("INACTIVE") && !status.equals("DELETED")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("STATUS INVALID!");
		}
		String response = iStudentProfileService.changeStatusStudent(idAndStatusDTOList);

		return ResponseEntity.ok(response);
	}
}
