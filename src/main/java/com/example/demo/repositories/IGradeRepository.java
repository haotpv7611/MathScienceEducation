package com.example.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Grade;

@Repository
public interface IGradeRepository extends JpaRepository<Grade, Long> {

}
