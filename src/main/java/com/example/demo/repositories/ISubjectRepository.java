package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Subject;

@Repository
public interface ISubjectRepository extends JpaRepository<Subject, Long> {

	Subject findByIdAndIsDisableFalse(long id);

	Subject findByGradeIdAndSubjectNameIgnoreCaseAndIsDisableFalse(int gradeId, String subjectName);

	List<Subject> findByGradeIdAndIsDisableFalse(int gradeId);

	List<Subject> findByIsDisableFalse();

}
