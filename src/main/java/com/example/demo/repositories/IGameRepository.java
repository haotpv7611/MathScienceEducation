package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Game;

@Repository
public interface IGameRepository extends JpaRepository<Game, Long> {
	List<Game> findByLessonIdAndIsDisableFalse(long lessonId);

	Game findByIdAndIsDisableFalse(long id);

	Game findByLessonIdAndGameNameAndIsDisableFalse(long lessonId, String gameName);
}
