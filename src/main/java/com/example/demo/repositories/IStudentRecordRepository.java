package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.StudentRecord;

@Repository
public interface IStudentRecordRepository extends JpaRepository<StudentRecord, Long> {

	StudentRecord findByUnitIdAndAccountId(long unitId, long accountId);

	StudentRecord findByProgressTestIdAndAccountId(long progressTestId, long accountId);
}
