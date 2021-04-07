package com.example.demo.dtos;

public class OptionQuestionDTO {
	private long id;
	private String optionText;
	private String optionImageUrl;
	private String optionInputType;
	private boolean isCorrect;

	public OptionQuestionDTO() {
	}

	public OptionQuestionDTO(long id, String optionText, boolean isCorrect) {
		super();
		this.id = id;
		this.optionText = optionText;
		this.isCorrect = isCorrect;
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
	 * @return the optionText
	 */
	public String getOptionText() {
		return optionText;
	}

	/**
	 * @param optionText the optionText to set
	 */
	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}

	/**
	 * @return the isCorrect
	 */
	public boolean isCorrect() {
		return isCorrect;
	}

	/**
	 * @param isCorrect the isCorrect to set
	 */
	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	/**
	 * @return the optionImageUrl
	 */
	public String getOptionImageUrl() {
		return optionImageUrl;
	}

	/**
	 * @param optionImageUrl the optionImageUrl to set
	 */
	public void setOptionImageUrl(String optionImageUrl) {
		this.optionImageUrl = optionImageUrl;
	}

	/**
	 * @return the optionInputType
	 */
	public String getOptionInputType() {
		return optionInputType;
	}

	/**
	 * @param optionInputType the optionInputType to set
	 */
	public void setOptionInputType(String optionInputType) {
		this.optionInputType = optionInputType;
	}

}
