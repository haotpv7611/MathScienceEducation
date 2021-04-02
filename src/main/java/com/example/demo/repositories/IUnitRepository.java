package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Unit;

@Repository
public interface IUnitRepository extends JpaRepository<Unit, Long> {

	List<Unit> findBySubjectIdAndIsDisableOrderByUnitNameAsc(Long subjectId, boolean isDisable);

	Unit findByIdAndIsDisable(long id, boolean isDisable);

	Unit findBySubjectIdAndUnitNameAndIsDisable(long subjectId, int unitName, boolean isDisable);
}
