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
import com.example.demo.dtos.QuestionDTO;
import com.example.demo.dtos.QuestionViewDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Question;
import com.example.demo.repositories.IQuestionRepository;
import com.example.demo.services.IExerciseGameQuestionService;
import com.example.demo.services.IOptionQuestionService;
import com.example.demo.services.IQuestionService;

@Service
public class QuestionServiceImpl implements IQuestionService {

	private final int QUESTION_TITLE = 50;
	private final int QUESTION_TEXT = 250;

	@Autowired
	private IQuestionRepository iQuestionRepository;

	@Autowired
	private IExerciseGameQuestionService iExerciseGameQuestionService;

	@Autowired
	private FirebaseService firebaseService;

	@Autowired
	private ModelMapper modelMapper;
//	@Autowired
//	private IQuestionService iQuestionService;
//	
	@Autowired
	private IOptionQuestionService iOptionsService;

//	@Override
//	public Question findOneById(Long id) {
//
//		return iQuestionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
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
								question.getQuestionText(), question.getQuestionImageUrl(),
								question.getQuestionAudioUrl(), question.getScore(), optionsList);
						questionViewDTOList.add(questionViewDTO);
					}
				}
			}
		}
	}

	@Override
	public String deleteQuestion(long id) {
		Question question = iQuestionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
		question.setDisable(true);
		iQuestionRepository.save(question);
		return "DELETE SUCCESS !";
	}

	@Override
	public String createQuestion(String questionTitle, String questionText, MultipartFile multipartImage,
			MultipartFile multipartAudio, float score, long questionTypeId, long unitId)
			throws SizeLimitExceededException, IOException {
		String error = "";
		if (questionTitle.isEmpty() || questionTitle.length() > QUESTION_TITLE) {
			error += "\n Question Title is invalid !";
		}
		if (questionText.isEmpty() || questionText.length() > QUESTION_TEXT) {
			error += "\n Question Text is invalid !";
		}
		if (multipartImage.getContentType().contains("image")) {
			error += "\n Not supported this file type for image!";
		}
		if (multipartAudio.getContentType().contains("image")) {
			error += "\n Not supported this file type for image!";
		}
		if (score == 0) {
			error += "\n Score is invalid !";
		}
		if (!error.isEmpty()) {
			return error.trim();
		}
		if (questionTypeId == 1) {
			Question question = new Question();
			question.setQuestionTitle(questionTitle);
			question.setQuestionText(questionText);
			question.setQuestionImageUrl(firebaseService.saveFile(multipartImage));
			question.setQuestionAudioUrl(firebaseService.saveFile(multipartAudio));
			question.setScore(score);
			question.setUnitId(unitId);
			question.setQuestionTypeId(1);
			question.setDisable(false);
			iQuestionRepository.save(question);
		}
		if (questionTypeId != 1) {
			Question question = new Question();
			question.setQuestionTitle(questionTitle);
			question.setQuestionText(questionText);
			question.setQuestionImageUrl(firebaseService.saveFile(multipartImage));
			question.setQuestionAudioUrl(firebaseService.saveFile(multipartAudio));
			question.setScore(score);
			question.setUnitId(unitId);
			question.setQuestionTypeId(questionTypeId);
			question.setDisable(false);
			iQuestionRepository.save(question);
		}

		return "CREATE SUCCESS !";
	}

	@Override
	public String updateQuestion(long id, String questionTitle, String questionText, MultipartFile multipartImage,
			MultipartFile multipartAudio, float score, long unitId) throws SizeLimitExceededException, IOException {
		String error = "";
		Question question = iQuestionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
		if (questionTitle != null) {
			if (questionTitle.length() > QUESTION_TITLE) {
				error += "\n Question Title is invalid !";
			}
		}
		if (questionText != null) {
			if(questionText.length() > QUESTION_TEXT) {
				error += "\n Question Text is invalid !";
			}			
		}
		
		if (multipartImage.getContentType().contains("image")) {
			error += "\n Not supported this file type for image!";
		}
		if (multipartAudio.getContentType().contains("image")) {
			error += "\n Not supported this file type for image!";
		}
		if (score == 0) {
			error += "\n Score is invalid !";
		}
		if (!error.isEmpty()) {
			return error.trim();
		}
		question.setQuestionTitle(questionTitle);
		question.setQuestionText(questionText);
		question.setQuestionImageUrl(firebaseService.saveFile(multipartImage));
		question.setQuestionAudioUrl(firebaseService.saveFile(multipartAudio));
		question.setScore(score);
		question.setUnitId(unitId);
		iQuestionRepository.save(question);
		return "UPDATE SUCCESS !";
	}

	@Override
	public List<QuestionDTO> findByUnitIdAndIsDisable(long unitId, boolean isDisable) {
		List<Question> questionLists = iQuestionRepository.findByUnitIdAndIsDisable(unitId, isDisable);
		List<QuestionDTO> questionDTOLists = new ArrayList<>();
		for (Question question : questionLists) {
			QuestionDTO questionDTO = modelMapper.map(question, QuestionDTO.class);
			questionDTOLists.add(questionDTO);
		}
		return questionDTOLists;
	}

}
