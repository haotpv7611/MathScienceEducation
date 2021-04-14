package com.example.demo.dtos;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Length;

public class GameRequestDTO {
	@Min(value = 1, message = "Game Name must be greater than 0!")
	private int gameName;
	@Length(max = 50, message = "ClassName length must be less than 50!")
	private String description;
	private long lessonId;

	/**
	 * @return the gameName
	 */
	public int getGameName() {
		return gameName;
	}

	/**
	 * @param gameName the gameName to set
	 */
	public void setGameName(int gameName) {
		this.gameName = gameName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the lessonId
	 */
	public long getLessonId() {
		return lessonId;
	}

	/**
	 * @param lessonId the lessonId to set
	 */
	public void setLessonId(long lessonId) {
		this.lessonId = lessonId;
	}

}
