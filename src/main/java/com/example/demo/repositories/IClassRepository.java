package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Class;

@Repository
public interface IClassRepository extends JpaRepository<Class, Long> {
	List<Class> findBySchoolGradeIdAndIsDisable(long schooGradeId, boolean isDisable);
}
