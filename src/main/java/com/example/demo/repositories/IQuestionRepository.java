package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Question;

@Repository
public interface IQuestionRepository extends JpaRepository<Question, Long> {
	@Query(value = "FROM Question WHERE isDisable = false AND id in ?1")
	List<Question> findAllQuestionByListIdAndIsDisable(List<Long> questionIdList);
	
	List<Question> findByUnitIdAndIsDisable(long unitId, boolean isDisable);

	List<Question> findByUnitIdAndQuestionTypeIdAndIsDisable(long unitId, int questionTypeId, boolean isDisable);

	List<Question> findByUnitIdAndQuestionTypeIdNotAndIsDisable(long unitId, int questionTypeId, boolean isDisable);

}
