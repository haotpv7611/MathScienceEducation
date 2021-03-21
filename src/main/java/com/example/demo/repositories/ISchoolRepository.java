package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.School;

@Repository
public interface ISchoolRepository extends JpaRepository<School, Long> {
	List<School> findByIsDisable(boolean isDisable);
	Long countBySchoolCode(String schoolCode);
}
