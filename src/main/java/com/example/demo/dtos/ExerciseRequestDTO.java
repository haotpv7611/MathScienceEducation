package com.example.demo.dtos;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class ExerciseRequestDTO {
	@NotNull(message = "Exercise Name must be not null !")
	@Length(max = 20, message = "Exercise Name lenght must be less than 20 !")
	private String exerciseName;
	private long lessonId;
	private long progressTestId;
	@Length(max = 50, message = "Description lenght must be less than 50 !")
	private String description;
	private boolean isProgressTest;

	/**
	 * @return the exerciseName
	 */
	public String getExerciseName() {
		return exerciseName;
	}

	/**
	 * @param exerciseName the exerciseName to set
	 */
	public void setExerciseName(String exerciseName) {
		this.exerciseName = exerciseName;
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

	/**
	 * @return the progressTestId
	 */
	public long getProgressTestId() {
		return progressTestId;
	}

	/**
	 * @param progressTestId the progressTestId to set
	 */
	public void setProgressTestId(long progressTestId) {
		this.progressTestId = progressTestId;
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
	 * @return the isProgressTest
	 */
	public boolean isProgressTest() {
		return isProgressTest;
	}

	/**
	 * @param isProgressTest the isProgressTest to set
	 */
	public void setProgressTest(boolean isProgressTest) {
		this.isProgressTest = isProgressTest;
	}

}
