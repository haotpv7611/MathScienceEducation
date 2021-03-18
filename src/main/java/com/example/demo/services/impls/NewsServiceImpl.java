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

		// 1. connect database through repository
		// 2. find all entities are not disable and sort descending by Created Date
		List<News> newsList = iNewsRepository.findByIsDisableOrderByCreatedDateDesc(false);

		List<NewsDTO> newsDTOList = new ArrayList<>();

		// 3. convert all entities to dtos
		// 4. add all dtos to newsDTOList and return
		if (!newsList.isEmpty()) {
			for (News news : newsList) {
				NewsDTO newsDTO = modelMapper.map(news, NewsDTO.class);
				newsDTOList.add(newsDTO);
			}
		}

		return newsDTOList;
	}

	@Override
	public List<NewsDTO> findThreeNewsOrderByCreatedDateDesc() {

		List<News> newsList = null;

		// 1. connect database through repository
		// 2. count news entities
		// 3. if have more than 3 entities, get 3 entities are not disable and sort
		// descending by Created Date
		// 4. else list all entities are not disable and sort descending by Created Date
		if (iNewsRepository.count() >= 3) {
			newsList = iNewsRepository.findTop3ByIsDisableOrderByCreatedDateDesc(false);
		} else {
			newsList = iNewsRepository.findByIsDisableOrderByCreatedDateDesc(false);
		}

		List<NewsDTO> threeNewest = new ArrayList<>();

		// 5. convert 3 entity to dto
		// 6. add all dto to newsDTOList and return
		if (!newsList.isEmpty()) {
			for (News news : newsList) {
				NewsDTO newsDTO = modelMapper.map(news, NewsDTO.class);
				threeNewest.add(newsDTO);
			}
		}

		return threeNewest;
	}

	@Override
	public NewsDTO findNewsById(long id) {

		// 1. connect database through repository
		// 2. find entities by Id
		// 3. if not found throw not found exception
		// 4. else convert entity to dto
		// 5. return
		News news = iNewsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
		NewsDTO newsDTO = modelMapper.map(news, NewsDTO.class);

		return newsDTO;
	}
}
