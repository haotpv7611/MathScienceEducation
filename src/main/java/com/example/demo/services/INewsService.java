package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.NewsDTO;

public interface INewsService {
	List<NewsDTO> findAllNewsOrderByCreatedDateDesc();
	List<NewsDTO> findThreeNewsOrderByCreatedDateDesc();
	NewsDTO findNewsById(long id);
}
