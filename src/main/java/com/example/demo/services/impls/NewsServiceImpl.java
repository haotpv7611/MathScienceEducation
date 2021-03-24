package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.NewsRequestDTO;
import com.example.demo.dtos.NewsResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Account;
import com.example.demo.models.News;
import com.example.demo.repositories.IAccountRepository;
import com.example.demo.repositories.INewsRepository;
import com.example.demo.services.INewsService;;

@Service
public class NewsServiceImpl implements INewsService {

	@Autowired
	private INewsRepository iNewsRepository;

	@Autowired
	private IAccountRepository iAccountRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<NewsResponseDTO> findAllNewsOrderByCreatedDateDesc(boolean isStudent) {
		List<News> newsList = null;

		// 1. connect database through repository
		// 2. find all entities are not disable and sort descending by Created Date
		if (isStudent) {
			newsList = iNewsRepository.findByIsDisableOrderByCreatedDateDesc(false);
		} else {
			newsList = iNewsRepository.findByOrderByCreatedDateDesc();
		}

		List<NewsResponseDTO> newsDTOList = new ArrayList<>();

		// 3. convert all entities to dtos
		// 4. add all dtos to newsDTOList
		if (!newsList.isEmpty()) {
			for (News news : newsList) {
				NewsResponseDTO newsResponseDTO = null;

				// 5. set response by view and return
				if (isStudent) {
					newsResponseDTO = new NewsResponseDTO(news.getId(), news.getNewsTitle(), news.getShortDescription(),
							news.getCreatedDate());
				} else {
					newsResponseDTO = modelMapper.map(news, NewsResponseDTO.class);
					newsResponseDTO.setShortDescription(null);
					newsResponseDTO.setNewsContent(null);
					newsResponseDTO.setAccountId(0);
				}
				newsDTOList.add(newsResponseDTO);
			}
		}

		return newsDTOList;
	}

	@Override
	public List<NewsResponseDTO> findThreeNewsOrderByCreatedDateDesc() {
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

		List<NewsResponseDTO> threeNewest = new ArrayList<>();

		// 5. convert 3 entity to dto
		// 6. add all dto to newsDTOList and return
		if (!newsList.isEmpty()) {
			for (News news : newsList) {
				NewsResponseDTO newsResponseDTO = new NewsResponseDTO(news.getId(), news.getNewsTitle(),
						news.getShortDescription(), news.getCreatedDate());
				threeNewest.add(newsResponseDTO);
			}
		}

		return threeNewest;
	}

	@Override
	public NewsResponseDTO findNewsById(long id) {
		// 1. connect database through repository
		// 2. find entity by Id
		// 3. if not found throw not found exception
		// 4. else convert entity to dto
		// 5. return
		News news = iNewsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());

		if (news.isDisable()) {
			throw new ResourceNotFoundException();
		}
		NewsResponseDTO newsResponseDTO = new NewsResponseDTO(news.getNewsTitle(), news.getNewsContent(),
				news.getCreatedDate());

		return newsResponseDTO;
	}

	@Override
	public String createNews(NewsRequestDTO newsRequestDTO) {
		String error = "";

		// 1. connect database through repository
		// 2. find entity by Id
		// 3. if not found throw not found exception
		Account account = iAccountRepository.findById(newsRequestDTO.getAccountId())
				.orElseThrow(() -> new ResourceNotFoundException());
		if (account.isDisable()) {
			throw new ResourceNotFoundException();
		}

		// 4. if role is not admin, return error no permission
		if (account.getRoleId() != 1) {
			error = "You do not have permission";
			return error;
		}

		// 5. create new entity and return SUCCESS
		News news = modelMapper.map(newsRequestDTO, News.class);
		news.setDisable(false);
		iNewsRepository.save(news);

		return "CREATE SUCCESS!";
	}

	@Override
	public String updateNews(NewsRequestDTO newsRequestDTO) {

		// 1. connect database through repository
		// 2. find entity by id
		// 3. if not existed throw exception
		News news = iNewsRepository.findById(newsRequestDTO.getId()).orElseThrow(() -> new ResourceNotFoundException());

		if (news.isDisable()) {
			throw new ResourceNotFoundException();
		}
		// 5. if parameter valid, update entity and return SUCCESS
		// 6. else return error

		news.setNewsTitle(newsRequestDTO.getNewsTitle());
		news.setShortDescription(newsRequestDTO.getShortDescription());
		news.setNewsContent(newsRequestDTO.getNewsContent());
		iNewsRepository.save(news);

		return "UPDATE SUCCESS!";
	}

	@Override
	public String deleteNews(long id) {
		// 1. connect database through repository
		// 2. find entity by id
		// 3. if not existed throw exception
		News news = iNewsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());

		// 4. update entity with isDisable = true
		if (news.isDisable()) {
			throw new ResourceNotFoundException();
		}
		news.setDisable(true);
		iNewsRepository.save(news);

		return "DELETE SUCCESS!";
	}
}
