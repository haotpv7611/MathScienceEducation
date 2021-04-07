package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Question;

@Repository
public interface IQuestionRepository extends JpaRepository<Question, Long> {

	List<Question> findByIsDisableFalseAndIdIn(List<Long> questionIdList);

	List<Question> findByUnitIdAndIsDisableFalse(long unitId);

	List<Question> findByUnitIdAndQuestionTypeIdAndIsDisableFalse(long unitId, int questionTypeId);

	List<Question> findByUnitIdAndQuestionTypeIdNotAndIsDisableFalse(long unitId, int questionTypeId);

	Question findByIdAndIsDisableFalse(long id);

}
