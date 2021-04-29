package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.StudentProfile;

@Repository
public interface IStudentProfileRepository extends JpaRepository<StudentProfile, Long> {

	StudentProfile findByIdAndStatusNot(long accountId, String status);

	List<StudentProfile> findByClassesIdAndStatusNot(long classId, String status);
	
	List<StudentProfile> findByClassesIdAndStatus(long classId, String status);

	StudentProfile findFirstByClassesIdAndStatusContainingOrderByStudentCountDesc(long classId, String status);

}
