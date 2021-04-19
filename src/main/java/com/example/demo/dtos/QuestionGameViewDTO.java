package com.example.demo.dtos;

import java.util.List;

public class QuestionGameViewDTO {
	private String questionType;
	private String questionTitle;
	private String questionImageUrl;
	private List<Object> optionQuestion;

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
	 * @return the questionImageUrl
	 */
	public String getQuestionImageUrl() {
		return questionImageUrl;
	}

	/**
	 * @param questionImageUrl the questionImageUrl to set
	 */
	public void setQuestionImageUrl(String questionImageUrl) {
		this.questionImageUrl = questionImageUrl;
	}

	/**
	 * @return the optionQuestion
	 */
	public List<Object> getOptionQuestion() {
		return optionQuestion;
	}

	/**
	 * @param optionQuestion the optionQuestion to set
	 */
	public void setOptionQuestion(List<Object> optionQuestion) {
		this.optionQuestion = optionQuestion;
	}

}
