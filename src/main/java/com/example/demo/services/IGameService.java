package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.GameRequestDTO;
import com.example.demo.dtos.GameResponseDTO;

public interface IGameService {
	List<GameResponseDTO> findAllByLessonId(long lessonId);

	GameResponseDTO findGameById(long id);

	String updateGame(long id, GameRequestDTO gameRequestDTO);

	String createGame(GameRequestDTO gameRequestDTO);
	
	String deleteGame(List<Long> ids);
	
	void deleteOneGame(long id);
}
