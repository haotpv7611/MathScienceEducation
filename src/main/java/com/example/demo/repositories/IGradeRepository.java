package com.example.demo.repositories;

<<<<<<< Updated upstream
=======
import java.util.List;

>>>>>>> Stashed changes
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Grade;

@Repository
public interface IGradeRepository extends JpaRepository<Grade, Long> {
<<<<<<< Updated upstream
<<<<<<<< Updated upstream:src/main/java/com/example/demo/repositories/IGradeRepository.java
	
========

	List<Grade> findByIsDisable(boolean isDisable);
>>>>>>>> Stashed changes:src/main/java/com/example/demo/repositories/iGradeRepository.java
=======

	List<Grade> findByIsDisable(boolean isDisable);
>>>>>>> Stashed changes
}
