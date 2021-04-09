package com.example.demo.dtos;

import java.util.List;

public class QuestionExerciseViewDTO {
	private long id;
	private String questionTitle;
	private String questionImageUrl;
	private String questionAudioUrl;
	private float score;
	private List<OptionQuestionExerciseDTO> optionList;

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
	public List<OptionQuestionExerciseDTO> getOptionList() {
		return optionList;
	}

	/**
	 * @param option the option to set
	 */
	public void setOptionList(List<OptionQuestionExerciseDTO> optionList) {
		this.optionList = optionList;
	}

}
