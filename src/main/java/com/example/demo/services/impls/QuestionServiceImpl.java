package com.example.demo.services.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.OptionQuestionChooseDTO;
import com.example.demo.dtos.OptionQuestionExerciseDTO;
import com.example.demo.dtos.OptionQuestionFillDTO;
import com.example.demo.dtos.OptionQuestionGameDTO;
import com.example.demo.dtos.QuestionExerciseViewDTO;
import com.example.demo.dtos.QuestionGameViewDTO;
import com.example.demo.dtos.QuestionOptionResponseDTO;
import com.example.demo.dtos.QuestionResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.ExerciseGameQuestion;
import com.example.demo.models.OptionQuestion;
import com.example.demo.models.Question;
import com.example.demo.models.QuestionType;
import com.example.demo.models.Unit;
import com.example.demo.repositories.IExerciseGameQuestionRepository;
import com.example.demo.repositories.IOptionQuestionRepository;
import com.example.demo.repositories.IQuestionRepository;
import com.example.demo.repositories.IQuestionTypeRepository;
import com.example.demo.repositories.IUnitRepository;
import com.example.demo.services.IFirebaseService;
import com.example.demo.services.IOptionQuestionService;
import com.example.demo.services.IQuestionService;
import com.example.demo.utils.Util;

@Service
public class QuestionServiceImpl implements IQuestionService {
	Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

	private final int QUESTION_TITLE_LENGTH = 250;
	private final int DESCRIPTION_LENGTH = 250;
	private final int OPTION_TEXT_LENGTH = 100;

	@Autowired
	private IQuestionRepository iQuestionRepository;

	@Autowired
	private IFirebaseService iFirebaseService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IOptionQuestionService iOptionsService;

	@Autowired
	private IUnitRepository iUnitRepository;

	@Autowired
	private IQuestionTypeRepository iQuestionTypeRepository;

	@Autowired
	private IOptionQuestionRepository iOptionQuestionRepository;

	@Autowired
	private IExerciseGameQuestionRepository iExerciseGameQuestionRepository;

	// done
	@Override
	public Object findQuestionById(long id, String questionType) {
		Object object = null;
		try {
			// validate data input
			Question question = iQuestionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
			if (!questionType.equalsIgnoreCase(question.getQuestionType().getDescription())) {
				throw new ResourceNotFoundException();
			}

			// get all active option by questionId = ?
			List<OptionQuestion> optionQuestionList = iOptionQuestionRepository.findByQuestionIdAndIsDisableFalse(id);

			// convert list option to ExerciseDTO
			// add question, list optionDTO to questionDTO and return
			if (questionType.equals("EXERCISE")) {
				List<Object> optionQuestionExerciseDTOList = new ArrayList<>();
				if (!optionQuestionList.isEmpty()) {
					for (OptionQuestion optionQuestion : optionQuestionList) {
						OptionQuestionExerciseDTO optionQuestionExerciseDTO = modelMapper.map(optionQuestion,
								OptionQuestionExerciseDTO.class);
						optionQuestionExerciseDTOList.add(optionQuestionExerciseDTO);
					}
				}
				QuestionOptionResponseDTO questionOptionResponseDTO = modelMapper.map(question,
						QuestionOptionResponseDTO.class);
				questionOptionResponseDTO.setOptionQuestionDTOList(optionQuestionExerciseDTOList);
				object = questionOptionResponseDTO;
			}

			// convert list option to GameFillDTO
			// add question, list optionDTO to questionDTO and return
			if (questionType.equals("FILL")) {
				List<Object> optionQuestionFillDTOList = new ArrayList<>();
				if (!optionQuestionList.isEmpty()) {
					for (OptionQuestion optionQuestion : optionQuestionList) {
						OptionQuestionFillDTO optionQuestionFillDTO = new OptionQuestionFillDTO();
						if (optionQuestion.getOptionInputType().equalsIgnoreCase("Text")) {
							optionQuestionFillDTO.setText(optionQuestion.getOptionText());
						}
						if (optionQuestion.getOptionInputType().equalsIgnoreCase("Operator")) {
							optionQuestionFillDTO.setOperator(optionQuestion.getOptionText());
						}

						optionQuestionFillDTO.setId(optionQuestion.getId());
						optionQuestionFillDTO.setOptionInputType(optionQuestion.getOptionInputType());
						optionQuestionFillDTOList.add(optionQuestionFillDTO);
					}
				}
				QuestionOptionResponseDTO questionOptionResponseDTO = modelMapper.map(question,
						QuestionOptionResponseDTO.class);
				questionOptionResponseDTO.setOptionQuestionDTOList(optionQuestionFillDTOList);
				object = questionOptionResponseDTO;
			}

			// convert list option to GameOtherDTO
			// add question, list optionDTO to questionDTO and return
			if (questionType.equals("MATCH") || questionType.equals("CHOOSE") || questionType.equals("SWAP")) {
				List<Object> optionQuestionGameDTOList = new ArrayList<>();
				if (!optionQuestionList.isEmpty()) {
					for (OptionQuestion optionQuestion : optionQuestionList) {
						OptionQuestionGameDTO optionQuestionGameDTO = modelMapper.map(optionQuestion,
								OptionQuestionGameDTO.class);
						optionQuestionGameDTOList.add(optionQuestionGameDTO);
					}
				}
				QuestionOptionResponseDTO questionOptionResponseDTO = modelMapper.map(question,
						QuestionOptionResponseDTO.class);
				questionOptionResponseDTO.setOptionQuestionDTOList(optionQuestionGameDTOList);
				object = questionOptionResponseDTO;
			}
		} catch (Exception e) {
			logger.error("FIND: questionId = " + id + " and questionType = " + questionType + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "FIND FAIL!";
		}

		return object;
	}

	// done ok
	@Override
	public List<QuestionResponseDTO> findQuestionByExerciseIdOrGameId(long id, boolean isExercise) {
		List<ExerciseGameQuestion> exerciseGameQuestionList = null;
		List<QuestionResponseDTO> questionResponseDTOList = new ArrayList<>();

		try {
			if (isExercise) {
				exerciseGameQuestionList = iExerciseGameQuestionRepository.findByExerciseIdAndIsDisableFalse(id);
			} else {
				exerciseGameQuestionList = iExerciseGameQuestionRepository.findByGameIdAndIsDisableFalse(id);
			}

			if (!exerciseGameQuestionList.isEmpty()) {
				for (int i = 0; i < exerciseGameQuestionList.size(); i++) {
					Question question = iQuestionRepository
							.findByIdAndIsDisableFalse(exerciseGameQuestionList.get(i).getQuestionId());
					QuestionResponseDTO questionResponseDTO = modelMapper.map(question, QuestionResponseDTO.class);
					questionResponseDTO.setQuestionType(question.getQuestionType().getDescription());
					questionResponseDTOList.add(questionResponseDTO);
				}
			}
		} catch (Exception e) {
			logger.error("FIND: all question by" + (isExercise ? "exerciseId = " : "gameId = ") + id + "! "
					+ e.getMessage());

			return null;
		}

		return questionResponseDTOList;
	}

	// done
	@Override
	public List<QuestionResponseDTO> findAllByUnitId(long unitId, boolean isExercise) {
		List<QuestionResponseDTO> questionDTOList = new ArrayList<>();
		try {
			// find all active question by unitId = ? and is exerciseQuestion or is
			// gameQuestion
			List<Question> questionList = null;
			if (isExercise) {
				questionList = iQuestionRepository.findByUnitIdAndQuestionTypeIdAndIsDisableFalse(unitId, 1);
			} else {
				questionList = iQuestionRepository.findByUnitIdAndQuestionTypeIdNotAndIsDisableFalse(unitId, 1);
			}

			// convert to DTO and return

			for (Question question : questionList) {
				QuestionResponseDTO questionResponseDTO = modelMapper.map(question, QuestionResponseDTO.class);
				questionResponseDTO.setQuestionType(question.getQuestionType().getDescription());
				questionDTOList.add(questionResponseDTO);
			}
		} catch (Exception e) {
			logger.error("FIND: all question by unitId = " + unitId + "! " + e.getMessage());

			return null;
		}

		return questionDTOList;
	}

	@Override
	public List<QuestionExerciseViewDTO> findQuestionByExerciseId(long exerciseId) {
		List<QuestionExerciseViewDTO> questionExerciseViewDTOList = new ArrayList<>();
		try {
			// get list questionId by exerciseGame
			List<ExerciseGameQuestion> exerciseGameQuestionList = iExerciseGameQuestionRepository
					.findByExerciseIdAndIsDisableFalse(exerciseId);
			List<Long> questionIdList = new ArrayList<>();
			if (!exerciseGameQuestionList.isEmpty()) {
				for (ExerciseGameQuestion exerciseGameQuestion : exerciseGameQuestionList) {
					questionIdList.add(exerciseGameQuestion.getQuestionId());
				}
			}

			if (!questionIdList.isEmpty()) {
				List<Question> questionList = iQuestionRepository.findByIsDisableFalseAndIdIn(questionIdList);

				if (!questionList.isEmpty()) {
					for (Question question : questionList) {
						List<OptionQuestionExerciseDTO> optionQuestionExerciseDTOList = iOptionsService
								.findExerciseOptionByQuestionId(question.getId());
						if (!optionQuestionExerciseDTOList.isEmpty()) {
							QuestionExerciseViewDTO questionExerciseViewDTO = modelMapper.map(question,
									QuestionExerciseViewDTO.class);
							questionExerciseViewDTO.setOptionList(optionQuestionExerciseDTOList);
							questionExerciseViewDTOList.add(questionExerciseViewDTO);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("FIND: all question by exerciseId = " + exerciseId + "! " + e.getMessage());

			return null;
		}

		return questionExerciseViewDTOList;
	}

	// gameId --> listQuestion

	@Override
	public List<Object> findQuestionByGameId(long gameId) {
		List<Object> questionGameViewDTOList = new ArrayList<>();

		List<ExerciseGameQuestion> exerciseGameQuestionList = iExerciseGameQuestionRepository
				.findByGameIdAndIsDisableFalse(gameId);

		// find all question in game
		List<Question> questionList = new ArrayList<>();

		if (!exerciseGameQuestionList.isEmpty()) {
			for (ExerciseGameQuestion exerciseGameQuestion : exerciseGameQuestionList) {
				Question question = iQuestionRepository.findByIdAndIsDisableFalse(exerciseGameQuestion.getQuestionId());
				questionList.add(question);
			}

		}

		if (!questionList.isEmpty()) {
			for (Question question : questionList) {

				// find all option by questionId
				List<Object> optionQuestionGameDTOList = new ArrayList<>();
				List<OptionQuestion> optionQuestionList = iOptionQuestionRepository
						.findByQuestionIdAndIsDisableFalse(question.getId());
				if (!optionQuestionList.isEmpty()) {
					String questionType = question.getQuestionType().getDescription();
					if (questionType.equals("SWAP") || questionType.equals("MATCH")) {
						List<Integer> integerList = IntStream.rangeClosed(0, optionQuestionList.size() - 1).boxed()
								.collect(Collectors.toList());
						Collections.shuffle(integerList);

						for (int i = 0; i < optionQuestionList.size(); i++) {

							OptionQuestionGameDTO optionQuestionGameDTO = modelMapper.map(optionQuestionList.get(i),
									OptionQuestionGameDTO.class);
							optionQuestionGameDTO
									.setWrongOptionText(optionQuestionList.get(integerList.get(i)).getOptionText());
							optionQuestionGameDTOList.add(optionQuestionGameDTO);
						}
					}

					if (questionType.equals("CHOOSE")) {
						Collections.shuffle(optionQuestionList);
						for (OptionQuestion optionQuestion : optionQuestionList) {
							OptionQuestionChooseDTO optionQuestionChooseDTO = modelMapper.map(optionQuestion,
									OptionQuestionChooseDTO.class);
							optionQuestionGameDTOList.add(optionQuestionChooseDTO);
						}
					}
					// chưa viết
					if (questionType.equals("FILL")) {
						for (OptionQuestion optionQuestion : optionQuestionList) {
							OptionQuestionFillDTO optionQuestionFillDTO = new OptionQuestionFillDTO();
							optionQuestionFillDTO.setText(optionQuestion.getOptionText());
							optionQuestionFillDTO.setOptionInputType(optionQuestion.getOptionInputType());
							optionQuestionGameDTOList.add(optionQuestionFillDTO);
						}
					}

					QuestionGameViewDTO questionGameViewDTO = new QuestionGameViewDTO();
					questionGameViewDTO.setQuestionType(question.getQuestionType().getDescription());
					questionGameViewDTO.setQuestionTitle(question.getQuestionTitle());
					questionGameViewDTO.setQuestionImageUrl(question.getQuestionImageUrl());
					questionGameViewDTO.setOptionQuestion(optionQuestionGameDTOList);
					questionGameViewDTOList.add(questionGameViewDTO);
				}
			}
		}
		return questionGameViewDTOList;

	}

	// done ok
	@Override
	@Transactional
	public String createExerciseQuestion(MultipartFile imageFile, MultipartFile audioFile, String questionTitle,
			String description, float score, String questionType, long unitId, List<String> optionTextList,
			List<Boolean> isCorrectList) throws SizeLimitExceededException, IOException {
		// validate question data input
		if (!questionType.equals("EXERCISE")) {
			throw new ResourceNotFoundException();
		}
		String error = validateQuestionInput(questionTitle, description, score);
		error += Util.validateFile(imageFile, "image", "\nNot supported this file type for image!");
		error += Util.validateFile(audioFile, "audio", "\nNot supported this file type for audio!");

		// validate option data input
		String optionError = validateExerciseOptionInput(optionTextList);
		error += optionError;
		if (!error.isEmpty()) {

			return error.trim();
		}

		try {
			Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);
			if (unit == null) {
				throw new ResourceNotFoundException();
			}

			// first: create question
			int questionTypeId = 1;
			long questionId = createQuestion(imageFile, audioFile, questionTitle, description, score, unitId,
					questionTypeId);

			// last: create list option and return
			for (int i = 0; i < optionTextList.size(); i++) {
				iOptionsService.createExerciseOptionQuestion(questionId, optionTextList.get(i), isCorrectList.get(i));
			}
		} catch (Exception e) {
			logger.error("CREATE: questionType = " + questionType + " in unitId =  " + unitId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CREATE FAIL!";
		}

		return "CREATE SUCCESS!";
	}

	// done ok
	@Override
	@Transactional
	public String createGameFillInBlankQuestion(MultipartFile imageFile, String questionTitle, String description,
			float score, String questionType, long unitId, List<String> optionTextList,
			List<String> optionInputTypeList) throws SizeLimitExceededException, IOException {
		// validate question data input
		if (!questionType.equals("FILL")) {
			throw new ResourceNotFoundException();
		}
		String error = validateQuestionInput(questionTitle, description, score);
		error += Util.validateFile(imageFile, "image", "\nNot supported this file type for image!");

		// validate option data input
		String optionError = validateGameFillInBlankOptionInput(optionTextList, optionInputTypeList);
		error += optionError;
		if (!error.isEmpty()) {

			return error.trim();
		}

		try {
			Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);
			if (unit == null) {
				throw new ResourceNotFoundException();
			}

			// first: create question
			int questionTypeId = 2;
			long questionId = createQuestion(imageFile, null, questionTitle, description, score, unitId,
					questionTypeId);

			// last: create list option and return
			for (int i = 0; i < optionTextList.size(); i++) {
				iOptionsService.createGameFillInBlankOptionQuestion(questionId, optionTextList.get(i),
						optionInputTypeList.get(i));
			}
		} catch (Exception e) {
			logger.error("CREATE: questionType = " + questionType + " in unitId =  " + unitId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CREATE FAIL!";
		}

		return "CREATE SUCCESS!";
	}

	// done ok
	@Override
	@Transactional
	public String createGameSwappingMatchingChoosingQuestion(String questionTitle, String description, float score,
			String questionType, long unitId, List<MultipartFile> imageFileList, List<String> optionTextList)
			throws SizeLimitExceededException, IOException {
		// validate question data input
		String[] questionTypes = { "MATCH", "SWAP", "CHOOSE" };
		List<String> questionTypeList = Arrays.asList(questionTypes);
		if (!questionTypeList.contains(questionType)) {
			throw new ResourceNotFoundException();
		}
		String error = validateQuestionInput(questionTitle, description, score);

		// validate option data input
		String optionError = validateSwappingMatchingChoosingOptionInput(imageFileList, optionTextList, "CREATE");
		error += optionError;
		if (!error.isEmpty()) {

			return error.trim();
		}

		try {
			Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);
			if (unit == null) {
				throw new ResourceNotFoundException();
			}

			// first: create question
			int questionTypeId = questionTypeList.indexOf(questionType) + 3;
			long questionId = createQuestion(null, null, questionTitle, description, score, unitId, questionTypeId);

			// last: create list option and return
			for (int i = 0; i < optionTextList.size(); i++) {
				iOptionsService.createGameSwappingMatchingChoosingOptionQuestion(questionId, optionTextList.get(i),
						imageFileList.get(i));
			}
		} catch (Exception e) {
			logger.error("CREATE: questionType = " + questionType + " in unitId =  " + unitId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CREATE FAIL!";
		}

		return "CREATE SUCCESS!";
	}

	// done ok
	@Override
	@Transactional
	public String updateExerciseQuestion(long id, MultipartFile imageFile, MultipartFile audioFile,
			String questionTitle, String description, float score, List<Long> optionIdList, List<String> optionTextList,
			List<Boolean> isCorrectList) throws SizeLimitExceededException, IOException {
		// validate question data input
		String error = validateQuestionInput(questionTitle, description, score);
		error += Util.validateFile(imageFile, "image", "\nNot supported this file type for image!");
		error += Util.validateFile(audioFile, "audio", "\nNot supported this file type for audio!");

//		try {
			Question question = iQuestionRepository.findByIdAndIsDisableFalse(id);
			if (question == null) {
				throw new ResourceNotFoundException();
			}
			// validate option data input
			for (long optionId : optionIdList) {
				OptionQuestion optionQuestion = iOptionQuestionRepository.findByIdAndIsDisableFalse(optionId);
				if (optionQuestion == null) {
					throw new ResourceNotFoundException();
				}
			}
			String optionError = validateExerciseOptionInput(optionTextList);
			error += optionError;
			if (!error.isEmpty()) {

				return error.trim();
			}

			// first: update question
			updateQuestion(id, imageFile, audioFile, questionTitle, description, score);

			// last: update list option and return
			for (int i = 0; i < optionIdList.size(); i++) {
				iOptionsService.updateExerciseOptionQuestion(optionIdList.get(i), optionTextList.get(i),
						isCorrectList.get(i));
			}
//		} catch (Exception e) {
//			logger.error("UPDATE: questionId = " + id + "! " + e.getMessage());
//			if (e instanceof ResourceNotFoundException) {
//
//				return "NOT FOUND!";
//			}
//
//			return "UPDATE FAIL!";
//		}

		return "UPDATE SUCCESS!";
	}

	// done ok
	@Override
	@Transactional
	public String updateGameFillInBlankQuestion(long id, MultipartFile imageFile, String questionTitle,
			String description, float score, List<Long> optionIdList, List<String> optionTextList,
			List<String> optionInputTypeList) throws SizeLimitExceededException, IOException {
		// validate question data input

		String error = validateQuestionInput(questionTitle, description, score);
		error += Util.validateFile(imageFile, "image", "\nNot supported this file type for image!");

		try {
			Question question = iQuestionRepository.findByIdAndIsDisableFalse(id);
			if (question == null) {
				throw new ResourceNotFoundException();
			}
			// validate option data input
			for (long optionId : optionIdList) {
				OptionQuestion optionQuestion = iOptionQuestionRepository.findByIdAndIsDisableFalse(optionId);
				if (optionQuestion == null) {
					throw new ResourceNotFoundException();
				}
			}
			String optionError = validateGameFillInBlankOptionInput(optionTextList, optionInputTypeList);
			error += optionError;
			if (!error.isEmpty()) {

				return error.trim();
			}

			// first: update question
			updateQuestion(id, imageFile, null, questionTitle, description, score);

			// last: update list option and return
			for (int i = 0; i < optionIdList.size(); i++) {
				iOptionsService.updateGameFillInBlankOptionQuestion(optionIdList.get(i), optionTextList.get(i),
						optionInputTypeList.get(i));
			}
		} catch (Exception e) {
			logger.error("UPDATE: questionId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "UPDATE FAIL!";
		}

		return "UPDATE SUCCESS!";
	}

	// done ok
	@Override
	@Transactional
	public String updateGameSwappingMatchingChoosingQuestion(long id, String questionTitle, String description,
			float score, List<Long> optionIdList, List<MultipartFile> imageFileList, List<String> optionTextList)
			throws SizeLimitExceededException, IOException {
		// validate question data input
		String error = validateQuestionInput(questionTitle, description, score);

		try {
			Question question = iQuestionRepository.findByIdAndIsDisableFalse(id);
			if (question == null) {
				throw new ResourceNotFoundException();
			}
			// validate option data input
			for (long optionId : optionIdList) {
				OptionQuestion optionQuestion = iOptionQuestionRepository.findByIdAndIsDisableFalse(optionId);
				if (optionQuestion == null) {
					throw new ResourceNotFoundException();
				}
			}
			String optionError = validateSwappingMatchingChoosingOptionInput(imageFileList, optionTextList, "UPDATE");
			error += optionError;
			if (!error.isEmpty()) {

				return error.trim();
			}

			// first: update question
			updateQuestion(id, null, null, questionTitle, description, score);

			// last: update list option and return
			for (int i = 0; i < optionIdList.size(); i++) {
				iOptionsService.updateGameSwappingMatchingChoosingOptionQuestion(optionIdList.get(i),
						optionTextList.get(i), imageFileList.get(i));
			}
		} catch (Exception e) {
			logger.error("UPDATE: questionId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "UPDATE FAIL!";
		}

		return "UPDATE SUCCESS!";
	}

	// done ok
	@Override
	public String deleteQuestion(List<Long> ids) {
		for (long id : ids) {
			try {
				deleteOneQuestion(id);
			} catch (Exception e) {
				logger.error("DELETE: questionId = " + id + "! " + e.getMessage());
				if (e instanceof ResourceNotFoundException) {

					return "NOT FOUND!";
				}

				return "DELETE FAIL!";
			}
		}

		return "DELETE SUCCESS!";
	}

	// done ok
	@Override
	@Transactional
	public void deleteOneQuestion(long id) {
		// validate questionId
		try {
			Question question = iQuestionRepository.findByIdAndIsDisableFalse(id);
			if (question == null) {
				throw new ResourceNotFoundException();
			}

			// first: delete all active option by questionId
			List<OptionQuestion> optionQuestions = iOptionQuestionRepository.findByQuestionIdAndIsDisableFalse(id);
			for (OptionQuestion optionQuestion : optionQuestions) {
				iOptionsService.deleteOptionQuestion(optionQuestion.getId());
			}

			List<ExerciseGameQuestion> exerciseGameQuestionList = iExerciseGameQuestionRepository
					.findByQuestionIdAndIsDisableFalse(id);
			if (!exerciseGameQuestionList.isEmpty()) {
				for (ExerciseGameQuestion exerciseGameQuestion : exerciseGameQuestionList) {
					exerciseGameQuestion.setDisable(true);
					iExerciseGameQuestionRepository.save(exerciseGameQuestion);
				}
			}

			// last: delete question
			String questionImageUrl = question.getQuestionImageUrl();
			String questionAudioUrl = question.getQuestionAudioUrl();
			question.setDisable(true);
			question.setQuestionImageUrl(null);
			question.setQuestionAudioUrl(null);
			iQuestionRepository.save(question);
			if (questionImageUrl != null) {
				
				iFirebaseService.deleteFile(questionImageUrl);
			}
			if (questionAudioUrl != null) {
				iFirebaseService.deleteFile(questionAudioUrl);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private String validateQuestionInput(String questionTitle, String description, float score) {
		String error = Util.validateRequiredString(questionTitle, QUESTION_TITLE_LENGTH,
				"\nQuestion Title is invalid!");
		error += Util.validateString(description, DESCRIPTION_LENGTH, "\nDescription is invalid!");

		if (score <= 0) {
			error += "\nScore is invalid!";
		}

		return error;
	}

	private String validateExerciseOptionInput(List<String> optionTextList) {
		String optionError = "";
		for (String optionText : optionTextList) {
			optionError = Util.validateRequiredString(optionText, OPTION_TEXT_LENGTH, "\nOptionText is invalid!");
			if (!optionError.isEmpty()) {
				break;
			}
		}

		return optionError;
	}

	private String validateGameFillInBlankOptionInput(List<String> optionTextList, List<String> optionInputTypeList) {
		String optionError = "";
		for (int i = 0; i < optionTextList.size(); i++) {
			optionError = Util.validateRequiredString(optionTextList.get(i), OPTION_TEXT_LENGTH,
					"\nOptionText is invalid!");
			if (!optionError.isEmpty()) {
				break;
			}
		}

		return optionError;
	}

	private String validateSwappingMatchingChoosingOptionInput(List<MultipartFile> imageFileList,
			List<String> optionTextList, String action) {
		String optionError = "";
		for (int i = 0; i < optionTextList.size(); i++) {
			optionError = Util.validateRequiredString(optionTextList.get(i), OPTION_TEXT_LENGTH,
					"\nOptionText is invalid!");
			if (action.equalsIgnoreCase("CREATE")) {
				optionError += Util.validateRequiredFile(imageFileList.get(i), "image", "\nImageFile is invalid!",
						"\nNot supported this file type for image!");
			}
			if (action.equalsIgnoreCase("UPDATE")) {
				optionError += Util.validateFile(imageFileList.get(i), "image",
						"\nNot supported this file type for image!");
			}
			if (!optionError.isEmpty()) {
				break;
			}
		}

		return optionError;

	}

	@Transactional
	private long createQuestion(MultipartFile imageFile, MultipartFile audioFile, String questionTitle,
			String description, float score, long unitId, int questionTypeId)
			throws SizeLimitExceededException, IOException {
		QuestionType questionType = iQuestionTypeRepository.findById(questionTypeId)
				.orElseThrow(() -> new ResourceNotFoundException());
		Question question = new Question(questionTitle, description, score, unitId, false);
		if (imageFile != null) {
			question.setQuestionImageUrl(iFirebaseService.uploadFile(imageFile));
		}
		if (audioFile != null) {
			question.setQuestionAudioUrl(iFirebaseService.uploadFile(audioFile));
		}
		question.setQuestionType(questionType);
		iQuestionRepository.save(question);

		return question.getId();
	}

	@Transactional
	private void updateQuestion(long id, MultipartFile imageFile, MultipartFile audioFile, String questionTitle,
			String description, float score) throws SizeLimitExceededException, IOException {
		Question question = iQuestionRepository.findByIdAndIsDisableFalse(id);
		question.setQuestionTitle(questionTitle);
		question.setDescription(description);
		question.setScore(score);
		String questionImageUrl = question.getQuestionImageUrl();
		String questionAudioUrl = question.getQuestionAudioUrl();
		if (imageFile != null) {
			question.setQuestionImageUrl(iFirebaseService.uploadFile(imageFile));
			if (questionImageUrl != null) {
				if (!questionImageUrl.isEmpty()) {
					iFirebaseService.deleteFile(questionImageUrl);
				}
			}
		}
		if (audioFile != null) {
			question.setQuestionAudioUrl(iFirebaseService.uploadFile(audioFile));
			if (questionAudioUrl != null) {
				if (!questionAudioUrl.isEmpty()) {
					iFirebaseService.deleteFile(questionAudioUrl);
				}
			}
		}
		iQuestionRepository.save(question);

	}

}
