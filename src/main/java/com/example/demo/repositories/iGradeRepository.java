package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Grade;

@Repository
public interface iGradeRepository extends JpaRepository<Grade, Long> {

	List<Grade> findByIsDisable(boolean isDisable);
}
