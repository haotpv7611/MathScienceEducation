package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.NewsRequestDTO;
import com.example.demo.dtos.NewsResponseDTO;

public interface INewsService {
	String createNews(NewsRequestDTO newsRequestDTO);

	List<NewsResponseDTO> findAllNewsOrderByCreatedDateDesc(boolean isStudent);

	NewsResponseDTO findNewsById(long id);

	String deleteNews(List<Long> ids);

	List<NewsResponseDTO> findThreeNewsOrderByCreatedDateDesc();

}
