package com.example.demo.services.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.OptionQuestionDTO;
import com.example.demo.dtos.OptionQuestionExerciseDTO;
import com.example.demo.dtos.OptionQuestionFillDTO;
import com.example.demo.dtos.OptionQuestionGameDTO;
import com.example.demo.dtos.QuestionOptionResponseDTO;
import com.example.demo.dtos.QuestionResponseDTO;
import com.example.demo.dtos.QuestionViewDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.OptionQuestion;
import com.example.demo.models.Question;
import com.example.demo.models.QuestionType;
import com.example.demo.models.Unit;
import com.example.demo.repositories.IOptionQuestionRepository;
import com.example.demo.repositories.IQuestionRepository;
import com.example.demo.repositories.IQuestionTypeRepository;
import com.example.demo.repositories.IUnitRepository;
import com.example.demo.services.IExerciseGameQuestionService;
import com.example.demo.services.IOptionQuestionService;
import com.example.demo.services.IQuestionService;

@Service
public class QuestionServiceImpl implements IQuestionService {

	private final int QUESTION_TITLE_LENGTH = 50;
	private final int DESCRIPTION_LENGTH = 250;
	private final int OPTION_TEXT_LENGTH = 50;
	private final int OPTION_INPUT_TYPE_LENGTH = 50;
	private final String OPTION_TYPE_EXERCISE = "EXERCISE";
	private final String OPTION_TYPE_FILL_IN_BLANK = "FILL";

	@Autowired
	private IQuestionRepository iQuestionRepository;

	@Autowired
	private IExerciseGameQuestionService iExerciseGameQuestionService;

	@Autowired
	private FirebaseService firebaseService;

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

	// done
	@Override
	public Object findQuestionById(long id, String questionType) {
		// validate data input
		Question question = iQuestionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
		if (!questionType.equalsIgnoreCase(question.getQuestionType().getDescription())) {
			throw new ResourceNotFoundException();
		}

		// get all active option by questionId = ?
		List<OptionQuestion> optionQuestionList = iOptionQuestionRepository.findByQuestionIdAndIsDisableFalse(id);
		Object object = null;

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
					optionQuestionFillDTO.setOptionInputType(optionQuestion.getOptionInputType());
					optionQuestionFillDTOList.add(optionQuestionFillDTO);
				}
			}
			QuestionOptionResponseDTO questionExerciseResponseDTO = modelMapper.map(question,
					QuestionOptionResponseDTO.class);
			questionExerciseResponseDTO.setOptionQuestionDTOList(optionQuestionFillDTOList);
			object = questionExerciseResponseDTO;
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
			QuestionOptionResponseDTO questionExerciseResponseDTO = modelMapper.map(question,
					QuestionOptionResponseDTO.class);
			questionExerciseResponseDTO.setOptionQuestionDTOList(optionQuestionGameDTOList);
			object = questionExerciseResponseDTO;
		}

		return object;
	}

//	@Override
//	public List<QuestionDTO> findByExerciseId (Long exerciseId) {
//
//		List<Long> listQuestionIds = iExerciseGameQuestionService.findQuestionIdByExerciseId(exerciseId);
//
//		List<QuestionDTO> listQuestionDTOs = new ArrayList<>();
//
//		if (listQuestionIds.size() > 0) {
//			for (int i = 0; i < listQuestionIds.size(); i++) {
//
//				listQuestions.add(
//						iQuestionRepository.findById(listQuestionIds.get(i)).orElseThrow(() -> new ResourceNotFoundException()));
//			}
//		}
//
//		return listQuestions;
//	}

	@Override
	public List<QuestionViewDTO> showQuestionByExerciseId(long exerciseId) {
		// get list questionId by exerciseGame
		List<Long> questionIdList = iExerciseGameQuestionService.findAllQuestionByExerciseId(exerciseId);

		List<QuestionViewDTO> questionViewDTOList = new ArrayList<>();

//		if (!questionIdList.isEmpty()) {		
//			List<Question> questionList = iQuestionRepository.findAllQuestionByListIdAndIsDisable(questionIdList);
//		
//			if (!questionList.isEmpty()) {
//				for (Question question : questionList) {
//					List<OptionsDTO> optionsList = iOptionsService.findByQuestionId(question.getId());
//					if (!optionsList.isEmpty()) {
//						QuestionViewDTO questionViewDTO = new QuestionViewDTO(question.getId(),
//								question.getQuestionText(), question.getQuestionImageUrl(),
//								question.getQuestionAudioUrl(), question.getScore(), optionsList);
//						questionViewDTOList.add(questionViewDTO);
//					}
//				}
//			}
//		}
		generateQuestionView(questionIdList, questionViewDTOList);
		return questionViewDTOList;
	}

	@Override
	public List<QuestionViewDTO> showQuestionByGameId(long gameId) {
		List<Long> questionIdList = iExerciseGameQuestionService.findAllQuestionByGameId(gameId);
		List<QuestionViewDTO> questionViewDTOList = new ArrayList<>();
		generateQuestionView(questionIdList, questionViewDTOList);
		return null;
	}

	public void generateQuestionView(List<Long> questionIdList, List<QuestionViewDTO> questionViewDTOList) {
		if (!questionIdList.isEmpty()) {
			List<Question> questionList = iQuestionRepository.findAllQuestionByListIdAndIsDisable(questionIdList);

			if (!questionList.isEmpty()) {
				for (Question question : questionList) {
					List<OptionQuestionDTO> optionsList = iOptionsService.findByQuestionId(question.getId());
					if (!optionsList.isEmpty()) {
						QuestionViewDTO questionViewDTO = new QuestionViewDTO(question.getId(),
								question.getQuestionTitle(), question.getDescription(), question.getQuestionImageUrl(),
								question.getQuestionAudioUrl(), question.getScore(), optionsList);
						questionViewDTOList.add(questionViewDTO);
					}
				}
			}
		}
	}

	// done
	@Override
	@Transactional
	public String deleteQuestion(List<Long> ids) {
		for (long id : ids) {
			// validate data input
			Question question = iQuestionRepository.findByIdAndIsDisableFalse(id);
			if (question == null) {
				throw new ResourceNotFoundException();
			}

			// first: delete all active option by questionId
			List<OptionQuestion> optionQuestions = iOptionQuestionRepository.findByQuestionIdAndIsDisableFalse(id);
			for (OptionQuestion optionQuestion : optionQuestions) {
				iOptionsService.deleteOptionQuestion(optionQuestion.getId());
			}

			// last: delete question and return
			question.setDisable(true);
			iQuestionRepository.save(question);
		}

		return "DELETE SUCCESS !";
	}

	// done
	@Override
	@Transactional
	public String createExerciseQuestion(MultipartFile imageFile, MultipartFile audioFile, String questionTitle,
			String description, float score, String questionType, long unitId, List<String> optionTextList,
			List<Boolean> isCorrectList) throws SizeLimitExceededException, IOException {
		// validate question data input
		if (!questionType.equalsIgnoreCase(OPTION_TYPE_EXERCISE)) {
			throw new ResourceNotFoundException();
		}
		Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);
		if (unit == null) {
			throw new ResourceNotFoundException();
		}
		String error = validateQuestionInput(questionTitle, description, score);
		error += validateFile(imageFile, "image", "\nNot supported this file type for image!");
		error += validateFile(audioFile, "audio", "\nNot supported this file type for audio!");

		// validate option data input
		String optionError = validateExerciseOptionInput(optionTextList);
		error += optionError;
		if (!error.isEmpty()) {
			return error.trim();
		}

		// first: create question
		int questionTypeId = 1;
		long questionId = createQuestion(imageFile, audioFile, questionTitle, description, score, unitId,
				questionTypeId);

		// last: create list option and return
		for (int i = 0; i < optionTextList.size(); i++) {
			iOptionsService.createExerciseOptionQuestion(questionId, optionTextList.get(i), isCorrectList.get(i));
		}

		return "CREATE SUCCESS!";
	}

	// done
	@Override
	@Transactional
	public String createGameFillInBlankQuestion(MultipartFile imageFile, MultipartFile audioFile, String questionTitle,
			String description, float score, String questionType, long unitId, List<String> optionTextList,
			List<String> optionInputTypeList) throws SizeLimitExceededException, IOException {
		// validate question data input
		Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);
		if (unit == null) {
			throw new ResourceNotFoundException();
		}
		if (!questionType.equalsIgnoreCase(OPTION_TYPE_FILL_IN_BLANK)) {
			throw new ResourceNotFoundException();
		}
		String error = validateQuestionInput(questionTitle, description, score);
		error += validateFile(imageFile, "image", "\nNot supported this file type for image!");
		error += validateFile(audioFile, "audio", "\nNot supported this file type for audio!");

		// validate option data input
		String optionError = validateGameFillInBlankOptionInput(optionTextList, optionInputTypeList);
		error += optionError;
		if (!error.isEmpty()) {
			return error.trim();
		}

		// first: create question
		int questionTypeId = 2;
		long questionId = createQuestion(imageFile, audioFile, questionTitle, description, score, unitId,
				questionTypeId);

		// last: create list option and return
		for (int i = 0; i < optionTextList.size(); i++) {
			iOptionsService.createGameFillInBlankOptionQuestion(questionId, optionTextList.get(i),
					optionInputTypeList.get(i));
		}

		return "CREATE SUCCESS!";
	}

	// done
	@Override
	@Transactional
	public String createGameSwappingMatchingChoosingQuestion(String questionTitle, String description, float score,
			String questionType, long unitId, List<MultipartFile> imageFileList, List<String> optionTextList)
			throws SizeLimitExceededException, IOException {
		// validate question data input
		Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);
		if (unit == null) {
			throw new ResourceNotFoundException();
		}
		List<String> questionTypeList = new ArrayList<>();
		questionTypeList.addAll(Arrays.asList("MATCH", "SWAP", "CHOOSE"));
		if (questionTypeList.contains(questionType) == false) {
			throw new ResourceNotFoundException();
		}
		String error = validateQuestionInput(questionTitle, description, score);

		// validate option data input
		String optionError = validateSwappingMatchingChoosingOptionInput(imageFileList, optionTextList);
		error += optionError;
		if (!error.isEmpty()) {
			return error.trim();
		}

		// first: create question
		int questionTypeId = questionTypeList.indexOf(questionType) + 3;
		long questionId = createQuestion(null, null, questionTitle, description, score, unitId, questionTypeId);

		// last: create list option and return
		for (int i = 0; i < optionTextList.size(); i++) {
			iOptionsService.createGameSwappingMatchingChoosingOptionQuestion(questionId, optionTextList.get(i),
					imageFileList.get(i));
		}

		return "CREATE SUCCESS !";
	}

	// done
	@Override
	@Transactional
	public String updateExerciseQuestion(long id, MultipartFile imageFile, MultipartFile audioFile,
			String questionTitle, String description, float score, List<Long> optionIdList, List<String> optionTextList,
			List<Boolean> isCorrectList) throws SizeLimitExceededException, IOException {
		// validate question data input
		Question question = iQuestionRepository.findByIdAndIsDisableFalse(id);
		if (question == null) {
			throw new ResourceNotFoundException();
		}
		String error = validateQuestionInput(questionTitle, description, score);
		error += validateFile(imageFile, "image", "\nNot supported this file type for image!");
		error += validateFile(audioFile, "audio", "\nNot supported this file type for audio!");

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

		return "UPDATE SUCCESS!";
	}

	// done
	@Override
	@Transactional
	public String updateGameFillInBlankQuestion(long id, MultipartFile imageFile, MultipartFile audioFile,
			String questionTitle, String description, float score, List<Long> optionIdList, List<String> optionTextList,
			List<String> optionInputTypeList) throws SizeLimitExceededException, IOException {
		// validate question data input
		Question question = iQuestionRepository.findByIdAndIsDisableFalse(id);
		if (question == null) {
			throw new ResourceNotFoundException();
		}
		String error = validateQuestionInput(questionTitle, description, score);
		error += validateFile(imageFile, "image", "\nNot supported this file type for image!");
		error += validateFile(audioFile, "audio", "\nNot supported this file type for audio!");

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
		updateQuestion(id, imageFile, audioFile, questionTitle, description, score);

		// last: update list option and return
		for (int i = 0; i < optionIdList.size(); i++) {
			iOptionsService.updateGameFillInBlankOptionQuestion(optionIdList.get(i), optionTextList.get(i),
					optionInputTypeList.get(i));
		}

		return "UPDATE SUCCESS!";
	}

	// done
	@Override
	@Transactional
	public String updateGameSwappingMatchingChoosingQuestion(long id, String questionTitle, String description,
			float score, List<Long> optionIdList, List<MultipartFile> imageFileList, List<String> optionTextList)
			throws SizeLimitExceededException, IOException {
		// validate question data input
		Question question = iQuestionRepository.findByIdAndIsDisableFalse(id);
		if (question == null) {
			throw new ResourceNotFoundException();
		}
		String error = validateQuestionInput(questionTitle, description, score);

		// validate option data input
		for (long optionId : optionIdList) {
			OptionQuestion optionQuestion = iOptionQuestionRepository.findByIdAndIsDisableFalse(optionId);
			if (optionQuestion == null) {
				throw new ResourceNotFoundException();
			}
		}
		String optionError = validateSwappingMatchingChoosingOptionInput(imageFileList, optionTextList);
		error += optionError;
		if (!error.isEmpty()) {
			return error.trim();
		}

		// first: update question
		updateQuestion(id, null, null, questionTitle, description, score);

		// last: update list option and return
		for (int i = 0; i < optionIdList.size(); i++) {
			iOptionsService.updateGameSwappingMatchingChoosingOptionQuestion(optionIdList.get(i), optionTextList.get(i),
					imageFileList.get(i));
		}

		return "UPDATE SUCCESS!";
	}

	// done
	@Override
	public List<QuestionResponseDTO> findAllByUnitId(long unitId, boolean isExercise) {
		// validate data input
		Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);
		if (unit == null) {
			throw new ResourceNotFoundException();
		}

		// find all active question by unitId = ? and is exerciseQuestion or is
		// gameQuestion
		List<Question> questionList = null;
		if (isExercise) {
			questionList = iQuestionRepository.findByUnitIdAndQuestionTypeIdAndIsDisable(unitId, 1, false);
		} else {
			questionList = iQuestionRepository.findByUnitIdAndQuestionTypeIdNotAndIsDisable(unitId, 1, false);
		}

		// convert to DTO and return
		List<QuestionResponseDTO> questionDTOList = new ArrayList<>();
		for (Question question : questionList) {
			QuestionResponseDTO questionResponseDTO = modelMapper.map(question, QuestionResponseDTO.class);
			questionResponseDTO.setQuestionType(question.getQuestionType().getDescription());
			questionDTOList.add(questionResponseDTO);
		}

		return questionDTOList;
	}

	private String validateQuestionInput(String questionTitle, String description, float score) {
		String error = validateRequiredString(questionTitle, QUESTION_TITLE_LENGTH, "\nQuestion Title is invalid!");
		error += validateString(description, DESCRIPTION_LENGTH, "\nDescription is invalid!");

		if (score <= 0) {
			error += "\nScore is invalid!";
		}

		return error;
	}

	private String validateExerciseOptionInput(List<String> optionTextList) {
		String optionError = "";
		for (String optionText : optionTextList) {
			optionError = validateRequiredString(optionText, OPTION_TEXT_LENGTH, "\nOptionText is invalid!");
			if (!optionError.isEmpty()) {
				break;
			}
		}

		return optionError;
	}

	private String validateGameFillInBlankOptionInput(List<String> optionTextList, List<String> optionInputTypeList) {
		String optionError = "";
		for (int i = 0; i < optionTextList.size(); i++) {
			optionError = validateRequiredString(optionTextList.get(i), OPTION_TEXT_LENGTH, "\nOptionText is invalid!");
			optionError += validateRequiredString(optionInputTypeList.get(i), OPTION_INPUT_TYPE_LENGTH,
					"\nOptionInputType is invalid!");
			if (!optionError.isEmpty()) {
				break;
			}
		}

		return optionError;
	}

	private String validateSwappingMatchingChoosingOptionInput(List<MultipartFile> imageFileList,
			List<String> optionTextList) {
		String optionError = "";
		for (int i = 0; i < optionTextList.size(); i++) {
			optionError = validateRequiredString(optionTextList.get(i), OPTION_TEXT_LENGTH, "\nOptionText is invalid!");
			optionError += validateRequiredFile(imageFileList.get(i), "image", "\nImageFile is invalid!",
					"\nNot supported this file type for image!");
			if (!optionError.isEmpty()) {
				break;
			}
		}

		return optionError;
	}

	private long createQuestion(MultipartFile imageFile, MultipartFile audioFile, String questionTitle,
			String description, float score, long unitId, int questionTypeId)
			throws SizeLimitExceededException, IOException {
		QuestionType questionType = iQuestionTypeRepository.findById(questionTypeId)
				.orElseThrow(() -> new ResourceNotFoundException());
		Question question = new Question(questionTitle, description, score, unitId, false);
		if (imageFile != null) {
			question.setQuestionImageUrl(firebaseService.saveFile(imageFile));
		}
		if (audioFile != null) {
			question.setQuestionAudioUrl(firebaseService.saveFile(audioFile));
		}
		question.setQuestionType(questionType);
		iQuestionRepository.save(question);

		return question.getId();
	}

	private void updateQuestion(long id, MultipartFile imageFile, MultipartFile audioFile, String questionTitle,
			String description, float score) throws SizeLimitExceededException, IOException {
		Question question = iQuestionRepository.findByIdAndIsDisableFalse(id);
		question.setQuestionTitle(questionTitle);
		question.setDescription(description);
		question.setScore(score);
		if (imageFile != null) {
			question.setQuestionImageUrl(firebaseService.saveFile(imageFile));
		}
		if (audioFile != null) {
			question.setQuestionAudioUrl(firebaseService.saveFile(audioFile));
		}
		iQuestionRepository.save(question);
	}

	private String validateString(String property, int length, String errorMessage) {
		String error = "";
		if (property != null) {
			if (property.length() > length) {
				error = errorMessage;
			}
		}

		return error;
	}

	private String validateRequiredString(String property, int length, String errorMessage) {
		String error = "";
		if (property == null) {
			error = errorMessage;
		} else {
			if (property.isEmpty() || property.length() > length) {
				error = errorMessage;
			}
		}

		return error;
	}

	private String validateFile(MultipartFile file, String contentType, String errorMessage) {
		String error = "";
		if (file != null) {
			if (!file.getContentType().contains(contentType)) {
				error += errorMessage;
			}
		}

		return error;
	}

	private String validateRequiredFile(MultipartFile file, String contentType, String errorMessage,
			String errorMessage2) {
		String error = "";
		if (file == null) {
			error = errorMessage;
		} else {
			if (file.isEmpty()) {
				error = errorMessage;
			} else if (!file.getContentType().contains(contentType)) {
				error += errorMessage2;
			}
		}

		return error;
	}
}
