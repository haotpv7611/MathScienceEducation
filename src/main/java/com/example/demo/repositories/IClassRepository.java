package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Classes;

@Repository
public interface IClassRepository extends JpaRepository<Classes, Long> {
	List<Classes> findBySchoolGradeIdAndStatusNotOrderByStatusAscClassNameAsc(long schoolGradeId, String status);
	
	Classes findByIdAndStatusNot(long id, String status);
	
	Classes findBySchoolGradeIdAndClassName(long schoolGradeId, String className);
	
	List<Classes> findBySchoolGradeIdAndStatusNot(long schoolGradeId, String status);
}
