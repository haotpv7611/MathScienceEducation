package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Subject;

@Repository
public interface ISubjectRepository extends JpaRepository<Subject, Long> {

	List<Subject> findByIsDisableFalse();

	List<Subject> findByGradeIdAndIsDisableFalse(long gradeId);

	Subject findByIdAndIsDisableFalse(long id);

	Subject findByGradeIdAndSubjectNameIgnoreCaseAndIsDisableFalse(long gradeId, String subjectName);

}
