package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.dtos.SchoolResponseDTO;
import com.example.demo.models.SchoolGrade;

@Repository
public interface ISchoolGradeRepository extends JpaRepository<SchoolGrade, Long> {

	List<SchoolResponseDTO> findSchoolLinkedByGradeId(int gradeId);

	List<SchoolGrade> findByGradeIdAndStatusNotOrderByStatusAsc(int gradeId, String status);

	SchoolGrade findByGradeIdAndSchoolIdAndStatusNot(int gradeId, long schoolId, String status);
	
	List<SchoolGrade> findBySchoolIdAndStatusNot(long schoolId, String status);
}
