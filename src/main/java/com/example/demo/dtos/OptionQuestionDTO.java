package com.example.demo.dtos;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class OptionQuestionDTO {

	private long id;
	@NotNull(message = "optionText must be not null!")
	@Length(max = 30, message = "optionText length must be less than 30!")
	private String optionText;
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
}
