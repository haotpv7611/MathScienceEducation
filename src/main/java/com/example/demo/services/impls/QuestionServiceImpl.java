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

	@Override
	public Object findQuestionById(long id, String questionType) {
		Question question = iQuestionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());

		List<OptionQuestion> optionQuestionList = iOptionQuestionRepository.findByQuestionIdAndIsDisable(id, false);
		Object object = null;
		
		if (!questionType.equalsIgnoreCase(question.getQuestionType().getDescription())) {
			throw new ResourceNotFoundException();
		}
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
		
		if (questionType.equals("FILL")) {
			List<Object> optionQuestionFillDTOList = new ArrayList<>();
			if (!optionQuestionList.isEmpty()) {
				for (OptionQuestion optionQuestion : optionQuestionList) {
					OptionQuestionFillDTO optionQuestionFillDTO = modelMapper.map(optionQuestion,
							OptionQuestionFillDTO.class);
					optionQuestionFillDTOList.add(optionQuestionFillDTO);
				}
			}
			QuestionOptionResponseDTO questionExerciseResponseDTO = modelMapper.map(question,
					QuestionOptionResponseDTO.class);
			questionExerciseResponseDTO.setOptionQuestionDTOList(optionQuestionFillDTOList);
			object = questionExerciseResponseDTO;
		}
		
		if (questionType.equals("FILL")) {
			List<Object> optionQuestionFillDTOList = new ArrayList<>();
			if (!optionQuestionList.isEmpty()) {
				for (OptionQuestion optionQuestion : optionQuestionList) {
					OptionQuestionFillDTO optionQuestionFillDTO = modelMapper.map(optionQuestion,
							OptionQuestionFillDTO.class);
					optionQuestionFillDTOList.add(optionQuestionFillDTO);
				}
			}
			QuestionOptionResponseDTO questionExerciseResponseDTO = modelMapper.map(question,
					QuestionOptionResponseDTO.class);
			questionExerciseResponseDTO.setOptionQuestionDTOList(optionQuestionFillDTOList);
			object = questionExerciseResponseDTO;
		}
		
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

//	public QuestionExerciseResponseDTO findFillInBlankQuestionById(long id) {
//		Question question = iQuestionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
//
//		List<OptionQuestion> optionQuestionList = iOptionQuestionRepository.findByQuestionIdAndIsDisable(id, false);
//		List<OptionQuestionExerciseDTO> optionQuestionExerciseDTOList = new ArrayList<>();
//		if (!optionQuestionList.isEmpty()) {
//			for (OptionQuestion optionQuestion : optionQuestionList) {
//				OptionQuestionExerciseDTO optionQuestionExerciseDTO = modelMapper.map(optionQuestion,
//						OptionQuestionExerciseDTO.class);
//				optionQuestionExerciseDTOList.add(optionQuestionExerciseDTO);
//			}
//		}
//		QuestionExerciseResponseDTO questionExerciseResponseDTO = modelMapper.map(question,
//				QuestionExerciseResponseDTO.class);
//		questionExerciseResponseDTO.setOptionQuestionExerciseDTOList(optionQuestionExerciseDTOList);
//
//		return questionExerciseResponseDTO;
//	}

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

	@Override
	@Transactional
	public String deleteQuestion(List<Long> ids) {
		for (long id : ids) {
			Question question = iQuestionRepository.findByIdAndIsDisable(id, false);
			if (question == null) {
				throw new ResourceNotFoundException();
			}

			List<OptionQuestion> optionQuestions = iOptionQuestionRepository.findByQuestionIdAndIsDisable(id, false);
			for (OptionQuestion optionQuestion : optionQuestions) {
				iOptionsService.deleteOptionQuestion(optionQuestion.getId());
			}

			question.setDisable(true);
			iQuestionRepository.save(question);
		}

		return "DELETE SUCCESS !";
	}

	@Override
	@Transactional
	public String createExerciseQuestion(MultipartFile imageFile, MultipartFile audioFile, String questionTitle,
			String description, float score, String questionType, long unitId, List<String> optionTextList,
			List<Boolean> isCorrectList) throws SizeLimitExceededException, IOException {

		String error = validateQuestionInput(questionTitle, description, score);
		if (imageFile != null) {
			if (!imageFile.getContentType().contains("image")) {
				error += "\nNot supported this file type for image!";
			}
		}
		if (audioFile != null) {
			if (!audioFile.getContentType().contains("audio")) {
				error += "\nNot supported this file type for audio!";
			}
		}
		if (!questionType.equalsIgnoreCase(OPTION_TYPE_EXERCISE)) {
			throw new ResourceNotFoundException();
		}

		String optionError = validateExerciseOptionInput(optionTextList);
		error += optionError;
		if (!error.isEmpty()) {
			return error.trim();
		}

		int questionTypeId = 1;
		Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);
		if (unit == null) {
			throw new ResourceNotFoundException();
		}

		long questionId = createQuestion(imageFile, audioFile, questionTitle, description, score, unitId,
				questionTypeId);

		for (int i = 0; i < optionTextList.size(); i++) {
			iOptionsService.createExerciseOptionQuestion(questionId, optionTextList.get(i), isCorrectList.get(i));
		}

		return "CREATE SUCCESS !";
	}

	@Override
	@Transactional
	public String createGameFillInBlankQuestion(MultipartFile imageFile, MultipartFile audioFile, String questionTitle,
			String description, float score, String questionType, long unitId, List<String> optionTextList,
			List<String> optionInputTypeList) throws SizeLimitExceededException, IOException {

		String error = validateQuestionInput(questionTitle, description, score);
		if (imageFile != null) {
			if (!imageFile.getContentType().contains("image")) {
				error += "\nNot supported this file type for image!";
			}
		}
		if (audioFile != null) {
			if (!audioFile.getContentType().contains("audio")) {
				error += "\nNot supported this file type for audio!";
			}
		}
		if (!questionType.equalsIgnoreCase(OPTION_TYPE_FILL_IN_BLANK)) {
			throw new ResourceNotFoundException();
		}

		String optionError = validateGameFillInBlankOptionInput(optionTextList, optionInputTypeList);
		error += optionError;
		if (!error.isEmpty()) {
			return error.trim();
		}

		int questionTypeId = 2;
		Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);
		if (unit == null) {
			throw new ResourceNotFoundException();
		}

		long questionId = createQuestion(imageFile, audioFile, questionTitle, description, score, unitId,
				questionTypeId);
		for (int i = 0; i < optionTextList.size(); i++) {
			iOptionsService.createGameFillInBlankOptionQuestion(questionId, optionTextList.get(i),
					optionInputTypeList.get(i));
		}

		return "CREATE SUCCESS !";
	}

	@Override
	@Transactional
	public String createGameSwappingMatchingChoosingQuestion(String questionTitle, String description, float score,
			String questionType, long unitId, List<MultipartFile> imageFileList, List<String> optionTextList)
			throws SizeLimitExceededException, IOException {
		String error = validateQuestionInput(questionTitle, description, score);
		List<String> questionTypeList = new ArrayList<>();
		questionTypeList.addAll(Arrays.asList("MATCH", "SWAP", "CHOOSE"));
		if (questionTypeList.contains(questionType) == false) {
			throw new ResourceNotFoundException();
		}

		String optionError = validateSwappingMatchingChoosingOptionInput(imageFileList, optionTextList);
		error += optionError;
		if (!error.isEmpty()) {
			return error.trim();
		}

		int questionTypeId = questionTypeList.indexOf(questionType) + 3;
		Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);
		if (unit == null) {
			throw new ResourceNotFoundException();
		}

		long questionId = createQuestion(null, null, questionTitle, description, score, unitId, questionTypeId);
		for (int i = 0; i < optionTextList.size(); i++) {
			iOptionsService.createGameSwappingMatchingChoosingOptionQuestion(questionId, optionTextList.get(i),
					imageFileList.get(i));
		}

		return "CREATE SUCCESS !";
	}

	@Override
	public String updateQuestion(long id, String questionTitle, String description, MultipartFile multipartImage,
			MultipartFile multipartAudio, float score, long unitId) throws SizeLimitExceededException, IOException {
		String error = "";
		Question question = iQuestionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
		if (questionTitle != null) {
			if (questionTitle.length() > QUESTION_TITLE_LENGTH) {
				error += "\n Question Title is invalid !";
			}
		}
		if (description != null) {
			if (description.length() > DESCRIPTION_LENGTH) {
				error += "\n Question Text is invalid !";
			}
		}
		if (multipartImage != null) {
			if (multipartImage.getContentType().contains("image")) {
				error += "\n Not supported this file type for image!";
			}
		}
		if (multipartAudio != null) {
			if (multipartAudio.getContentType().contains("image")) {
				error += "\n Not supported this file type for image!";
			}
		}

		if (!error.isEmpty()) {
			return error.trim();
		}
		if (questionTitle != null) {
			question.setQuestionTitle(questionTitle);
		}
		if (description != null) {
			question.setDescription(description);
		}
		if (multipartImage != null) {
			question.setQuestionImageUrl(firebaseService.saveFile(multipartImage));
		}
		if (multipartAudio != null) {
			question.setQuestionAudioUrl(firebaseService.saveFile(multipartAudio));
		}
		question.setScore(score);
		question.setUnitId(unitId);
		iQuestionRepository.save(question);
		return "UPDATE SUCCESS !";
	}

	@Override
	public List<QuestionResponseDTO> findAllByUnitId(long unitId, boolean isExercise) {
		Unit unit = iUnitRepository.findByIdAndIsDisableFalse(unitId);
		if (unit == null) {
			throw new ResourceNotFoundException();
		}

		List<Question> questionList = null;
		List<QuestionResponseDTO> questionDTOList = new ArrayList<>();
		if (isExercise) {
			questionList = iQuestionRepository.findByUnitIdAndQuestionTypeIdAndIsDisable(unitId, 1, false);
		} else {
			questionList = iQuestionRepository.findByUnitIdAndQuestionTypeIdNotAndIsDisable(unitId, 1, false);
		}

		for (Question question : questionList) {
			QuestionResponseDTO questionResponseDTO = modelMapper.map(question, QuestionResponseDTO.class);
			questionResponseDTO.setQuestionType(question.getQuestionType().getDescription());
			questionDTOList.add(questionResponseDTO);
		}

		return questionDTOList;
	}

	private String validateQuestionInput(String questionTitle, String description, float score) {
		String error = "";

		error += checkNullAndLength(questionTitle, QUESTION_TITLE_LENGTH, "\nQuestion Title is invalid!");
		if (!description.isEmpty()) {
			if (description.length() > DESCRIPTION_LENGTH) {
				error += "\nDescription is invalid!";
			}
		}

		if (score <= 0) {
			error += "\nScore is invalid!";
		}

		return error;
	}

	private String validateExerciseOptionInput(List<String> optionTextList) {
		String optionError = "";
		for (String optionText : optionTextList) {
			optionError += checkNullAndLength(optionText, OPTION_TEXT_LENGTH, "\nOptionText is invalid!");
			if (!optionError.isEmpty()) {
				break;
			}
		}

		return optionError;
	}

	private String validateGameFillInBlankOptionInput(List<String> optionTextList, List<String> optionInputTypeList) {
		String optionError = "";
		for (int i = 0; i < optionTextList.size(); i++) {
			optionError += checkNullAndLength(optionTextList.get(i), OPTION_TEXT_LENGTH, "\nOptionText is invalid!");
			optionError += checkNullAndLength(optionInputTypeList.get(i), OPTION_INPUT_TYPE_LENGTH,
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
			optionError += checkNullAndLength(optionTextList.get(i), OPTION_TEXT_LENGTH, "\nOptionText is invalid!");
			if (imageFileList.get(i) == null) {
				optionError += "\nImageFile is invalid!";
			} else {
				if (!imageFileList.get(i).getContentType().contains("image")) {
					optionError += "\nNot supported this file type for image!";
				}
			}
			if (!optionError.isEmpty()) {
				break;
			}
		}

		return optionError;
	}

	private long createQuestion(MultipartFile imageFile, MultipartFile audioFile, String questionTitle,
			String description, float score, long unitId, int questionTypeId)
			throws SizeLimitExceededException, IOException {

		Question question = new Question(questionTitle, description, score, unitId, false);
		if (imageFile != null) {
			question.setQuestionImageUrl(firebaseService.saveFile(imageFile));
		}
		if (audioFile != null) {
			question.setQuestionAudioUrl(firebaseService.saveFile(audioFile));
		}
		QuestionType questionType = iQuestionTypeRepository.findById(questionTypeId)
				.orElseThrow(() -> new ResourceNotFoundException());
		question.setQuestionType(questionType);
		iQuestionRepository.save(question);

		return question.getId();
	}

	private String checkNullAndLength(String property, int length, String errorMessage) {
		String error = "";
		if (property == null) {
			error += "\nOptionText is invalid!";
		} else {
			if (property.length() > OPTION_TEXT_LENGTH) {
				error += "\nOptionText is invalid!";
			}
		}

		return error;
	}

}
