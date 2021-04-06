package com.example.demo.dtos;

public class OptionQuestionFillDTO {
	private long id;
	private String text;
	private String operator;
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
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
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
