package com.example.demo.services;

import java.util.List;
import java.util.Map;

import com.example.demo.dtos.GameRequestDTO;
import com.example.demo.dtos.GameResponseDTO;
import com.example.demo.dtos.IdAndStatusDTO;

public interface IGameService {

	Object findGameById(long id);

	List<GameResponseDTO> findAllByLessonId(long lessonId);
	
	List<GameResponseDTO> findAllByLessonIdStudentView(long lessonId);
	
	Map<Long, Integer> findAllGame();

	String updateGame(long id, GameRequestDTO gameRequestDTO);

	String createGame(GameRequestDTO gameRequestDTO);

	String changeGameStatus(IdAndStatusDTO idAndStatusDTO);

	void changeStatusOne(IdAndStatusDTO idAndStatusDTO);
}
