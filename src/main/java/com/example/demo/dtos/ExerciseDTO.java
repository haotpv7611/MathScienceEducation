package com.example.demo.dtos;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class ExerciseDTO {
	private long id;
	@NotNull(message = "Exercise Name must be not null !")
	@Length(max = 20, message = "Exercise Name lenght must be less than 20 !")
	private String exerciseName;
	private long lessonId;
	private long progressTestId;
	@Length(max = 50, message = "Description lenght must be less than 50 !")
	private String description;
	private boolean isProgressTest;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getExerciseName() {
		return exerciseName;
	}

	public void setExerciseName(String exerciseName) {
		this.exerciseName = exerciseName;
	}

	public long getLessonId() {
		return lessonId;
	}

	public void setLessonId(long lessonId) {
		this.lessonId = lessonId;
	}

	public long getProgressTestId() {
		return progressTestId;
	}

	public void setProgressTestId(long progressTestId) {
		this.progressTestId = progressTestId;
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
