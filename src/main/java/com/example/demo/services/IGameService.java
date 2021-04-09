package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.GameRequestDTO;
import com.example.demo.dtos.GameResponseDTO;

public interface IGameService {

	Object findGameById(long id);

	List<GameResponseDTO> findAllByLessonId(long lessonId);

	String updateGame(long id, GameRequestDTO gameRequestDTO);

	String createGame(GameRequestDTO gameRequestDTO);

	String deleteGame(long id);

	void deleteOneGame(long id);
}
