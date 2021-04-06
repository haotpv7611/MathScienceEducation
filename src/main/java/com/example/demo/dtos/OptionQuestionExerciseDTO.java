package com.example.demo.dtos;

public class OptionQuestionExerciseDTO {
	private long id;
	private String optionText;
	private boolean isCorrect;

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
	public boolean getIsCorrect() {
		return isCorrect;
	}

	/**
	 * @param isCorrect the isCorrect to set
	 */
	public void setIsCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

}
