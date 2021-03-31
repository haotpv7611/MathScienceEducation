package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.SchoolGrade;

@Repository
public interface ISchoolGradeRepository extends JpaRepository<SchoolGrade, Long> {
	List<SchoolGrade> findByGradeIdAndStatusNot(long gradeId, String status);

	SchoolGrade findByGradeIdAndSchoolIdAndStatusNot(long gradeId, long schoolId, String status);
}
