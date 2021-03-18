package com.example.demo.dtos;

public class ExerciseDTO {

	private Long id;
	private int exerciseName;
	private Long lessonId;
	private Long progressTestId;
	
	public ExerciseDTO() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getExerciseName() {
		return exerciseName;
	}

	public void setExerciseName(int exerciseName) {
		this.exerciseName = exerciseName;
	}

	public Long getLessonId() {
		return lessonId;
	}

	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}

	public Long getProgressTestId() {
		return progressTestId;
	}

	public void setProgressTestId(Long progressTestId) {
		this.progressTestId = progressTestId;
	}

	public ExerciseDTO(Long id, int exerciseName, Long lessonId, Long progressTestId) {
		super();
		this.id = id;
		this.exerciseName = exerciseName;
		this.lessonId = lessonId;
		this.progressTestId = progressTestId;
	}
	
	
	
}
