package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Game;

@Repository
public interface IGameRepository extends JpaRepository<Game, Long> {
	List<Game> findByLessonIdAndStatusNot(long lessonId, String status);

	Game findByIdAndStatusNot(long id, String status);

	Game findByLessonIdAndGameNameAndStatusNot(long lessonId, int gameName, String status);
}
