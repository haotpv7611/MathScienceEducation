package com.example.demo.dtos;

public class ExerciseResponseDTO {
	private long id;
	private String exerciseName;	
	private String description;
//	private long lessonId;
//	private long progressTestId;
//	private boolean isProgressTest;

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

//	public long getLessonId() {
//		return lessonId;
//	}
//
//	public void setLessonId(long lessonId) {
//		this.lessonId = lessonId;
//	}
//
//	public long getProgressTestId() {
//		return progressTestId;
//	}
//
//	public void setProgressTestId(long progressTestId) {
//		this.progressTestId = progressTestId;
//	}
//
//	/**
//	 * @return the isProgressTest
//	 */
//	public boolean isProgressTest() {
//		return isProgressTest;
//	}
//
//	/**
//	 * @param isProgressTest the isProgressTest to set
//	 */
//	public void setProgressTest(boolean isProgressTest) {
//		this.isProgressTest = isProgressTest;
//	}

}
