package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.SchoolLevel;

@Repository
public interface ISchoolLevelRepository extends JpaRepository<SchoolLevel, Integer> {

}
