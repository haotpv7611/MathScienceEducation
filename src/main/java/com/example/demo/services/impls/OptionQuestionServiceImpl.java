package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.OptionQuestionDTO;
import com.example.demo.models.OptionQuestion;
import com.example.demo.repositories.IOptionQuestionRepository;
import com.example.demo.services.IOptionQuestionService;

@Service
public class OptionQuestionServiceImpl implements IOptionQuestionService {

	@Autowired
	private IOptionQuestionRepository iOptionsRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<OptionQuestionDTO> findByQuestionId(long questionId) {

		// 1. connect database through repository
		// 2. find all entities are not disable and have questionId = ?
		List<OptionQuestion> optionList = iOptionsRepository.findByQuestionIdAndIsDisable(questionId, false);

		List<OptionQuestionDTO> optionDTOList = new ArrayList<>();

		// 3. convert all entities to dtos
		// 4. add all dtos to newsDTOList and return
		if (!optionList.isEmpty()) {
			for (OptionQuestion option : optionList) {
				OptionQuestionDTO optionQuestionDTO = modelMapper.map(option, OptionQuestionDTO.class);
				optionDTOList.add(optionQuestionDTO);
			}
		}

		return optionDTOList;
	}
}
