package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.ProgressTest;

@Repository
public interface IProgressTestRepository extends JpaRepository<ProgressTest, Long> {

	ProgressTest findByIdAndIsDisableFalse(long id);

	ProgressTest findByUnitAfterIdAndIsDisableFalse(long unitAfterId);

	ProgressTest findBySubjectIdAndProgressTestNameIgnoreCaseAndIsDisableFalse(long subjectId, String progressTestName);

	List<ProgressTest> findBySubjectIdAndIsDisableFalse(long subjectId);

	List<ProgressTest> findBySubjectIdAndIsDisableFalseOrderByProgressTestName(long subjectId);
}
