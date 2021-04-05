package com.example.demo.dtos;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

public class QuestionRequestDTO {
//	@NotNull(message = "questionTitle must be not null!")
//	@Length(max = 30, message = "questionTitle length must be less than 30!")
//	private String questionTitle;
//	@Length(max = 30, message = "description length must be less than 30!")
//	private String description;
//	private float score;
//	@NotNull(message = "FirstName must be not null!")
//	@Length(max = 30, message = "questionType length must be less than 30!")
//	private String questionType;
//	private long unitId;
//	private List<OptionQuestionDTO> optionQuestionList;

	@NotNull(message = "questionTitle must be not null!")
	@Length(max = 30, message = "questionTitle length must be less than 30!")
	private String questionTitle;
	@Length(max = 30, message = "description length must be less than 30!")
	private String description;
	private MultipartFile imageFile;
	private MultipartFile audioFile;
	private float score;
	@NotNull(message = "FirstName must be not null!")
	@Length(max = 30, message = "questionType length must be less than 30!")
	private String questionType;
	private long unitId;
	private List<OptionQuestionDTO> optionQuestionList;

	/**
	 * @return the questionTitle
	 */
	public String getQuestionTitle() {
		return questionTitle;
	}

	/**
	 * @param questionTitle the questionTitle to set
	 */
	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the score
	 */
	public float getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(float score) {
		this.score = score;
	}

	/**
	 * @return the questionType
	 */
	public String getQuestionType() {
		return questionType;
	}

	/**
	 * @param questionType the questionType to set
	 */
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	/**
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}

	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}

	/**
	 * @return the optionList
	 */
	public List<OptionQuestionDTO> getOptionQuestionList() {
		return optionQuestionList;
	}

	/**
	 * @param optionList the optionList to set
	 */
	public void setOptionQuestionList(List<OptionQuestionDTO> optionQuestionList) {
		this.optionQuestionList = optionQuestionList;
	}

	/**
	 * @return the imageFile
	 */
	public MultipartFile getImageFile() {
		return imageFile;
	}

	/**
	 * @param imageFile the imageFile to set
	 */
	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	/**
	 * @return the audioFile
	 */
	public MultipartFile getAudioFile() {
		return audioFile;
	}

	/**
	 * @param audioFile the audioFile to set
	 */
	public void setAudioFile(MultipartFile audioFile) {
		this.audioFile = audioFile;
	}

}
