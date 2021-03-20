package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.NewsDTO;

public interface INewsService {
	List<NewsDTO> findAllNewsOrderByCreatedDateDesc(boolean isStudent);
	List<NewsDTO> findThreeNewsOrderByCreatedDateDesc();
	NewsDTO findNewsById(long id);
	String createNews(NewsDTO newsDTO);
	String updateNews(NewsDTO newsDTO);
	void deleteNews(long id);
}
