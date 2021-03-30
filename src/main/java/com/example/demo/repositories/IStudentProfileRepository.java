package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.StudentProfile;

@Repository
public interface IStudentProfileRepository extends JpaRepository<StudentProfile, Long> {

}
