package com.example.demo.dtos;

import java.util.List;

public class QuestionViewDTO {
	private long id;
	private String questionText;
	private String questionImageUrl;
	private String questionAudioUrl;
	private float score;

	private List<OptionQuestionDTO> option;

	public QuestionViewDTO() {
	}

	public QuestionViewDTO(long id, String questionText, String questionImageUrl, String questionAudioUrl, float score,
			List<OptionQuestionDTO> option) {
		super();
		this.id = id;
		this.questionText = questionText;
		this.questionImageUrl = questionImageUrl;
		this.questionAudioUrl = questionAudioUrl;
		this.score = score;
		this.option = option;
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
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * @param questionText the questionText to set
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
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
	public List<OptionQuestionDTO> getOption() {
		return option;
	}

	/**
	 * @param option the option to set
	 */
	public void setOption(List<OptionQuestionDTO> option) {
		this.option = option;
	}

}
