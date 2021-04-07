package com.example.demo.dtos;

import java.util.List;

public class QuestionViewDTO {
	private long id;
	private String questionTitle;
	private String description;
	private String questionImageUrl;
	private String questionAudioUrl;
	private float score;
	private List<OptionQuestionDTO> optionList;

	public QuestionViewDTO() {
	}

	public QuestionViewDTO(long id, String questionTitle, String description, String questionImageUrl,
			String questionAudioUrl, float score, List<OptionQuestionDTO> optionList) {
		super();
		this.id = id;
		this.questionTitle = questionTitle;
		this.description = description;
		this.questionImageUrl = questionImageUrl;
		this.questionAudioUrl = questionAudioUrl;
		this.score = score;
		this.optionList = optionList;
	}

	public QuestionViewDTO(long id, String questionTitle, String description, String questionImageUrl,
			String questionAudioUrl, float score) {
		super();
		this.id = id;
		this.questionTitle = questionTitle;
		this.description = description;
		this.questionImageUrl = questionImageUrl;
		this.questionAudioUrl = questionAudioUrl;
		this.score = score;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the questionText
	 */

	/**
	 * @return the questionImageUrl
	 */
	public String getQuestionImageUrl() {
		return questionImageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param questionImageUrl the questionImageUrl to set
	 */
	public void setQuestionImageUrl(String questionImageUrl) {
		this.questionImageUrl = questionImageUrl;
	}

	/**
	 * @return the questionAudioUrl
	 */
	public String getQuestionAudioUrl() {
		return questionAudioUrl;
	}

	/**
	 * @param questionAudioUrl the questionAudioUrl to set
	 */
	public void setQuestionAudioUrl(String questionAudioUrl) {
		this.questionAudioUrl = questionAudioUrl;
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
	 * @return the option
	 */
	public List<OptionQuestionDTO> getOptionList() {
		return optionList;
	}

	/**
	 * @param option the option to set
	 */
	public void setOptionList(List<OptionQuestionDTO> optionList) {
		this.optionList = optionList;
	}

}
