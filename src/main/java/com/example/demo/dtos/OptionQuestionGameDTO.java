package com.example.demo.dtos;

public class OptionQuestionGameDTO {
	private long id;
	private String optionImageUrl;
	private String optionText;

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

}
