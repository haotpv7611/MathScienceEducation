package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.SchoolGrade;

@Repository
public interface ISchoolGradeRepository extends JpaRepository<SchoolGrade, Long> {
	List<SchoolGrade> findByGradeIdAndIsDisable(long gradeId, boolean isDisable);

	SchoolGrade findByGradeIdAndSchoolIdAndIsDisable(long gradeId, long schoolId, boolean isDisable);
}
