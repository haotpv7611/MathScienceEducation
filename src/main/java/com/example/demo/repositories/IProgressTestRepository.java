package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.ProgressTest;

@Repository
public interface IProgressTestRepository extends JpaRepository<ProgressTest, Long> {	
	List<ProgressTest> findBySubjectIdAndIsDisable(long subjectId, boolean isDisable);

	List<ProgressTest> findBySubjectId(long subjectId);
	
	ProgressTest findByIdAndIsDisable(long id, boolean isDisable);
}
