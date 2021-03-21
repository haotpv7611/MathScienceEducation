package com.example.demo.dtos;

public class QuestionDTO {

	private long id;
	private String questionText;
	private String questionImageUrl;
	private String questionAudioUrl;
	private float score;
	private long unitId;
	private long questionTypeId;

	public QuestionDTO() {
	}

	public QuestionDTO(long id, String questionText, String questionImageUrl, String questionAudioUrl, float score,
			long unitId, long questionTypeId) {
		super();
		this.id = id;
		this.questionText = questionText;
		this.questionImageUrl = questionImageUrl;
		this.questionAudioUrl = questionAudioUrl;
		this.score = score;
		this.unitId = unitId;
		this.questionTypeId = questionTypeId;
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
	 * @return the questionTypeId
	 */
	public long getQuestionTypeId() {
		return questionTypeId;
	}

	/**
	 * @param questionTypeId the questionTypeId to set
	 */
	public void setQuestionTypeId(long questionTypeId) {
		this.questionTypeId = questionTypeId;
	}
}
