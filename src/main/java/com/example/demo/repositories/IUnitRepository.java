package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Unit;

@Repository
public interface IUnitRepository extends JpaRepository<Unit, Long> {

	List<Unit> findBySubjectIdAndIsDisableFalseOrderByUnitNameAsc(long subjectId);
	
	List<Unit> findBySubjectIdAndIsDisableFalse(long subjectId);

	Unit findByIdAndIsDisableFalse(long id);

	Unit findBySubjectIdAndUnitNameAndIsDisableFalse(long subjectId, int unitName);
}
