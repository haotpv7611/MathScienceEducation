package com.example.demo.services.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.StudentRecord;
import com.example.demo.repositories.IStudentRecordRepository;
import com.example.demo.services.IStudentRecordService;

@Service
public class StudentRecordServiceIpml implements IStudentRecordService {
	
	@Autowired
	private IStudentRecordRepository iStudentRecordRepository;

	@Override
	public float calculateScore(long exerciseId, long accountId, float score) {
//		StudentRecord studentRecord = iStudentRecordRepository.findByExerciseIdAndAccountId(exerciseId, accountId);
//		if (studentRecord == null) {
//			studentRecord.setExerciseId(exerciseId);
//			studentRecord.setAccountId(accountId);
//			if ()
//		}
		return 0;
	}

}
