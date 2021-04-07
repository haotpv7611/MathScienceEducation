package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public String createNews(NewsRequestDTO newsRequestDTO) {		
		// 1. connect database through repository
		// 2. find entity by Id
		// 3. if not found throw not found exception
		Account account = iAccountRepository.findByIdAndStatusNot(newsRequestDTO.getAccountId(), "DELETED");
		if (account == null) {
			throw new ResourceNotFoundException();
		}
		// 4. if role is not admin, return error no permission
		if (account.getRoleId() != 1) {

			return "You do not have permission!";
		}

		// 5. create new entity and return SUCCESS
		News news = modelMapper.map(newsRequestDTO, News.class);
		news.setDisable(false);
		iNewsRepository.save(news);

		return "CREATE SUCCESS!";
	}

	@Override
	public List<NewsResponseDTO> findAllNewsOrderByCreatedDateDesc(boolean isStudent) {
//		List<News> newsList = null;
		List<News> newsList = iNewsRepository.findByIsDisableFalseOrderByCreatedDateDesc();
		// 1. connect database through repository
		// 2. find all entities are not disable and sort descending by Created Date

		

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
	public NewsResponseDTO findNewsById(long id) {
		// 1. connect database through repository
		// 2. find entity by Id
		// 3. if not found throw not found exception
		// 4. else convert entity to dto
		// 5. return
		News news = iNewsRepository.findByIdAndIsDisableFalse(id);
		if (news == null) {
			throw new ResourceNotFoundException();
		}
		NewsResponseDTO newsResponseDTO = new NewsResponseDTO(news.getNewsTitle(), news.getShortDescription(),
				news.getNewsContent(), news.getCreatedDate());

		return newsResponseDTO;
	}

	@Override
	@Transactional
	public String deleteNews(List<Long> ids) {
		// 1. connect database through repository
		// 2. find entity by id
		// 3. if not existed throw exception
		for (Long id : ids) {
			News news = iNewsRepository.findByIdAndIsDisableFalse(id);
			if (news == null) {
				throw new ResourceNotFoundException();
			}
			news.setDisable(true);
			iNewsRepository.save(news);
		}

		return "DELETE SUCCESS!";
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
			newsList = iNewsRepository.findTop3ByIsDisableFalseOrderByCreatedDateDesc();
		} else {
			newsList = iNewsRepository.findByIsDisableFalseOrderByCreatedDateDesc();
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

}
