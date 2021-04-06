package com.example.demo.dtos;

public class OptionQuestionFillDTO {
	private long id;
	private String optionText;
	private String optionInputType;

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
