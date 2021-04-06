package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.QuestionType;

@Repository
public interface IQuestionTypeRepository extends JpaRepository<QuestionType, Integer> {

}
