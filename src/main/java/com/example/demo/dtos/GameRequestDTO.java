package com.example.demo.dtos;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class GameRequestDTO {
	@NotNull(message = "ClassName must be not null!")
	@Length(max = 50, message = "ClassName length must be less than 50!")
	private String gameName;
	@Length(max = 50, message = "ClassName length must be less than 50!")
	private String description;
	private long lessonId;

	/**
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * @param gameName the gameName to set
	 */
	public void setGameName(String gameName) {
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
