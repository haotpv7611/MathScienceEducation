package com.example.demo.dtos;

public class ExerciseDTO {

	private long id;
	private int exerciseName;
	private long lessonId;
	private long progressTestId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getExerciseName() {
		return exerciseName;
	}
	public void setExerciseName(int exerciseName) {
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
	public ExerciseDTO(long id, int exerciseName, long lessonId, long progressTestId) {
		super();
		this.id = id;
		this.exerciseName = exerciseName;
		this.lessonId = lessonId;
		this.progressTestId = progressTestId;
	}
	public ExerciseDTO() {
		
	}
	
	
	
	
	
}
