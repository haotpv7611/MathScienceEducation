package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.School;

@Repository
public interface ISchoolRepository extends JpaRepository<School, Long> {
	List<School> findByStatus(String status);

	Long countBySchoolCode(String schoolCode);

	School findByIdAndStatusNot(long id, String status);

	List<School> findBySchoolNameAndSchoolDistrictAndSchoolLevelId(String schoolName, String District, int schoolLevelId);
	
	List<School> findByStatusNot(String status);
	
	
	School findFirstBySchoolCodeOrderBySchoolCountDesc(String schoolCode);
}
