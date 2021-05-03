package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Game;

@Repository
public interface IGameRepository extends JpaRepository<Game, Long> {

	List<Game> findByStatusNot(String status);

	List<Game> findByLessonIdAndStatusNot(long lessonId, String status);

	List<Game> findByLessonIdAndStatus(long lessonId, String status);

	Game findByIdAndStatusNot(long id, String status);
	
	Game findByIdAndStatus(long id, String status);

	Game findByLessonIdAndGameNameAndStatusNot(long lessonId, int gameName, String status);
}
