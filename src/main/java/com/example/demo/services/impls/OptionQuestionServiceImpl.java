package com.example.demo.services.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.OptionQuestionDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.OptionQuestion;
import com.example.demo.repositories.IOptionQuestionRepository;
import com.example.demo.services.IOptionQuestionService;

@Service
public class OptionQuestionServiceImpl implements IOptionQuestionService {

	@Autowired
	private IOptionQuestionRepository iOptionQuestionRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	FirebaseService firebaseService;

	// not ok about data response OptionQuestionDTO
	@Override
	public List<OptionQuestionDTO> findByQuestionId(long questionId) {
		// find all active option by questionId
		List<OptionQuestion> optionList = iOptionQuestionRepository.findByQuestionIdAndIsDisableFalse(questionId);
		List<OptionQuestionDTO> optionDTOList = new ArrayList<>();

		// convert all entities to dtos and return list option
		// cần check lại data response cho role student
		if (!optionList.isEmpty()) {
			for (OptionQuestion option : optionList) {
				OptionQuestionDTO optionQuestionDTO = modelMapper.map(option, OptionQuestionDTO.class);
				optionDTOList.add(optionQuestionDTO);
			}
		}

		return optionDTOList;
	}

	// done ok
	@Override
	public void createExerciseOptionQuestion(long questionId, String optionText, boolean isCorrect) {
		OptionQuestion optionQuestion = new OptionQuestion(optionText, isCorrect, questionId, false);
		try {
			iOptionQuestionRepository.save(optionQuestion);
		} catch (Exception e) {
			throw e;
		}
	}

	// done ok
	@Override
	public void createGameFillInBlankOptionQuestion(long questionId, String optionText, String optionInputType) {
		OptionQuestion optionQuestion = new OptionQuestion(optionText, optionInputType, questionId, false);
		try {
			iOptionQuestionRepository.save(optionQuestion);
		} catch (Exception e) {
			throw e;
		}
	}

	// done ok
	@Override
	@Transactional
	public void createGameSwappingMatchingChoosingOptionQuestion(long questionId, String optionText,
			MultipartFile imageFile) throws SizeLimitExceededException, IOException {
		OptionQuestion optionQuestion = new OptionQuestion(optionText, questionId, false);
		try {
			if (imageFile != null) {
				optionQuestion.setOptionImageUrl(firebaseService.saveFile(imageFile));
			}
			iOptionQuestionRepository.save(optionQuestion);
		} catch (Exception e) {
			throw e;
		}
	}

	// done ok
	@Override
	public void updateExerciseOptionQuestion(long id, String optionText, boolean isCorrect) {
		try {
			// validate optionId
			OptionQuestion optionQuestion = iOptionQuestionRepository.findByIdAndIsDisableFalse(id);
			if (optionQuestion == null) {
				throw new ResourceNotFoundException();
			}

			// allow update optionText, isCorrect and save data
			optionQuestion.setOptionText(optionText);
			optionQuestion.setCorrect(isCorrect);
			iOptionQuestionRepository.save(optionQuestion);
		} catch (Exception e) {
			throw e;
		}
	}

	// done ok
	@Override
	public void updateGameFillInBlankOptionQuestion(long id, String optionText, String optionInputType) {
		try {
			// validate optionId
			OptionQuestion optionQuestion = iOptionQuestionRepository.findByIdAndIsDisableFalse(id);
			if (optionQuestion == null) {
				throw new ResourceNotFoundException();
			}

			// allow update optionText, optionInputType and save data
			optionQuestion.setOptionText(optionText);
			optionQuestion.setOptionInputType(optionInputType);
			iOptionQuestionRepository.save(optionQuestion);
		} catch (Exception e) {
			throw e;
		}
	}

	// done ok
	@Override
	@Transactional
	public void updateGameSwappingMatchingChoosingOptionQuestion(long id, String optionText, MultipartFile imageFile)
			throws SizeLimitExceededException, IOException
	{
		try {
			// validate optionId
			OptionQuestion optionQuestion = iOptionQuestionRepository.findByIdAndIsDisableFalse(id);
			if (optionQuestion == null) {
				throw new ResourceNotFoundException();
			}

			// allow update optionText, optionImageUrl if not null and save data
			optionQuestion.setOptionText(optionText);
			if (imageFile != null) {
				String optionImageUrl = optionQuestion.getOptionImageUrl();
				firebaseService.deleteFile(optionImageUrl);
				optionQuestion.setOptionImageUrl(firebaseService.saveFile(imageFile));
			}
			iOptionQuestionRepository.save(optionQuestion);
		} catch (Exception e) {							
			throw e;
		}
	}

	// done ok
	@Override
	@Transactional
	public void deleteOptionQuestion(long id) {
		// validate optionId
		try {
			OptionQuestion optionQuestion = iOptionQuestionRepository.findByIdAndIsDisableFalse(id);
			if (optionQuestion == null) {
				throw new ResourceNotFoundException();
			}

			// delete image in firebase, set null after delete option
			String optionImageUrl = optionQuestion.getOptionImageUrl();
			optionQuestion.setDisable(true);
			optionQuestion.setOptionImageUrl(null);
			iOptionQuestionRepository.save(optionQuestion);
			if (optionImageUrl != null) {
				firebaseService.deleteFile(optionImageUrl);
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
