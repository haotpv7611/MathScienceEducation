package com.example.demo.dtos;

public class OptionQuestionGameDTO {
	private long id;
	private String optionImageUrl;
	private String optionText;
	private String wrongOptionText;

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
	 * @return the wrongOptionText
	 */
	public String getWrongOptionText() {
		return wrongOptionText;
	}

	/**
	 * @param wrongOptionText the wrongOptionText to set
	 */
	public void setWrongOptionText(String wrongOptionText) {
		this.wrongOptionText = wrongOptionText;
	}

}
