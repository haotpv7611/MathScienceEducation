package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.OptionQuestion;

@Repository
public interface IOptionQuestionRepository extends JpaRepository<OptionQuestion, Long> {
	
	List<OptionQuestion> findByQuestionIdAndIsDisable(long questionId, boolean isDisable);
}
