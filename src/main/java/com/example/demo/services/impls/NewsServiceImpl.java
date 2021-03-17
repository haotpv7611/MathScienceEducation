package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
//import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.NewsDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.News;
import com.example.demo.repositories.INewsRepository;
import com.example.demo.services.INewsService;;

@Service
public class NewsServiceImpl implements INewsService {

	@Autowired
	private INewsRepository iNewsRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<NewsDTO> findAllNewsOrderByCreatedDateDesc() {

		// get list Entity enable and sort descending by Created Date
		List<News> newsList = iNewsRepository.findByIsDisableOrderByCreatedDateDesc(false);
		List<NewsDTO> newsDTOList = new ArrayList<>();
		// convert list Entity to list DTO
		if (newsList != null) {
			for (News news : newsList) {
				newsDTOList.add(modelMapper.map(news, NewsDTO.class));
			}
		}

		// response DTO to user
		return newsDTOList;
	}

	@Override
	public List<NewsDTO> findThreeNewsOrderByCreatedDateDesc() {
		
		
		List<News> newsList = iNewsRepository.findByIsDisableOrderByCreatedDateDesc(false);
		List<NewsDTO> threeNewest = new ArrayList<>();
		if (newsList.size() > 0) {
			for (int i = 0; i < 3; i++) {
				threeNewest.add(modelMapper.map(newsList.get(i), NewsDTO.class));
			}
		}

		return threeNewest;
	}

	@Override
	public NewsDTO findNewsById(Long id){
		News news = iNewsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
		return modelMapper.map(news, NewsDTO.class);
	}
}
