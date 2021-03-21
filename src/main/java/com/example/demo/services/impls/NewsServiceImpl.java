package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
//import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.NewsDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Account;
import com.example.demo.models.News;
import com.example.demo.repositories.IAccountRepository;
import com.example.demo.repositories.INewsRepository;
import com.example.demo.services.INewsService;;

@Service
public class NewsServiceImpl implements INewsService {
	private final int TITLE_MAX_LENGTH = 100;
	private final int SHORTDESCRIPTION_MAX_LENGTH = 150;

	@Autowired
	private INewsRepository iNewsRepository;

	@Autowired
	private IAccountRepository iAccountRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<NewsDTO> findAllNewsOrderByCreatedDateDesc(boolean isStudent) {
		List<News> newsList = null;

		// 1. connect database through repository
		// 2. find all entities are not disable and sort descending by Created Date
		if (isStudent) {
			newsList = iNewsRepository.findByIsDisableOrderByCreatedDateDesc(false);
		} else {
			newsList = iNewsRepository.findByOrderByCreatedDateDesc();
		}

		List<NewsDTO> newsDTOList = new ArrayList<>();

		// 3. convert all entities to dtos
		// 4. add all dtos to newsDTOList
		if (!newsList.isEmpty()) {
			for (News news : newsList) {
				NewsDTO newsDTO = null;

				// 5. set response by view and return
				if (isStudent) {
					newsDTO = new NewsDTO(news.getId(), news.getNewsTitle(), news.getShortDescription(),
							news.getCreatedDate());
				} else {
					newsDTO = modelMapper.map(news, NewsDTO.class);
					newsDTO.setShortDescription(null);
					newsDTO.setNewsContent(null);
					newsDTO.setAccountId(0);
				}
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
				NewsDTO newsDTO = new NewsDTO(news.getId(), news.getNewsTitle(), news.getShortDescription(),
						news.getCreatedDate());
				threeNewest.add(newsDTO);
			}
		}

		return threeNewest;
	}

	@Override
	public NewsDTO findNewsById(long id) {
		// 1. connect database through repository
		// 2. find entity by Id
		// 3. if not found throw not found exception
		// 4. else convert entity to dto
		// 5. return
		News news = iNewsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());

		NewsDTO newsDTO = null;
		if (!news.isDisable()) {
			newsDTO = new NewsDTO(news.getNewsTitle(), news.getNewsContent(), news.getCreatedDate());
		}

		return newsDTO;
	}

	@Override
	public String createNews(String newsTitle, String shortDescription, String newsContent, long accountId) {
		String error = "";

		// 1. connect database through repository
		// 2. find entity by Id
		// 3. if not found throw not found exception
		Account account = iAccountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException());

		// 4. if role is not admin, return error no permission
		if (account.getRoleId() != 1) {
			error = "You do not have permission";
			return error;
		}

		// 5. validate parameter
		if (newsTitle.isEmpty() || newsTitle.length() > TITLE_MAX_LENGTH) {
			error += "NewsTitle is invalid!, ";
		}
		if (shortDescription.length() > SHORTDESCRIPTION_MAX_LENGTH) {
			error += "ShortDescription is invalid!, ";
		}
		if (newsContent.isEmpty()) {
			error += "NewsContent is invalid!";
		}

		// 6. if parameter valid, create new entity and return SUCCESS
		// 7. else return error
		if (!error.isEmpty()) {
			
			return error;
		} else {
			News news = new News();
			news.setNewsTitle(newsTitle);
			news.setShortDescription(shortDescription);
			news.setNewsContent(newsContent);
			news.setDisable(false);
			news.setAccountId(accountId);
			iNewsRepository.save(news);

			return "CREATE SUCCESS!";
		}
	}

	@Override
	public String updateNews(long id, String newsTitle, String shortDescription, String newsContent) {
		String error = "";

		// 1. connect database through repository
		// 2. find entity by id
		// 3. if not existed throw exception
		News news = iNewsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());

		// 4. validate parameter
		if (newsTitle.length() > TITLE_MAX_LENGTH) {
			error += "NewsTitle is invalid!, ";
		}
		if (shortDescription.length() > SHORTDESCRIPTION_MAX_LENGTH) {
			error += "ShortDescription is invalid!";
		}

		// 5. if parameter valid, update entity and return SUCCESS
		// 6. else return error
		if (!error.isEmpty()) {
			
			return error;
		} else {
			if (!newsTitle.isEmpty()) {
				news.setNewsTitle(newsTitle);
			}
			news.setShortDescription(shortDescription);
			if (!newsContent.isEmpty()) {
				news.setNewsContent(newsContent);
			}
			iNewsRepository.save(news);
			
			return "UPDATE SUCCESS!";
		}
	}

	@Override
	public String deleteNews(long id) {
		// 1. connect database through repository
		// 2. find entity by id
		// 3. if not existed throw exception
		News news = iNewsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());

		// 4. update entity with isDisable = true
		if (news.isDisable()) {
			return "Id is invalid";
		}
		news.setDisable(true);
		iNewsRepository.save(news);
		
		return "DELETE SUCCESS!";
	}
}
