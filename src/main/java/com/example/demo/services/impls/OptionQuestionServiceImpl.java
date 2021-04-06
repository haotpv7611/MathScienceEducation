package com.example.demo.services.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.OptionQuestionDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.OptionQuestion;
import com.example.demo.repositories.IOptionQuestionRepository;
import com.example.demo.services.IOptionQuestionService;

@Service
public class OptionQuestionServiceImpl implements IOptionQuestionService {

	@Autowired
	private IOptionQuestionRepository iOptionsRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	FirebaseService firebaseService;

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

	@Override
	public void createExerciseOptionQuestion(long questionId, String optionText, boolean isCorrect) {
		OptionQuestion optionQuestion = new OptionQuestion(optionText, isCorrect, questionId, false);
		iOptionsRepository.save(optionQuestion);
	}

	@Override
	public void createGameFillInBlankOptionQuestion(long questionId, String optionText, String optionInputType) {
		OptionQuestion optionQuestion = new OptionQuestion(optionText, optionInputType, questionId, false);
		iOptionsRepository.save(optionQuestion);

	}

	@Override
	public void createGameSwappingMatchingChoosingOptionQuestion(long questionId, String optionText,
			MultipartFile imageFile) throws SizeLimitExceededException, IOException {

		OptionQuestion optionQuestion = new OptionQuestion(optionText, questionId, false);
		if (imageFile != null) {
			optionQuestion.setOptionImageUrl(firebaseService.saveFile(imageFile));
		}

		iOptionsRepository.save(optionQuestion);

	}
	@Override
	public void deleteOptionQuestion(long id) {
		OptionQuestion optionQuestion = iOptionsRepository.findByIdAndIsDisableFalse(id);
		if (optionQuestion == null) {
			throw new ResourceNotFoundException();
		}
		optionQuestion.setDisable(true);		
		iOptionsRepository.save(optionQuestion);
	}
}
