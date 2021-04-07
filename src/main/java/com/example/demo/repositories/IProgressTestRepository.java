package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.ProgressTest;

@Repository
public interface IProgressTestRepository extends JpaRepository<ProgressTest, Long> {
	List<ProgressTest> findBySubjectIdAndIsDisableFalse(long subjectId);

//	List<ProgressTest> findBySubjectId(long subjectId);

	ProgressTest findByIdAndIsDisableFalse(long id);
	
	ProgressTest findBySubjectIdAndProgressTestNameAndIsDisableFalse(long subjectId, String progressTestName);
}
