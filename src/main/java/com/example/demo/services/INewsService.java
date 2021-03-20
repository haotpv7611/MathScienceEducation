package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.NewsDTO;

public interface INewsService {
	List<NewsDTO> findAllNewsOrderByCreatedDateDesc(boolean isStudent);
	List<NewsDTO> findThreeNewsOrderByCreatedDateDesc();
	NewsDTO findNewsById(long id);
	String createNews(String newsTitle, String shortDescription, String newsContent, long accountId);
	String updateNews(long id, String newsTitle, String shortDescription, String newsContent);
	void deleteNews(long id);
}
