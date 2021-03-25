package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.NewsRequestDTO;
import com.example.demo.dtos.NewsResponseDTO;

public interface INewsService {
	List<NewsResponseDTO> findAllNewsOrderByCreatedDateDesc(boolean isStudent);
	List<NewsResponseDTO> findThreeNewsOrderByCreatedDateDesc();
	NewsResponseDTO findNewsById(long id);
	String createNews(NewsRequestDTO newsRequestDTO);
//	String updateNews(NewsRequestDTO newsRequestDTO);
	String deleteNews(long id);
}
